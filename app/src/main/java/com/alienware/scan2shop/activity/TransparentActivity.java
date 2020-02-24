package com.alienware.scan2shop.activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.Item;
import com.alienware.scan2shop.data.Receipt;
import com.alienware.scan2shop.helpers.ItemHelper;
import com.alienware.scan2shop.helpers.ReceiptManager;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class TransparentActivity extends AppCompatActivity {
    private String barcode;
    private int quantity;
    private ItemHelper helper;
    private List<Item> product;
    private static int code;
    private static Double total;
    private  String payment_id;
    private ProgressBar progressBar;
    private UserSqliteHandler userSqliteHandler;
    private static String phone;
    private static String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        progressBar= findViewById(R.id.progressTransparent);
        userSqliteHandler=new UserSqliteHandler(getApplicationContext());
        product = new ArrayList<>();
        helper = new ItemHelper(getApplicationContext());
        code = helper.getProductCode();
        phone=userSqliteHandler.getPhoneNo();
        price= String.valueOf(helper.getDbTotal());
        startPayment(phone,price);
    }
    private String  generateReceiptId(){
        //UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        String id=java.util.UUID.randomUUID().toString();
        ReceiptManager receiptManager=new ReceiptManager(getApplicationContext());
        receiptManager.setReceiptId(id);
        return id;
    }
    private void startPayment(final String phoneNo, final String amount) {
        List<Receipt> receipt = helper.getReceipt();
        final int quantity=helper.getDbQuantity();
        final String receiptid=generateReceiptId();
        final String receiptStr = new Gson().toJson(receipt);
        final String url = Config.URL_PAYMENT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("amount", amount);
        params.put("totalquantity",String.valueOf(quantity));
        params.put("phonenumber", phoneNo);
        params.put("receiptid",receiptid);
        params.put("receipt",receiptStr);
        JsonObjectRequest str = new JsonObjectRequest(url,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject message=response.getJSONObject("message");
                    String coderesponse="";
                    coderesponse=message.getString("ResponseCode");
                    if (coderesponse.equals("0")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        showDialogBuilder("Wait to enter MPESA PIN.");
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        showDialogBuilder("Not sent try again");
                    }
                }
                catch (Exception e) {
                    Log.e("mpesaexceiption", e.getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    showDialogBuilder("Not sent try again");
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyerror)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),volleyerror.getMessage(),Toast.LENGTH_LONG).show();
                showDialogBuilder("Network error try again");
            }
        })
        {
        };
        str.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_payment");
    }
    private void showDialogBuilder(String message) {
        final AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage(message).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                finish();
            }
        });
        localBuilder.create().show();
    }


}


