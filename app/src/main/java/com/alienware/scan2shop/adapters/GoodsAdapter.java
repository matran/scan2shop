package com.alienware.scan2shop.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.data.Goods;
import com.alienware.scan2shop.helpers.MyListHelper;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MyViewHolder> {
    private Context context;
    private List<Goods> data;
    private LayoutInflater layoutInflater;
    private MyListHelper myListHelper;
    private static int code;
    ImageLoader imageLoader = Scan2ShopApplication.getInstance().getImageLoader();
    public GoodsAdapter(Context paramContext, List<Goods> paramList) {
        this.context = paramContext;
        this.layoutInflater = LayoutInflater.from(paramContext);
        this.data = paramList;
        myListHelper=new MyListHelper(paramContext);

    }
    private void showPopupMenu(View paramView, int paramInt) {
        PopupMenu popupMenu = new PopupMenu(this.context, paramView);
        popupMenu.getMenuInflater().inflate(R.menu.goods_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new GoodsAdapter.MyMenuItemClickListener(paramInt));
        popupMenu.show();
    }
    public int getItemCount() {
        return data.size();
    }
    public void onBindViewHolder(final MyViewHolder paramMyViewHolder, int paramInt) {
        if (imageLoader == null)
            imageLoader = Scan2ShopApplication.getInstance().getImageLoader();
        final Goods goods = data.get(paramInt);
        paramMyViewHolder.itemName.setText(goods.getName());
        paramMyViewHolder.itemDescription.setText(goods.getDescription());
        paramMyViewHolder.itemIMage.setImageUrl(goods.getItemImage(), imageLoader);
        paramMyViewHolder.itemPrice.setText("price: ksh." +goods.getPrice());
        paramMyViewHolder.ratingBar.setRating(3);
        final String price=goods.getPrice().replace(".00","");
        paramMyViewHolder.addToMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListHelper.addMyList(myListHelper.getMyListCode()+1, goods.getName(),price, "1", price);
                Toast.makeText(context,"Added to my list",Toast.LENGTH_LONG).show();
            }
        });
        paramMyViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                showPopupMenu(paramMyViewHolder.overflow, paramMyViewHolder.getAdapterPosition());
            }
        });
    }

    public GoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        View view=layoutInflater.inflate(R.layout.goods_items, paramViewGroup, false);
        return new GoodsAdapter.MyViewHolder(view);
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;
        public MyMenuItemClickListener(int paramInt) {
            position = paramInt;
        }
        public boolean onMenuItemClick(MenuItem paramMenuItem) {
            switch (paramMenuItem.getItemId()) {

                case R.id.moreGoods:

                    return true;
                default:
                    return false;
            }
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemDescription;
        public NetworkImageView itemIMage;
        public TextView itemName;
        public TextView itemPrice;
        public TextView itemQuantity;
        public ImageView overflow;
        public Button addToMyList;
        public RatingBar ratingBar;
        public MyViewHolder(View paramView){
            super(paramView);
            itemName = paramView.findViewById(R.id.itemName);
            itemDescription = paramView.findViewById(R.id.itemDescription);
            itemPrice = paramView.findViewById(R.id.itemPPrice);
            itemIMage = paramView.findViewById(R.id.itemImage);
            addToMyList= paramView.findViewById(R.id.addToMyList);
            overflow = paramView.findViewById(R.id.moreOperation);
            ratingBar= paramView.findViewById(R.id.goods_rating_bar);
        }
    }
}
