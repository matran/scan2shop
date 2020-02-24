package com.alienware.scan2shop.activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.adapters.ListAdapter;
import com.alienware.scan2shop.data.MyListData;
import com.alienware.scan2shop.dialogs.MyListDialog;
import com.alienware.scan2shop.helpers.MyListHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class MyListActivity extends AppCompatActivity {
    private static String TAG = "mylistfragment";
    private static int code = 0;
    private RecyclerView MlistView;
    private boolean VisibleToUser = false;
    private EditText listEdit;
    private List<MyListData> myList = new ArrayList();
    private MyListHelper myListHelper;
    private String myListName;
    private String myListPrice;
    private String myListQuantity;
    private TextView myListTotal;
    private Toolbar toolbar;
    private RecyclerView mylistrecycleview;
    private ListAdapter listAdapter;
    private ImageView voiceinput;
    private Context context;
    private TextView quanText;
    private final int REQ_CODE_SPEECH_INPUT=1;
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.my_list_layout);
        context=MyListActivity.this;
        myListHelper = new MyListHelper(context);
        toolbar= findViewById(R.id.toolbar);
        quanText= findViewById(R.id.mylistQun);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        code = myListHelper.getMyListCode();
        listEdit = findViewById(R.id.addItemEdit);
        MlistView = findViewById(R.id.myListPView);
        voiceinput= findViewById(R.id.voiceInput);
        MlistView.setLayoutManager(new LinearLayoutManager(context));
        myListTotal = findViewById(R.id.mylisttotalTextView);
        myList = myListHelper.getMylistProductInfo();
        listAdapter=new ListAdapter(context,myList);
        MlistView.setAdapter(listAdapter);
        updateTotal();
        voiceinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });
        listEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((actionId & EditorInfo.IME_MASK_ACTION) !=0){

                    perform();
                    return true;
                }else {

                    return false;
                }
            }
        });
    }
    private void clearAllList() {
        myListHelper.deleteMylistAllItems();
        myList.clear();
        listAdapter.notifyDataSetChanged();
        updateTotal();
    }

    public void perform(){
        myListName= listEdit.getText().toString().trim();
        if(!myListName.isEmpty()) {
            code = code + 1;
            myList.add(new MyListData(code, myListName, "", "1","0.0"));
            myListHelper.addMyList(code, myListName, "", "1", "0");
            listAdapter = new ListAdapter(context, myList);
            MlistView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            updateTotal();
            listEdit.setText("");
        }
        ((InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(listEdit.getWindowToken(), 2);

    }
    public void performFromSpeech(String message){
        if(!message.isEmpty()) {
            code = code + 1;
            myList.add(new MyListData(code, message, "", "1","0.0"));
            myListHelper.addMyList(code, message, "", "1", "0");
            listAdapter = new ListAdapter(context, myList);
            MlistView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            updateTotal();
            listEdit.setText("");
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mylist_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem){
        switch (paramMenuItem.getItemId()){
            case R.id.clearMylist:
                clearAllList();
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }
    public void updateListView(String name, String price, String quantity,int position1,int position2){
        myListPrice = price;
        myListQuantity = quantity;
        myListName = name;
        myListHelper.deleteMyListItem(position1);
        myList.remove(position2);
        int total = Integer.parseInt(price);
       int quan = Integer.parseInt(quantity);
       int subtotal=total *quan;
        code += 1;
        BigDecimal bigDecimal=new BigDecimal(total);
        this.myListHelper.addMyList(code, myListName,String.valueOf(total), String.valueOf(quan), String.valueOf(subtotal));
        myList.add(new MyListData(code,myListName,"ksh." + total,"(" + quan +")",String.valueOf(subtotal)));
        listAdapter=new ListAdapter(context,myList);
        MlistView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        updateTotal();
        listEdit.setText("");
    }
    public void updateTotal(){
        Object localObject = myListHelper.getMyListTotal();
        int qun=myListHelper.getMylistQuantity();
        localObject = String.format(Locale.getDefault(), "%.2f", localObject);
        quanText.setText(String.valueOf(qun));
        myListTotal.setText("ksh." + localObject);
    }
    private void askSpeechInput(){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try{

            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            e.getMessage();
        }
    }


       public void startMyListDialog(String paramString1, String paramString2, String paramString3,int position,int position2,String total){
           MyListDialog myListDialog=new MyListDialog();
           myListDialog.setData(paramString1, paramString2, paramString3,position,position2,total);
           myListDialog.show(getSupportFragmentManager(), "mylistdialog");
        }
    public void deleteData(int position,int position2) {
            myListHelper.deleteMyListItem(position);
            myList.remove(position2);
        listAdapter.notifyDataSetChanged();
        updateTotal();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT : {
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String  inputmessage=result.get(0);
                performFromSpeech(inputmessage.trim());
                }
                break;
            }
        }
    }
}
