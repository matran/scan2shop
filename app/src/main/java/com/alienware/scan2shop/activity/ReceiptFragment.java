package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.ReceiptAdapter;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.Receipt;
import com.alienware.scan2shop.helpers.ReceiptManager;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
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
/**
 * Created by henry cheruiyot on 3/27/2018.
 */
public class ReceiptFragment extends Fragment {
    private ReceiptAdapter receiptAdapter;
    private RecyclerView recyclerView;
    private List<Receipt>receiptList;
    private TextView amountTextView;
    private ReceiptManager receiptManager;
    private RelativeLayout relativeLayout;
    private LinearLayout receiptSection;
    private View rProgressView;
    private TextView progressText;
    private ImageView barcodeImageView;
    private String receiptid="";
    private String barcode="";
    private String date="";
    private String amount="";
    private ImageView receiptImage;
    private TextView messageTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.receipt_activity,container,false);
            amountTextView= view.findViewById(R.id.amount);
            relativeLayout = view.findViewById(R.id.NoReceipt);
            receiptSection= view.findViewById(R.id.receiptContentArea);
            rProgressView=view.findViewById(R.id.rrProgressBar);
            progressText=view.findViewById(R.id.pbText);
            barcodeImageView=view.findViewById(R.id.barcodeImage);
            recyclerView = view.findViewById(R.id.receiptRecycle);
            receiptImage=view.findViewById(R.id.receiptImage);
            messageTextView=view.findViewById(R.id.receiptMessageTextView);
            progressText.setText("Loading...");
            receiptManager=new ReceiptManager(getContext());
            receiptList=new ArrayList<>();
            fetchReceipt();
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private void fetchReceipt(){
        UserSqliteHandler userSqliteHandler=new UserSqliteHandler(getContext());
        String phoneno=userSqliteHandler.getPhoneNo();
        rProgressView.setVisibility(View.VISIBLE);
        String url=Config.URL_RESENT+phoneno;
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
                        receiptManager.setCurrentReceipt(receiptid);
                        receiptAdapter = new ReceiptAdapter(getContext(), receiptList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(receiptAdapter);
                        receiptAdapter.notifyDataSetChanged();
                        amountTextView.setText("ksh."+amount+".00");
                        receiptSection.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.INVISIBLE);
                        barcodeImageView.setImageBitmap(Utils.createBarcodeBitmap(barcode, 650, 250));
                    }else{
                        receiptSection.setVisibility(View.INVISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        receiptImage.setImageResource(R.mipmap.receipt_image);
                        messageTextView.setText("No receipt.");
                    }
                } catch (Exception e) {
                    rProgressView.setVisibility(View.GONE);
                    receiptSection.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    receiptImage.setImageResource(R.mipmap.error_in_network);
                    messageTextView.setText(R.string.connection_error);
                 Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rProgressView.setVisibility(View.GONE);
                receiptSection.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                receiptImage.setImageResource(R.mipmap.error_in_network);
                messageTextView.setText(R.string.connection_error);
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"receiptrequest");
    }
    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("receiptrequest");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
