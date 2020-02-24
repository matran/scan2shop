package com.alienware.scan2shop.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.data.Receipt;
import com.alienware.scan2shop.data.SearchItem;

import java.util.Collections;
import java.util.List;
/**
 * Created by henry cheruiyot on 3/27/2018.
 */

public class ReceiptAdapter  extends RecyclerView.Adapter<ReceiptAdapter.MyHolder>{
     Context context;
    SearchItem current;
    int currentPos = 0;
    private List<Receipt> data = Collections.emptyList();
    private LayoutInflater inflater;

    public ReceiptAdapter(Context paramContext, List<Receipt> paramList)
    {
        this.context = paramContext;
        this.inflater = LayoutInflater.from(paramContext);
        this.data = paramList;
    }

    public int getItemCount()
    {
        return this.data.size();
    }

    public void onBindViewHolder(final MyHolder myHolder, int paramInt)
    {
        Receipt receipt= data.get(paramInt);
        myHolder.textName.setText(receipt.getName());
        myHolder.textQ.setText("x" + receipt.getQuantity());
        myHolder.textPrice.setText("Ksh." + receipt.getTotal()+".00");
    }

    public MyHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
        return new MyHolder(inflater.inflate(R.layout.receipt_layout, paramViewGroup, false));
    }

   public class MyHolder extends RecyclerView.ViewHolder {

        TextView textName;
       TextView textQ;
        TextView textPrice;


        public MyHolder(View paramView) {
            super(paramView);
            textName = paramView.findViewById(R.id.receiptName);
            textQ= paramView.findViewById(R.id.receipQ);
            textPrice = paramView.findViewById(R.id.receiptPrice);


        }
    }
}
