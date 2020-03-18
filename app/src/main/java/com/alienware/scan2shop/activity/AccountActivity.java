package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;
/**
 * Created by  Henry cheruiyot on 2/4/2018.
 */
public class AccountActivity extends AppCompatActivity {
    private static String TAG = "account";
    private String afterTextEmail;
    private String afterTextFirstName;
    private String afterTextLastName;
    private String afterTextPhone;
    private String beforeTextEmail;
    private String beforeTextFirstName;
    private String beforeTextLastName;
    private String beforeTextPhone;
    private EditText editEmail;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhone;
    private Button saveButton;
    private Toolbar toolbar;
    private View accountProgressBar;
    String uid;
    private HashMap<String, String> user;
    private UserSqliteHandler userDB;
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.account_layout);
        toolbar = findViewById(R.id.toolbarAc);
        setSupportActionBar(this.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setEnabled(false);
        editFirstName = findViewById(R.id.acFirstName);
        editLastName=findViewById(R.id.aclastName);
        editPhone = findViewById(R.id.accountPhone);
        editEmail = findViewById(R.id.acEmail);
        accountProgressBar=findViewById(R.id.accountProgressBar);
    }
    public void onResume(){
        super.onResume();
        userDB = new UserSqliteHandler(getApplicationContext());
        user = userDB.getUserDetails();
        uid = userDB.getuid();
        editFirstName.setText(user.get("firstname"));
        editLastName.setText(user.get("secondname"));
        editEmail.setText(user.get("email"));
        editPhone.setText(user.get("phone"));
        editLastName.addTextChangedListener(new MyTextWatcher(editLastName));
        editFirstName.addTextChangedListener(new MyTextWatcher(editFirstName));
        editEmail.addTextChangedListener(new MyTextWatcher(editEmail));
        editPhone.addTextChangedListener(new MyTextWatcher(editPhone));
        beforeTextChangedValues();
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                beforeTextFirstName=editFirstName.getText().toString().trim();
                beforeTextLastName=editLastName.getText().toString().trim();
                beforeTextPhone =editPhone.getText().toString().trim();
                beforeTextEmail=editEmail.getText().toString().trim();
                if(!beforeTextFirstName.equals("")&&!beforeTextLastName.equals("")&&!beforeTextPhone.equals("") &&!beforeTextEmail.equals("")){
                    updateAccount(beforeTextFirstName,beforeTextLastName,beforeTextPhone,beforeTextEmail);
                }else{
                    Toast.makeText(getApplicationContext(),"Some input fields are empty",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View paramView) {
            this.view = paramView;
        }
        public void afterTextChanged(Editable paramEditable) {
            switch (this.view.getId()){
                case R.id.acFirstName:
                    getTextFirstName();
                    return;
                case R.id.aclastName:
                    getTextLastName();
                    return;
                case R.id.acEmail:
                    getTextEmail();
                    return;
                case R.id.accountPhone:
                    getTextPhone();
                default:
            }

        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }
    private void beforeTextChangedValues(){
        beforeTextFirstName = editFirstName.getText().toString().trim();
        beforeTextLastName = editLastName.getText().toString().trim();
        beforeTextPhone = editPhone.getText().toString().trim();
        beforeTextEmail = editEmail.getText().toString().trim();
    }

    private void changeButtonBackground()
    {
        this.saveButton.setEnabled(true);
        this.saveButton.setTextColor(-1);
        this.saveButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.bg_after_change_button));
    }

    private void getTextEmail()
    {
        afterTextEmail = editEmail.getText().toString().trim();
        if ((beforeTextEmail.equals(afterTextEmail)) || (afterTextEmail.isEmpty()))
        {
            revertButtonBackground();
        }else{
            changeButtonBackground();
        }

    }
    private void getTextFirstName(){
        afterTextFirstName = editFirstName.getText().toString().trim();
        if ((beforeTextFirstName.equals(afterTextFirstName)) || (afterTextFirstName.isEmpty()))
        {
            revertButtonBackground();
        }else{
            changeButtonBackground();
        }

    }

    private void getTextLastName(){
        afterTextLastName = editLastName.getText().toString().trim();
        if ((beforeTextLastName.equals(afterTextLastName)) || (afterTextLastName.isEmpty())){
            revertButtonBackground();
        }else{
            changeButtonBackground();
        }

    }
    private void getTextPhone(){
        afterTextPhone = editPhone.getText().toString().trim();
        if ((beforeTextPhone.equals(afterTextPhone)) || (afterTextPhone.isEmpty())){
            revertButtonBackground();
        }else{
            changeButtonBackground();
        }

    }
    private void revertButtonBackground(){
        saveButton.setEnabled(false);
        saveButton.setTextColor(-7829368);
        saveButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.bg_save_button));
    }
    private void updateAccount(final String firstname,final String lastname,final String phone,final String email) {
       accountProgressBar.setVisibility(View.VISIBLE);
        final String url = Config.URL_REGISTER;
        HashMap<String, String> params = new HashMap<>();
        params.put("id",userDB.getUserid());
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phone", phone);
        params.put("email", email);
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                accountProgressBar.setVisibility(View.INVISIBLE);
                try{
                    String status=response.getString("status");
                    if (status.equals("success")){
                        userDB.updateUser(firstname,lastname,phone, email,"null");
                        Toast.makeText(AccountActivity.this, "Details updated successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(AccountActivity.this, "Failed to update try again", Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    accountProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
    {
        public void onErrorResponse(VolleyError e)
        {
            accountProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    })
        {
        };
        str.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0F));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_update");
    }

    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("req_update");

    }

}
