package com.alienware.scan2shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.data.MyListData;

import java.util.List;

/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class MyListAdapter  extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> {
    private static String TAG = "mylistadapter";
    Context context;
    LayoutInflater inflater;
    List<MyListData> productslist;

    public MyListAdapter(Context paramContext, List<MyListData> paramList)
    {
        this.context = paramContext;
        this.productslist = paramList;
        this.inflater = LayoutInflater.from(paramContext);
    }

    @Override

    public int getItemCount(){

        return productslist.size();
    }

    @Override
    public MyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        View view=inflater.inflate(R.layout.mylist_items, paramViewGroup, false);
        return new MyListAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position){

        MyListData myListData= productslist.get(position);
        myViewHolder.itemName.setText(myListData.getMyListName());
        myViewHolder.price.setText(myListData.getMyListPrice() + ".00");
        myViewHolder.quantity.setText("(" + myListData.getMyListQuantity() + ")");
        myViewHolder.MoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {

            }
        });
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
       public ImageView MoreButton;
       public TextView itemName;
       public TextView price;
       public TextView quantity;
       public MyViewHolder(View view){
           super(view);
           itemName = view.findViewById(R.id.mylistpName);
           price = view.findViewById(R.id.mylistprice);
           quantity = view.findViewById(R.id.mylistquantity);
           MoreButton = view.findViewById(R.id.mylistMoreButton);
       }

    }

}
