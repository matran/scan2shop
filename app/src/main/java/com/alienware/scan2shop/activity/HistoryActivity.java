package com.alienware.scan2shop.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.HistoryAdapter;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.History;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
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
/**
 * Created by henry cheruiyot on 3/1/2018.
 */
public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<History>historyList;
    private List<History>list;
    private Toolbar toolbar;
    private View hLoadingBar;
    private UserSqliteHandler user;
    private TextView progressText;
    private ImageView historyImage;
    private TextView historyMessageTextView;
    private RelativeLayout historyOverlay;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        hLoadingBar=findViewById(R.id.hLoadingBar);
        user=new UserSqliteHandler(HistoryActivity.this);
        recyclerView= findViewById(R.id.history_recycler_view);
        historyOverlay=findViewById(R.id.historyOverlay);
        historyImage=findViewById(R.id.historyImage);
        historyMessageTextView=findViewById(R.id.historyMessageTextView);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");
        progressText=findViewById(R.id.pbText);
        progressText.setText("Loading...");
        historyList= new ArrayList<>();
        fetchHistory(user.getPhoneNo());
    }
    private void clearHIstory(){
        historyList.clear();
        historyAdapter.notifyDataSetChanged();
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        return super.onOptionsItemSelected(paramMenuItem);
    }
    private void fetchHistory(String phoneno){
        hLoadingBar.setVisibility(View.VISIBLE);
        String url= Config.URL_HISTORY+phoneno;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hLoadingBar.setVisibility(View.GONE);
                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray products = object.getJSONArray("history");
                    if(products.length()>0) {
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject product = (JSONObject) products.get(i);
                            String receiptid = product.getString("receiptid");
                            int barcode = product.getInt("barcode");
                            String date = product.getString("date");
                            int quantity = product.getInt("totalquantity");
                            BigDecimal price = new BigDecimal(product.getString("amount"));
                            History history = new History(String.valueOf(barcode), date, price.intValue(), quantity, receiptid);
                            historyList.add(history);
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        historyAdapter = new HistoryAdapter(HistoryActivity.this, historyList, new HistoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(History history) {
                                Intent intent = new Intent(HistoryActivity.this, HReceiptActivity.class);
                                intent.putExtra("receiptid", history.getReceiptid());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                    }else{
                        historyOverlay.setVisibility(View.VISIBLE);
                        historyImage.setImageResource(R.mipmap.history_icon);
                        historyMessageTextView.setText("No history");
                    }
                } catch (Exception e) {
                    hLoadingBar.setVisibility(View.GONE);
                    historyOverlay.setVisibility(View.VISIBLE);
                    historyImage.setImageResource(R.mipmap.error_in_network);
                    historyMessageTextView.setText(R.string.connection_error);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hLoadingBar.setVisibility(View.GONE);
                historyOverlay.setVisibility(View.VISIBLE);
                historyImage.setImageResource(R.mipmap.error_in_network);
                historyMessageTextView.setText(R.string.connection_error);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"historyrequest");
    }

    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("historyrequest");

    }
}
