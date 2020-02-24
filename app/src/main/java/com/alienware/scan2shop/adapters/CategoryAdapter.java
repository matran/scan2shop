package com.alienware.scan2shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.data.Category;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<Category> data = Collections.emptyList();
    private LayoutInflater layoutInflater;
    public CategoryAdapter(Context paramContext, List<Category> paramList) {
        this.context = paramContext;
        this.layoutInflater = LayoutInflater.from(paramContext);
        this.data = paramList;

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder paramMyViewHolder, int position) {
        Category category = data.get(position);
        paramMyViewHolder.itemName.setText(category.getName());
        paramMyViewHolder.itemDescription.setText(category.getDescription());
        Glide.with(context).load(category.getImage())
                .into(paramMyViewHolder.itemIMage);

    }

    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
        View view=layoutInflater.inflate(R.layout.category_items, paramViewGroup, false);
        return new CategoryAdapter.MyViewHolder(view);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemDescription;
        public ImageView itemIMage;
        public TextView itemName;
        public MyViewHolder(View paramView) {
            super(paramView);
            itemName = paramView.findViewById(R.id.itemCame);
            itemDescription = paramView.findViewById(R.id.itemCDescription);
            itemIMage = paramView.findViewById(R.id.itemCImage);

        }
    }


}
