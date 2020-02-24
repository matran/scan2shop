package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
public class ForgotPasswordActivity extends AppCompatActivity {
    private View fpProgressBar;
    private Button fpSendButton;
    private EditText fpEditTextEmail;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);
        toolbar = findViewById(R.id.toolbarFP);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fpProgressBar=findViewById(R.id.fpProgressBar);
        fpEditTextEmail=findViewById(R.id.fpEditEditTextEmail);
        fpSendButton=findViewById(R.id.fpsendButtonEmail);
        fpSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=fpEditTextEmail.getText().toString().trim();
                if(!email.equals("")){
                    fpEditTextEmail.setText("");
                    sendChangePasswordRequest(email);
                }else{
                    Toast.makeText(getApplicationContext(),"Empty field Email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void sendChangePasswordRequest(String email){
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
}
