package com.alienware.scan2shop.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.MainActivity;
import com.alienware.scan2shop.data.Product;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{
    private Context context;
    private List<Product> data = Collections.emptyList();
    private LayoutInflater layoutInflater;
    public ItemAdapter(Context paramContext, List<Product> paramList) {
        this.context = paramContext;
        this.layoutInflater = LayoutInflater.from(paramContext);
        this.data = paramList;

    }
    private void showPopupMenu(View paramView, int paramInt) {
        PopupMenu popupMenu = new PopupMenu(this.context, paramView);
        popupMenu.getMenuInflater().inflate(R.menu.cart_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener(paramInt));
        popupMenu.show();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void onBindViewHolder(final MyViewHolder paramMyViewHolder, int paramInt) {
        Product localProduct = data.get(paramInt);
        paramMyViewHolder.itemName.setText(localProduct.getName());
        paramMyViewHolder.itemQuantity.setText( localProduct.getQuantity() );
        paramMyViewHolder.itemPrice.setText(String.format("Ksh %s", String.format(Locale.getDefault(), "%.2f", localProduct.getTotal())));
        Glide.with(context).load(localProduct.getItemImage())
                .into(paramMyViewHolder.itemIMage);
        paramMyViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView){
                showPopupMenu(paramMyViewHolder.overflow, paramMyViewHolder.getAdapterPosition());
            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
        View view=layoutInflater.inflate(R.layout.products_item, paramViewGroup, false);
        return new MyViewHolder(view);
}

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener
    {
        int position;

        public MyMenuItemClickListener(int paramInt) {
            position = paramInt;
        }

        public boolean onMenuItemClick(MenuItem paramMenuItem) {
            switch (paramMenuItem.getItemId()) {
                case R.id.action_delete:
                    int i = data.get(position).getItemCode();
                    MainActivity localMainActivity = (MainActivity)context;
                    localMainActivity.startConfirmDelete(i,false);
                    return true;
                case R.id.action_edit:
                    int j= data.get(position).getItemCode();
                    int price= data.get(position).getPrice();
                    String quanity= data.get(position).getQuantity();
                    int q=Integer.parseInt(quanity);
                    MainActivity mainActivity=(MainActivity)context;
                    mainActivity.startItemQuantity(j,price,q);
                    return true;
                default:
                    return false;
            }

        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemDescription;
        public ImageView itemIMage;
        public TextView itemName;
        public TextView itemPrice;
        public TextView itemQuantity;
        public ImageView overflow;
        public MyViewHolder(View paramView) {
            super(paramView);
            itemName = paramView.findViewById(R.id.itemName);
            itemDescription = paramView.findViewById(R.id.itemDescription);
            itemQuantity = paramView.findViewById(R.id.itemQuantity);
            itemPrice = paramView.findViewById(R.id.itemPPrice);
            itemIMage = paramView.findViewById(R.id.itemImage);
            overflow = paramView.findViewById(R.id.moreOperation);
        }
    }
}
