package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.ReceiptAdapter;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.Receipt;
import com.alienware.scan2shop.helpers.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HReceiptActivity extends AppCompatActivity {
    private ReceiptAdapter receiptAdapter;
    private List<Receipt>receiptList;
    private RecyclerView recyclerView;
    private LinearLayout receiptContent;
    private RelativeLayout errorSection;
    private View rProgressView;
    private TextView progressText;
    private TextView amountTextView;
    private ImageView barcodeImageView;
    private String receiptid;
    private String barcode="";
    private String date="";
    private String amount="";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_receipt_activity);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Receipt");
        recyclerView = findViewById(R.id.receiptRecycle);
        receiptContent= findViewById(R.id.receiptContentArea);
        errorSection= findViewById(R.id.NoReceipt);
        rProgressView=findViewById(R.id.rrProgressBar);
        progressText=findViewById(R.id.pbText);
        amountTextView= findViewById(R.id.amount);
        barcodeImageView=findViewById(R.id.barcodeImage);
        progressText.setText("Loading...");
        receiptContent.setVisibility(View.INVISIBLE);
        receiptList=new ArrayList<>();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                receiptid=null;
            } else {
                receiptid=extras.getString("receiptid");
            }
        } else {
            receiptid = (String) savedInstanceState.getSerializable("receiptid");
        }
        fetchReceipt(receiptid);
      }
    private void fetchReceipt(final String receipt_id){
        rProgressView.setVisibility(View.VISIBLE);
        String url=Config.URL_RECEIPT+receipt_id;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rProgressView.setVisibility(View.GONE);
                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray products = object.getJSONArray("receipt");
                    if(products.length()>0) {
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject product = (JSONObject) products.get(i);
                            receiptid = product.getString("receiptid");
                            barcode = product.getString("barcode");
                            date = product.getString("date");
                            amount = product.getString("amount");
                            String name = product.getString("name");
                            int quantity = product.getInt("quantity");
                            BigDecimal price = new BigDecimal(product.getString("price"));
                            Receipt r = new Receipt(name, quantity, price);
                            receiptList.add(r);
                        }
                        receiptAdapter = new ReceiptAdapter(getApplicationContext(), receiptList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(receiptAdapter);
                        receiptAdapter.notifyDataSetChanged();
                        amountTextView.setText("ksh."+amount+".00");
                        receiptContent.setVisibility(View.VISIBLE);
                        errorSection.setVisibility(View.INVISIBLE);
                        barcodeImageView.setImageBitmap(Utils.createBarcodeBitmap(barcode, 650, 250));
                    }else{
                        receiptContent.setVisibility(View.INVISIBLE);
                        errorSection.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    rProgressView.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rProgressView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }

        })
        {
        };
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"hreceiptrequest");
    }
    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("hreceiptrequest");
    }
}
