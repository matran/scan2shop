package com.alienware.scan2shop.activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.data.Receipt;
import com.alienware.scan2shop.dialogs.ConfirmDialog;
import com.alienware.scan2shop.dialogs.ItemQuantityDialog;
import com.alienware.scan2shop.helpers.FirebaseRegManager;
import com.alienware.scan2shop.helpers.ItemHelper;
import com.alienware.scan2shop.helpers.NotificationUtils;
import com.alienware.scan2shop.helpers.ReceiptHelper;
import com.alienware.scan2shop.helpers.SessionManager;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    public static int ItemIndex=0;
    private static final String TAG_HOME="home";
    private static final  String TAG_HISTORY="history";
    private  static  final String TAG_HELP="help";
    private static final String TAG_FEEDBACK="feedback";
    private static final String TAG_ABOUT="about";
    private static String CURRENT_TAG=TAG_HOME;
    private String[]activityTitles;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Handler mHandler;
    private NavigationView navigationView;
    private UserSqliteHandler db;
    private SessionManager session;
    private FragmentManager fragmentManager;
    private ItemHelper helper;
    private ProgressDialog dialog;
    private static int total;
    private static int code = 1;
    ReceiptHelper receiptHelper;
    private List<Receipt> receiptItems;
    private Button button;
    private FirebaseRegManager firebaseRegManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_loader);
        receiptHelper=new ReceiptHelper(getApplicationContext());
        receiptItems = new ArrayList<>();
        firebaseRegManager=new FirebaseRegManager(getApplicationContext());
        db=new UserSqliteHandler(getApplicationContext());
        session=new SessionManager(getApplicationContext());
        helper=new ItemHelper(getApplicationContext());
        code = helper.getProductCode();
        dialog = new ProgressDialog(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer) ;
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ItemIndex=0;
        mHandler=new Handler();
        activityTitles=getResources().getStringArray(R.array.nav_items_titles);
        navigationView = findViewById(R.id.nav_view);
        setUpNavigationView();
        if(savedInstanceState==null){
            ItemIndex=0;
            CURRENT_TAG=TAG;
        }
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.nav);
        fragmentManager = getSupportFragmentManager();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (id){
                    case R.id.navigation_cart:
                        fragment = new CartFragment();
                        transaction.replace(R.id.main_container, fragment,"cart").commit();
                        break;
                    case R.id.navigation_discover:
                        fragment = new LoyaltyFragment();
                        transaction.replace(R.id.main_container, fragment,"loyalty").commit();
                        break;
                    case R.id.navigation_receipts:
                        fragment = new ReceiptFragment();
                        transaction.replace(R.id.main_container, fragment,"receipt").commit();
                        break;
                }
                return true;
            }
        });
        loadDefaultFragment();
        mRegistrationBroadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                  //  Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        getRegistrationToken();
        submitReg();
        View header = navigationView.getHeaderView(0);
        CircularImageView profileImage=header.findViewById(R.id.profileImageNavHeader);
        TextView fullNameTextView =header.findViewById(R.id.fullNameNavheader);
        TextView emailAdressTextView=header.findViewById(R.id.emailAddressNavHeader);
        HashMap<String, String> user=db.getUserDetails();
        fullNameTextView.setText(user.get("firstname")+" "+ user.get("secondname"));
        emailAdressTextView.setText(user.get("email"));
    }
    public void loadDefaultFragment(){
        fragment=new CartFragment();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container,fragment,"cart").commit();
    }
    private void setUpNavigationView() {
        this.navigationView.getMenu().getItem(ItemIndex).setChecked(true);
        this.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        ItemIndex = 0;
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_profile:
                        ItemIndex = 1;
                        startActivity(new Intent(MainActivity.this, AccountActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_history:
                        ItemIndex = 2;
                        startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_help:
                       ItemIndex = 3;
                       startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_feedback:
                      ItemIndex = 4;
                      startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_about:
                        ItemIndex = 5;
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        ItemIndex = 6;
                        logoutUser();
                        drawer.closeDrawers();
                        return true;
                    default:
                        ItemIndex=0;
                }

                return true;
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.this.finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.action_myList:
                startActivity(new Intent(MainActivity.this, MyListActivity.class));
                break;
        }

        return super.onOptionsItemSelected(paramMenuItem);
    }
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    public void fetchProduct(final String barcode, final int quantity) {
        this.dialog.setMessage("Getting Item Info..");
        String url=Config.URL_ITEM+ barcode;
        showDialog();
        StringRequest local7 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                hideDialog();
                try
                {
                    JSONObject object = new JSONObject(response);
                    String status=object.getString("status");
                    if (status.equals("success")) {
                        JSONObject product = object.getJSONObject("item");
                        String name = product.getString("name");
                        String description = product.getString("description");
                        int price = product.getInt("price");
                        String image=product.getString("image");
                        code=code + 1;
                        int total2=price * quantity;
                        helper.addProduct(code, name,description, price, quantity, total2,image);
                        total=helper.getDbTotal();
                        CartFragment  cartFragment=(CartFragment)getSupportFragmentManager().findFragmentByTag("cart");
                        if (cartFragment != null) {
                            cartFragment.updateList();
                            Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        String message = object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError e) {
                hideDialog();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                //showDialogBuilder();
            }
        })

        {
            @Override
            public Map<String, String> getHeaders(){
                String token=db.getuid();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        local7.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0F));
        Scan2ShopApplication.getInstance().addToRequestQueue(local7, "req_product");
    }


    private void showDialog() {
        if (!this.dialog.isShowing()) {
            this.dialog.show();
        }
    }
    private void hideDialog() {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    private void showDialogBuilder() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage("Connectivity error.please try again.").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.cancel();
            }
        });
        localBuilder.create().show();
    }
    public void refreshFragment() {
        CartFragment  cartFragment=(CartFragment)getSupportFragmentManager().findFragmentByTag("cart");
        cartFragment.updateList();

    }
    public void startConfirmDelete(int i,boolean flag){
        ConfirmDialog confirmDialog=new ConfirmDialog();
        confirmDialog.setPosition(i);
        confirmDialog.setFlag(flag);
        confirmDialog.show(getSupportFragmentManager(),"confirmdelete");
    }
    public void startItemQuantity(int i,int price,int quantity){
        ItemQuantityDialog itemQuantityDialog=new ItemQuantityDialog();
        itemQuantityDialog.setStatus(true);
        itemQuantityDialog.setData(i,price,quantity);
        itemQuantityDialog.show(getSupportFragmentManager(),"itemquantity");
    }

    public void startQuantityDialog(String barcode){
        ItemQuantityDialog  itemQuantityDialog=new ItemQuantityDialog();
        itemQuantityDialog.setBarcode(barcode);
        itemQuantityDialog.show(getSupportFragmentManager(),"itemquanity");

    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        Scan2ShopApplication.getInstance().cancelPendingRequests("req_product");
        Scan2ShopApplication.getInstance().cancelPendingRequests("req_firebase_reg");
    }
    private void getRegistrationToken(){
        if(firebaseRegManager.getReg()==null){
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    firebaseRegManager.setReg(token);
                }
            });
        }
    }

    private void submitReg(){
        if(firebaseRegManager.getReg()!=null &&db.getUserid()!=null && !firebaseRegManager.isSent()){
            String token=firebaseRegManager.getReg();
           sendRegistrationToServer(token);
        }
    }
    private void sendRegistrationToServer(final String token){
        final String userid=db.getUserid();
        final String url = Config.URL_FIREBASE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("deviceid", token);
        JsonObjectRequest str = new JsonObjectRequest(url,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status=response.getString("status");
                    if (status.equals("success")) {
                        firebaseRegManager.setSent(true);
                        Toast.makeText(getApplicationContext(),"Token sent",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"not stored",Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"jhsjs",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError paramAnonymousVolleyError) {
               // Toast.makeText(getApplicationContext(),"network error",Toast.LENGTH_LONG).show();
            }
        });
        new DefaultRetryPolicy(0, 1, 1.0F);
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_firebase_reg");
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

}
