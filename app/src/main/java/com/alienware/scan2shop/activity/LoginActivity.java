package com.alienware.scan2shop.activity;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.helpers.SessionManager;
import com.alienware.scan2shop.helpers.StartUpManager;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private String Email;
    private String Password;
    private View ProgressOverlay;
    private Button btnLinkToRegister;
    private Button btnLogin;
    private LinearLayout contentLayout;
    private UserSqliteHandler db;
    private TextView errorTextView;
    private boolean errorflag = false;
    private Button forgotPassword;
    private AlphaAnimation inAnimation;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressBar progressBar;
    private Button retryButton;
    private SessionManager session;
    private StartUpManager startupManager;
    private TextInputLayout textEmail;
    private TextInputLayout textPassword;
    private Toolbar toolbar;
    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        setContentView(R.layout.login_layout);
        toolbar = findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getSupportActionBar().setHomeButtonEnabled(true);
        forgotPassword = findViewById(R.id.linktoForgotPassword);
        contentLayout = findViewById(R.id.cotentLayout);
        textEmail = findViewById(R.id.tilEmailAddress);
        textPassword = findViewById(R.id.tilPassword);
        inputEmail = findViewById(R.id.etEmailAddress);
        inputEmail.setText(getPrimaryEmail());
        inputPassword = findViewById(R.id.etPassword);
        inputEmail.addTextChangedListener(new MyTextWatcher(this.inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(this.inputPassword));
        btnLogin = findViewById(R.id.loginButton);
        btnLinkToRegister = findViewById(R.id.linktoregister);
        ProgressOverlay = findViewById(R.id.progress_overlay);
        errorTextView = findViewById(R.id.errorTextView);
        retryButton = findViewById(R.id.retryButton);
        progressBar = findViewById(R.id.progressBar);
        db = new UserSqliteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        this.startupManager = new StartUpManager(this);
        if (session.isLoggedIn())
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        this.retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView){
                LoginActivity.this.progressBar.setVisibility(View.VISIBLE);
                LoginActivity.this.errorTextView.setText("");
                LoginActivity.this.retryButton.setVisibility(View.INVISIBLE);
                Email = inputEmail.getText().toString().trim();
                String str = LoginActivity.this.inputPassword.getText().toString().trim();
                if ((!Email.isEmpty()) && (!str.isEmpty()) && (!LoginActivity.this.errorflag)) {
                    LoginActivity.this.checkLogin(Email, str);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View paramAnonymousView){

                Email =inputEmail.getText().toString().trim();
                String password = LoginActivity.this.inputPassword.getText().toString().trim();
                if ((!Email.isEmpty()) && (!password.isEmpty()) && (!LoginActivity.this.errorflag)) {
                    checkLogin(Email, password);
                }
            }
        });
        btnLinkToRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView){
                Intent intent= new Intent(LoginActivity.this.getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View paramView)
        {
            this.view = paramView;
        }
        public void afterTextChanged(Editable paramEditable)
        {
            switch (this.view.getId()) {
                case R.id.tilPassword:
                    validatePassword();
                    return;
                case R.id.etEmailAddress:
                    validateEmail();
                    return;
                default:
                    break;

            }
        }
        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }
    private void checkLogin(final String email, final String password)
    {
        hideSoftInput();
        inAnimation = new AlphaAnimation(0.0F, 1.0F);
        inAnimation.setDuration(200L);
        ProgressOverlay.setAnimation(this.inAnimation);
        ProgressOverlay.setVisibility(View.VISIBLE);
        final String URL = Config.URL_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("privilege","user");
        JsonObjectRequest str = new JsonObjectRequest(URL,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                ProgressOverlay.setVisibility(View.INVISIBLE);
                try{
                    String status=response.getString("status");
                    if (status.equals("success")){
                        session.setLogin(true);
                        String apiKey = response.getString("token");
                        JSONObject object = response.getJSONObject("data");
                        String id=object.getString("id");
                        String firstname = object.getString("firstname");
                        String  secondname=object.getString("lastname");
                        String phone = object.getString("phone");
                        String email = object.getString("email");
                        db.addUser(id,apiKey,firstname,secondname,phone,email);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        startupManager.setFirstTimeLaunch(false);
                        LoginActivity.this.finish();
                    }else if(status.equals("fail")) {
                        LoginActivity.this.ProgressOverlay.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "invalid email or password", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "Unable to login try again later", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progressBar.setVisibility(View.INVISIBLE);
                    retryButton.setVisibility(View.VISIBLE);
                    errorTextView.setText(R.string.connection_error);
                    errorTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
        public void onErrorResponse(VolleyError e) {
            progressBar.setVisibility(View.GONE);
            retryButton.setVisibility(View.VISIBLE);
            errorTextView.setText(R.string.connection_error);
            errorTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    });
        str.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0F));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_login");
    }
    private String getPrimaryEmail()
    {
        Pattern localPattern = Patterns.EMAIL_ADDRESS;
        String str = "";
        Account[] arrayOfAccount = AccountManager.get(getApplicationContext()).getAccounts();
        int j = arrayOfAccount.length;
        int i = 0;
        while (i < j)
        {
            Account localAccount = arrayOfAccount[i];
            if (localPattern.matcher(localAccount.name).matches()) {
                str = localAccount.name;
            }
            i += 1;
        }
        return str;
    }

    private void hideSoftInput()
    {
        ((InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.ProgressOverlay.getWindowToken(), 2);
    }

    private static boolean isValidEmail(String paramString)
    {
        return (!TextUtils.isEmpty(paramString)) && (Patterns.EMAIL_ADDRESS.matcher(paramString).matches());
    }

    private void requestFocus(View paramView)
    {
        if (paramView.requestFocus()) {
            getWindow().setSoftInputMode(5);
        }
    }

    private boolean validateEmail()
    {
        String str = this.inputEmail.getText().toString().trim();
        if ((str.isEmpty()) || (!isValidEmail(str)))
        {
            this.errorflag = true;
            this.textEmail.setError("invalid email address");
            requestFocus(this.inputEmail);
            return false;
        }
        this.errorflag = false;
        this.textEmail.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword(){
        this.Password = this.inputPassword.getText().toString().trim();
        if (this.Password.isEmpty())
        {
            this.textPassword.setError("empty field please enter password");
            requestFocus(this.inputPassword);
            return false;
        }
        if (this.Password.length() < 6)
        {
            this.errorflag = true;
            this.textPassword.setError("Enter password with more than 6 characters");
            return false;
        }
        this.errorflag = false;
        this.textPassword.setErrorEnabled(false);
        return true;
    }


}

