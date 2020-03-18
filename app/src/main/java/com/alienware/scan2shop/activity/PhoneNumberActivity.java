package com.alienware.scan2shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONObject;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
public class PhoneNumberActivity  extends AppCompatActivity {
    private TextInputEditText etPhoneNumber;
    private TextInputLayout tilPhoneNumber;
    private Button continueButton;
    private String phoneno;
    private View phoneProgressBar;
    private Toolbar toolbar;
    private boolean errorflag=true;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_layout);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        tilPhoneNumber=findViewById(R.id.tilPhoneNumber);
        phoneProgressBar=findViewById(R.id.phoneProgressBar);
        continueButton=findViewById(R.id.continueButton);
        etPhoneNumber.addTextChangedListener(new MyTextWatcher(etPhoneNumber));
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno= Objects.requireNonNull(etPhoneNumber.getText()).toString().trim();
                if(!phoneno.equals("") && !errorflag){
                  sendPhoneToserver(phoneno);
                }
            }
        });
    }
    private void sendPhoneToserver(final String phoneno){
        hideSoftInput();
        phoneProgressBar.setVisibility(View.VISIBLE);
        String url= Config.URL_PHONE+phoneno;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                phoneProgressBar.setVisibility(View.INVISIBLE);
                try {

                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")) {
                    Intent intent=new Intent(PhoneNumberActivity.this,VerifyCodeActivity.class);
                      intent.putExtra("phone",phoneno);
                       startActivity(intent);
                    }
                } catch (Exception e) {
                    phoneProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                phoneProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Scan2ShopApplication.getInstance().addToRequestQueue(jsonObjReq,"phone_number_verify");
    }


    @Override
    public void onPause() {
        super.onPause();
        Scan2ShopApplication.getInstance().cancelPendingRequests("phone_number_verify");
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View paramView)
        {
            this.view = paramView;
        }
        public void afterTextChanged(Editable paramEditable)
        {
            if (this.view.getId() == R.id.etPhoneNumber) {
                validatePhoneNumber();
                return;
            }
        }
        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }

     private boolean validatePhoneNumber(){
         String phonenumber = etPhoneNumber.getText().toString().trim();
         if (phonenumber.isEmpty()) {
             this.errorflag = true;
             tilPhoneNumber.setError("phone number is required");
             requestFocus(this.etPhoneNumber);
             return false;
         }else if(phonenumber.length()<9){
             this.errorflag = true;
             tilPhoneNumber.setError("phone number must have 9 characters");
             requestFocus(this.etPhoneNumber);
             return false;
         }
         this.errorflag = false;
         tilPhoneNumber.setErrorEnabled(false);
         return true;
     }

    private void requestFocus(View paramView)
    {
        if (paramView.requestFocus()) {
            getWindow().setSoftInputMode(5);
        }
    }
    private void hideSoftInput(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
