package com.alienware.scan2shop.activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.CategoryAdapter;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.Category;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class CategoryFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private List<Category>categoryList;
    private RecyclerView category_recycleView;
    private Context mContext;
    private ProgressBar category_progress;
    private RelativeLayout categoryLayout;
    private Button retryButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        categoryList=new ArrayList<>();
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategories();
            }
        });
        getCategories();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_layout, container, false);
        category_recycleView= view.findViewById(R.id.category_recyclerView);
        category_progress= view.findViewById(R.id.progressCategory);
        categoryLayout= view.findViewById(R.id.errorCategory);
        retryButton= view.findViewById(R.id.categoryRetryButton);
        return view;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getCategories(){
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, Config.URL_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                category_progress.setVisibility(View.INVISIBLE);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray products = object.getJSONArray("products");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject product = (JSONObject) products.get(i);
                        String name = product.getString("name");
                        String description = product.getString("description");
                        String image = product.getString("image");
                        Category category = new Category(name,description,image);
                        categoryList.add(category);
                    }
                    categoryAdapter = new CategoryAdapter(mContext, categoryList);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                    category_recycleView.setLayoutManager(mLayoutManager);
                    category_recycleView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    category_recycleView.setItemAnimator(new DefaultItemAnimator());
                    category_recycleView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    category_progress.setVisibility(View.INVISIBLE);
                    categoryLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                category_progress.setVisibility(View.INVISIBLE);
                categoryLayout.setVisibility(View.VISIBLE);
            }
        });
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq);
    }
}
