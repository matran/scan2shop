package com.alienware.scan2shop.activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.ImageSliderAdapter;
import com.alienware.scan2shop.adapters.SliderAdapter;
import com.alienware.scan2shop.dialogs.PaymentDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class CartFragment extends Fragment {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private Button button;
    private com.alienware.scan2shop.helpers.ItemHelper helper;
    private com.alienware.scan2shop.adapters.ItemAdapter itemAdapter;
    private Context mContext;
    private List<com.alienware.scan2shop.data.Product> productList;
    private RecyclerView recyclerView;
    private LinearLayout showEmptyCart;
    private ImageView swipeleft;
    private ImageView swiperight;
    private Button scanButton;
    private LinearLayout bottomBar;
    private boolean VisibleToUser =false;
    static final int REQUEST_BARCODE_PICKER_ACTIVITY = 9;
    static final int RESULT_CODE=5;
    private static final String TAG="cartfragment";
    private  TextView textView;
    private SliderView sliderView;
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = paramLayoutInflater.inflate(R.layout.cart_layout, paramViewGroup, false);
        mContext = getContext();
        helper = new com.alienware.scan2shop.helpers.ItemHelper(mContext);
        recyclerView = view.findViewById(R.id.item_recycler_view);
        sliderView=view.findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapter(mContext));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        recyclerView.setNestedScrollingEnabled(false);
        bottomBar= view.findViewById(R.id.bottomBAR);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        productList = new ArrayList();
        itemAdapter = new com.alienware.scan2shop.adapters.ItemAdapter(this.mContext, this.productList);
        recyclerView.setAdapter(itemAdapter);
        scanButton= view.findViewById(R.id.scanItem);
         scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startScanning();
            }
        });
        if (helper.getProductInfo() != null){
            bottomBar.setVisibility(View.VISIBLE);
            productList = helper.getProductInfo();
            itemAdapter = new com.alienware.scan2shop.adapters.ItemAdapter(mContext, productList);
            recyclerView.setAdapter(itemAdapter);
        }else {
            bottomBar.setVisibility(View.INVISIBLE);
        }

        textView = view.findViewById(R.id.totalTextView);
        button = view.findViewById(R.id.checkButton);
        showEmptyCart = view.findViewById(R.id.cartContent);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                PaymentDialog paymentDialog=new PaymentDialog();
                if (getFragmentManager() != null) {
                    paymentDialog.show(getFragmentManager(),"paymentdialog");
                }
            }
        });
        updateList();
        return view;
    }
    private void startScanning(){
        Intent intent=new Intent(getActivity(),ScanActivity.class);
        startActivityForResult(intent, REQUEST_BARCODE_PICKER_ACTIVITY);
    }

    private void clearCart(){
        this.helper.deleteAllItems();
        this.productList.clear();
        this.itemAdapter.notifyDataSetChanged();
        showStatus();
        this.textView.setText("ksh:" + "0.00");
    }

    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        setRetainInstance(true);
    }

    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
        paramMenuInflater.inflate(R.menu.cart_menu, paramMenu);
        super.onCreateOptionsMenu(paramMenu, paramMenuInflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE_PICKER_ACTIVITY) {
              if(resultCode==RESULT_CODE) {
                  String message = "";
                  message = data.getStringExtra("barcodeData");
                  if (!message.isEmpty()) {
                      com.alienware.scan2shop.dialogs.ItemQuantityDialog dialog = new com.alienware.scan2shop.dialogs.ItemQuantityDialog();
                      dialog.setStatus(false);
                      dialog.setBarcode(message);
                      dialog.show(getFragmentManager(), "quantity");
                  }
              }
        }
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
        switch (paramMenuItem.getItemId()) {
            case R.id.clearCart:
                ((com.alienware.scan2shop.activity.MainActivity)getActivity()).startConfirmDelete(8,true);
            break;
        }
            return super.onOptionsItemSelected(paramMenuItem);
    }

    public void onResume()
    {
        super.onResume();
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void showStatus() {
        if (helper.getDbTotal() != 0)
        {
            showEmptyCart.setVisibility(View.INVISIBLE);
            bottomBar.setVisibility(View.VISIBLE);

        }else {
            showEmptyCart.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.INVISIBLE);
        }
    }

    public void updateList(){
        productList = this.helper.getProductInfo();
        itemAdapter = new com.alienware.scan2shop.adapters.ItemAdapter(mContext, productList);
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        int tot = this.helper.getDbTotal();
        textView.setText("ksh:" + tot+".00");
        showStatus();
    }

}
