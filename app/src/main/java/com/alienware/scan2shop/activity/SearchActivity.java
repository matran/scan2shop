package com.alienware.scan2shop.activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.SearchAdapter;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.SearchItem;
import com.alienware.scan2shop.dialogs.SearchQuantityDialog;
import com.alienware.scan2shop.helpers.ItemHelper;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class SearchActivity extends AppCompatActivity {
    private static String TAG = "SEARCH";
    private View ProgressOverlay;
    private TextView errorTextView;
    private ItemHelper helper;
    private AlphaAnimation inAnimation;
    private ProgressBar progressBar;
    private Button retryButton;
    private SearchAdapter searchAdapter;
    private RecyclerView searchItemView;
    private  Cursor c;
    private List<SearchItem> productsList;
    private static int code;
    SearchView searchView = null;
    private Toolbar toolbar;
    private void parseIntent(Intent paramIntent) {
        if (Intent.ACTION_SEARCH.equals(paramIntent.getAction())) {
            String query=paramIntent.getStringExtra(SearchManager.QUERY);
            try {
                fetchProducts(query);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    protected void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        setContentView(R.layout.search_result);
        this.searchItemView = findViewById(R.id.searchlist);
        this.toolbar = findViewById(R.id.toolbarSearch);
        helper=new ItemHelper(getApplicationContext());
        code = helper.getProductCode();
        productsList = new ArrayList<SearchItem>();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.progressBar = findViewById(R.id.progressBar);
        this.ProgressOverlay = findViewById(R.id.progress_overlay);
        this.errorTextView = findViewById(R.id.errorTextView);
        this.retryButton = findViewById(R.id.retryButton);
        parseIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        parseIntent(getIntent());
    }
    private void fetchProducts(final String query) throws Exception{
        inAnimation = new AlphaAnimation(0.0F, 1.0F);
        inAnimation.setDuration(200L);
        ProgressOverlay.setAnimation(this.inAnimation);
        ProgressOverlay.setVisibility(View.VISIBLE);
        String url=Config.URL_SEARCH+query;
        StringRequest  jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressOverlay.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray products = object.getJSONArray("products");

                    if(products.length()>0) {
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject product = (JSONObject) products.get(i);
                            String name = product.getString("name");
                            String description = product.getString("description");
                            int price = product.getInt("price");
                            String image = product.getString("image");
                            Log.e("search",image);
                            SearchItem p = new SearchItem(name, description, price, image);
                            productsList.add(p);
                        }
                        searchAdapter = new SearchAdapter(SearchActivity.this, productsList);
                        searchItemView.setAdapter(searchAdapter);
                        searchItemView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                        searchAdapter.notifyDataSetChanged();
                    }else {
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText(R.string.no_item_found);
                    }
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                   progressBar.setVisibility(View.GONE);
                   errorTextView.setVisibility(View.VISIBLE);
                   errorTextView.setText(R.string.connection_error);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
               progressBar.setVisibility(View.GONE);
               errorTextView.setVisibility(View.VISIBLE);
               errorTextView.setText(R.string.connection_error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                UserSqliteHandler user=new UserSqliteHandler(SearchActivity.this);
                String token=user.getuid();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

        };
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"searchrequest");
    }

    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("searchrequest");
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent(SearchActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
                SearchActivity.this.finish();
                break;
        }
        return true;
    }

    public void sendResultToCart(int quantity, String name, String description, int price, String image)
    {
        code=code+1;
        int total=quantity * price;
        helper.addProduct(code,name,description,price,quantity,total,image);
        Toast.makeText(this, "Added to cart", Toast.LENGTH_LONG).show();
    }

    public void startQuantityDialog(String name, String description,int price,String image) {
        SearchQuantityDialog localSearchQuantityDialog = new SearchQuantityDialog();
        localSearchQuantityDialog.setData(name,description, price,image);
        localSearchQuantityDialog.show(getSupportFragmentManager(), "quantity2");
    }
}
