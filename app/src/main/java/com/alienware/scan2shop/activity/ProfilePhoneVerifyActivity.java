package com.alienware.scan2shop.activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.helpers.SmsBroadcastReceiver;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfilePhoneVerifyActivity extends AppCompatActivity {
    private OtpView verifyNumberInput;
    private Button ctButton;
    private Button resendButton;
    private View verifyProgressBar;
    private Toolbar toolbar;
    private UserSqliteHandler userDB;
    private HashMap<String, String> user;
    private TextView verifyCodeTextView;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private String phoneno=null;
    private TextView progressMessageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_code_layout);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userDB=new UserSqliteHandler(getApplicationContext());
        user = userDB.getUserDetails();
        verifyNumberInput=findViewById(R.id.otp_view);
        progressMessageView=findViewById(R.id.pbText);
        verifyProgressBar=findViewById(R.id.verifyProgressBar);
        ctButton=findViewById(R.id.ctButton);
        resendButton=findViewById(R.id.resendButton);
        verifyCodeTextView=findViewById(R.id.text2);
        startSmsUserConsent();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneno=null;
            } else {
                phoneno=extras.getString("phone");
            }
        } else {
            phoneno = (String) savedInstanceState.getSerializable("phone");
        }
        ctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token=verifyNumberInput.getText().toString().trim();
                if(!token.equals("")){
                    confirmToken(phoneno,token);
                }
            }
        });
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refetchToken(phoneno);
            }
        });
        verifyNumberInput.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {
                Log.d("onOtpCompleted=>", otp);
            }
        });
    }
    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String token=matcher.group(0);
            verifyNumberInput.setText(token);
            confirmToken(phoneno,token);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                getOtpFromMessage(message);
            }
        }
    }
    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
    private void confirmToken(final String phoneno, String token){
        verifyProgressBar.setVisibility(View.VISIBLE);
        progressMessageView.setText("Verifying code..");
        final String URL = Config.URL_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phone", phoneno);
        params.put("token", token);
        JsonObjectRequest str = new JsonObjectRequest(URL,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                verifyProgressBar.setVisibility(View.INVISIBLE);
                try{
                    String status=response.getString("status");
                    if (status.equals("success")){
                        Toast.makeText(getApplicationContext(),"Successfully verified phone number",Toast.LENGTH_LONG).show();
                        updateAccount();
                    }else if(status.equals("fail")) {
                        Toast.makeText(getApplicationContext(),"Fail to confirm verify phone number",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Fail to verify phone number",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    verifyProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError e) {
                verifyProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        str.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "confirm_token");
    }

    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("confirm_token");
        Scan2ShopApplication.getInstance().cancelPendingRequests("resend_request");
    }
    private void refetchToken(String phone){
        hideSoftInput();
        verifyProgressBar.setVisibility(View.VISIBLE);
        progressMessageView.setText("Resending code...");
        String url= Config.URL_PHONE+phone;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                verifyProgressBar.setVisibility(View.INVISIBLE);
                try {

                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                        Toast.makeText(getApplicationContext(),"Resend successful",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    verifyProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                verifyProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"resend_request");

    }
    private void hideSoftInput(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ProfilePhoneVerifyActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void updateAccount() {
        verifyProgressBar.setVisibility(View.VISIBLE);
        progressMessageView.setText("Updating phone number...");
        final String url = Config.URL_REGISTER;
        HashMap<String, String> params = new HashMap<>();
        final String firstname=user.get("firstname");
        final String lastname=user.get("secondname");
        final String email=user.get("email");
        final String phone="254"+phoneno;
        params.put("id",userDB.getUserid());
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phone", phone);
        params.put("email", email);
        JsonObjectRequest str = new JsonObjectRequest(Request.Method.PUT,url,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                verifyProgressBar.setVisibility(View.INVISIBLE);
                try{
                    String status=response.getString("status");
                    if (status.equals("success")){
                        userDB.updateUser(firstname,lastname,phone, email,"null");
                        Toast.makeText(ProfilePhoneVerifyActivity.this, "Details updated successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ProfilePhoneVerifyActivity.this, "Failed to update try again", Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    verifyProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ProfilePhoneVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError e){
                verifyProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfilePhoneVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        str.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0F));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "request_update");
    }

}
