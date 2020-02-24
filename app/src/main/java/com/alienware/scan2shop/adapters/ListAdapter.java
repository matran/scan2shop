package com.alienware.scan2shop.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.MyListActivity;
import com.alienware.scan2shop.data.MyListData;
import com.alienware.scan2shop.helpers.MyListHelper;
import java.util.Collections;
import java.util.List;
/**
 * Created by henry cheruiyot on 3/3/2018.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>{
    private Context context;
    private List<MyListData> data = Collections.emptyList();
    private LayoutInflater layoutInflater;
    private MyListHelper helper;
    public ListAdapter(Context paramContext, List<MyListData> paramList) {
        this.context = paramContext;
        this.layoutInflater = LayoutInflater.from(paramContext);
        this.data = paramList;
    }
    public int getItemCount() {
       return data.size();
    }
    public void onBindViewHolder(final MyViewHolder paramMyViewHolder, final int paramInt) {
        final MyListData localProduct = data.get(paramInt);
        paramMyViewHolder.itemName.setText(localProduct.getMyListName());
        if(localProduct.getTotal().equals("0.0") || localProduct.getTotal().equals("0") || localProduct.getMyListQuantity().equals("1")){
            paramMyViewHolder.itemPrice.setText( "");
            paramMyViewHolder.itemQuantity.setText("");
        }else {
            paramMyViewHolder.itemQuantity.setText( localProduct.getMyListQuantity() );
            paramMyViewHolder.itemPrice.setText("ksh."+localProduct.getTotal() +".00");
        }
        paramMyViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MyListActivity myListActivity= (MyListActivity) context;
                myListActivity.startMyListDialog(localProduct.getMyListName(),localProduct.getMyListQuantity(),localProduct.getMyListPrice(),localProduct.getMyListCode(),paramInt,localProduct.getTotal());
            }
        });
    }
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new ListAdapter.MyViewHolder(layoutInflater.inflate(R.layout.mylist_items, paramViewGroup, false));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName;
        public TextView itemPrice;
        public TextView itemQuantity;
        public Button overflow;
        public MyViewHolder(View paramView) {
            super(paramView);
            itemName = paramView.findViewById(R.id.mylistpName);
            itemQuantity = paramView.findViewById(R.id.mylistquantity);
            itemPrice = paramView.findViewById(R.id.mylistprice);
            overflow = paramView.findViewById(R.id.mylistMoreButton);
        }
    }
}
