package com.alienware.scan2shop.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.data.History;

import java.util.Collections;
import java.util.List;
/**
 * Created by henry cheruiyot on 3/3/2018.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private Context context;
    private List<History> data = Collections.emptyList();
    private LayoutInflater layoutInflater;
    private  OnItemClickListener listener;
    public HistoryAdapter(Context paramContext, List<History> paramList,OnItemClickListener listener)
    {
        this.context = paramContext;
        this.layoutInflater = LayoutInflater.from(paramContext);
        this.data = paramList;
        this.listener=listener;
    }
    public int getItemCount() {
        return this.data.size();
    }
    public void onBindViewHolder(final HistoryAdapter.MyViewHolder paramMyViewHolder, int paramInt) {
        History localProduct = data.get(paramInt);
        paramMyViewHolder.bind(localProduct,listener);
        paramMyViewHolder.dateTx.setText(localProduct.getDate());
        paramMyViewHolder.receiptTx.setText(localProduct.getId());
        paramMyViewHolder.amountTx.setText( "ksh." + localProduct.getAmount() +".00");
        paramMyViewHolder.noOfItemsTx.setText(localProduct.getItems() +" items");
    }
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new HistoryAdapter.MyViewHolder(layoutInflater.inflate(R.layout.receipts_items, paramViewGroup, false));
    }

    public interface OnItemClickListener {
        void onItemClick(History history);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTx, receiptTx, amountTx, noOfItemsTx;

        public MyViewHolder(View paramView) {
            super(paramView);
            dateTx = paramView.findViewById(R.id.dateTextView);
            receiptTx = paramView.findViewById(R.id.receiptTextView);
            amountTx = paramView.findViewById(R.id.amountTextView);
            noOfItemsTx = paramView.findViewById(R.id.noOfItemsTextView);
        }

        public void bind(final History item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);

                }
            });
        }
    }
}
