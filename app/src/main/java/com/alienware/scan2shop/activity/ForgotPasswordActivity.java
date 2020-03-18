package com.alienware.scan2shop.activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
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
public class ForgotPasswordActivity extends AppCompatActivity {
    private View fpProgressBar;
    private Button fpSendButton;
    private TextInputEditText fpEditTextEmail;
    private TextInputLayout tilEmailAddress;
    private boolean errorflag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);
        Toolbar toolbar = findViewById(R.id.toolbarFP);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fpProgressBar=findViewById(R.id.fpProgressBar);
        fpEditTextEmail=findViewById(R.id.fpEditEditTextEmail);
        tilEmailAddress=findViewById(R.id.tilFEmail);
        fpSendButton=findViewById(R.id.fpsendButtonEmail);
        fpEditTextEmail.addTextChangedListener(new MyTextWatcher(fpEditTextEmail));
        fpSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=fpEditTextEmail.getText().toString().trim();
                if(!email.equals("") && !errorflag){
                    fpEditTextEmail.setText("");
                    sendChangePasswordRequest(email);
                }else{
                    Toast.makeText(getApplicationContext(),"Empty field Email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void sendChangePasswordRequest(String email){
        hideSoftInput();
        fpProgressBar.setVisibility(View.VISIBLE);
        String url= Config.URL_PASSWORD_CHANGE+email;
        StringRequest str = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fpProgressBar.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    String status=object.getString("status");
                    if(status.equals("success")){
                        Toast.makeText(getApplicationContext(),"Password reset link is sent to your email",Toast.LENGTH_LONG).show();
                    }else if(status.equals("fail")){
                        Toast.makeText(getApplicationContext(),"Failed to send password reset link",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Failed to send password reset link",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    fpProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fpProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }

        })
        {
        };
        str.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Scan2ShopApplication.getInstance().addToRequestQueue(str);
    }

    private void hideSoftInput(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private static boolean isValidEmail(String paramString){
        return (!TextUtils.isEmpty(paramString)) && (Patterns.EMAIL_ADDRESS.matcher(paramString).matches());
    }
    private boolean validateEmail()
    {
        String str = fpEditTextEmail.getText().toString().trim();
        if ((str.isEmpty()) || (!isValidEmail(str)))
        {
            errorflag = true;
            tilEmailAddress.setError("invalid email address");
            requestFocus(fpEditTextEmail);
            return false;
        }
          errorflag = false;
         tilEmailAddress.setErrorEnabled(false);
         return true;
    }
    private void requestFocus(View paramView){
        if (paramView.requestFocus()) {
            getWindow().setSoftInputMode(5);
        }
    }
    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View paramView){
            this.view = paramView;
        }
        public void afterTextChanged(Editable paramEditable){
            if (this.view.getId() == R.id.fpEditEditTextEmail) {
                validateEmail();
                return;
            }
        }
        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }

}
