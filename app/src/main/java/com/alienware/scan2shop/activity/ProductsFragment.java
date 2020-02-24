package com.alienware.scan2shop.activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.GoodsAdapter;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.Goods;
import com.alienware.scan2shop.helpers.GoodsHelper;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class ProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private GoodsAdapter goodsAdapter;
    private List<Goods> goodsList;
    private RelativeLayout errorShow;
    private Context mContext;
    private GoodsHelper goodsHelper;
    private static int code = 1;
    private Button retryButton;
    private ProgressBar productProgress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_layout, container, false);
        fetchGoods();
        recyclerView = view.findViewById(R.id.goods_recycler_view);
        retryButton= view.findViewById(R.id.productRetryButton);
        errorShow= view.findViewById(R.id.errorInternet);
        productProgress= view.findViewById(R.id.progressProducts);
        mContext = getContext();
        goodsHelper = new GoodsHelper(mContext);
        code = goodsHelper.getProductCode();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        goodsList = new ArrayList();
        goodsAdapter = new GoodsAdapter(mContext, goodsList);
        recyclerView.setAdapter(goodsAdapter);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorShow.setVisibility(View.INVISIBLE);
                productProgress.setVisibility(View.VISIBLE);
                fetchGoods();
            }
        });
        fetchGoods();
        return view;
    }
    private void fetchGoods() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, Config.URL_GOODS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                productProgress.setVisibility(View.INVISIBLE);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray products = object.getJSONArray("products");
                    for (int i = 0; i < products.length(); i++) {
                        code=code + 1;
                        JSONObject product = (JSONObject) products.get(i);
                        String name = product.getString("name");
                        String description = product.getString("description");
                        String price =product.getString("price");
                        String image = product.getString("image");
                        Goods goods = new Goods(code,name,description, price, image);
                        goodsList.add(goods);
                    }
                    goodsAdapter = new GoodsAdapter(mContext, goodsList);
                    recyclerView.setAdapter(goodsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    goodsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    productProgress.setVisibility(View.INVISIBLE);
                    errorShow.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productProgress.setVisibility(View.INVISIBLE);
                errorShow.setVisibility(View.VISIBLE);
            }
        });
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq);
    }
}
