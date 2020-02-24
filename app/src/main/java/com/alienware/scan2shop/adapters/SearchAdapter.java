package com.alienware.scan2shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.SearchActivity;
import com.alienware.scan2shop.data.SearchItem;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by henry cheruiyot on 2/4/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    SearchItem current;
    int currentPos = 0;
    private List<SearchItem> data = Collections.emptyList();
    private LayoutInflater inflater;

    public SearchAdapter(Context paramContext, List<SearchItem> paramList)
    {
        this.context = paramContext;
        this.inflater = LayoutInflater.from(paramContext);
        this.data = paramList;
    }

    public int getItemCount()
    {
        return this.data.size();
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
        MyHolder data = (MyHolder)paramViewHolder;
        final SearchItem localSearchItem = this.data.get(paramInt);
        Glide.with(context).load(localSearchItem.getImage())
                .into(data.searchImage);
        data.textName.setText(localSearchItem.getPname());
        data.sDescription.setText(localSearchItem.getDescription());
        String str = String.valueOf(localSearchItem.getPrice());
        data.textPrice.setText("Ksh." + str);
        data.addTocart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {

                ((SearchActivity)context).startQuantityDialog(localSearchItem.pname, localSearchItem.getDescription(), localSearchItem.getPrice(),localSearchItem.getImage());

            }


        });
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
        return new MyHolder(this.inflater.inflate(R.layout.container_search, paramViewGroup, false));
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView searchImage;
        Button addTocart;
        TextView textName;
        TextView sDescription;
        TextView textPrice;
        public MyHolder(View paramView) {
            super(paramView);
            this.searchImage= paramView.findViewById(R.id.searchImage);
            this.textName = paramView.findViewById(R.id.sName);
            this.sDescription= paramView.findViewById(R.id.sDescription);
            this.textPrice = paramView.findViewById(R.id.sPrice);
            this.addTocart = paramView.findViewById(R.id.sAddToCart);
        }
    }
}
