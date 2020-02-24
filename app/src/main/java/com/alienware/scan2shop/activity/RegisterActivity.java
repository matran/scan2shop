package com.alienware.scan2shop.activity;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.helpers.UserSqliteHandler;
import com.alienware.scan2shop.helpers.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.alienware.scan2shop.helpers.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private View ProgressOverlay;
    private Button btnRegister;
    private UserSqliteHandler db;
    private String email;
    private boolean errorFlag = false;
    private ImageView errorImage;
    private TextView errorTextView;
    private AlphaAnimation inAnimation;
    private EditText inputEmail;
    private EditText inputFullName;
    private EditText lastnameEdit;
    private TextInputLayout lastnameInput;
    private TextInputLayout inputLEmail;
    private TextInputLayout inputLName;
    private TextInputLayout inputLPassword;
    private TextInputLayout inputLPhone;
    private TextInputLayout inputLRepeatPassword;
    private EditText inputPassword;
    private EditText inputPhone;
    private EditText inputRepeatPassword;
    private String firstname;
    private String lastname;
    private String Password;
    private String phone;
    private String repeatPassword;
    private ProgressBar progressBar;
    private Button retryButton;
    private SessionManager session;
    private TextView textView;
    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        setContentView(R.layout.register_layout);
        Utils.setUpToolbar(this);
        ProgressOverlay = findViewById(R.id.progress_overlay);
        errorTextView = findViewById(R.id.errorTextView);
        progressBar = findViewById(R.id.progressBar);
        inputLName = findViewById(R.id.tilUsername);
        inputLRepeatPassword=findViewById(R.id.tilRepeatPassword);
        inputRepeatPassword=findViewById(R.id.etRepeatPassword);
        lastnameInput= findViewById(R.id.tilUsernameSecond);
        lastnameEdit= findViewById(R.id.usernameSecond);
        inputLPhone = findViewById(R.id.PhoneNumber);
        inputLEmail = findViewById(R.id.tilEmailAddress);
        inputLPassword = findViewById(R.id.tilPassword);
        inputFullName = findViewById(R.id.username);
        inputEmail = findViewById(R.id.etEmailAddress);
        retryButton = findViewById(R.id.retryButton);
        inputPhone = findViewById(R.id.etPhoneNumber);
        inputPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.register);
        lastnameEdit.addTextChangedListener(new MyTextWatcher(lastnameEdit));
        inputFullName.addTextChangedListener(new MyTextWatcher(inputFullName));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputRepeatPassword.addTextChangedListener(new MyTextWatcher(inputRepeatPassword));
        textView = findViewById(R.id.tvToolbarText);
        textView.setText("Register");
        session = new SessionManager(getApplicationContext());
        db = new UserSqliteHandler(getApplicationContext());
        if (session.isLoggedIn())
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname=inputFullName.getText().toString().trim();
                lastname=lastnameEdit.getText().toString().trim();
                phone=inputPhone.getText().toString().trim();
                email =inputEmail.getText().toString().trim();
                Password=inputPassword.getText().toString().trim();
                if ((!firstname.isEmpty()) &&(!lastname.isEmpty()) && (!email.isEmpty()) && (!Password.isEmpty()) && !errorFlag) {
                    registerUser(firstname, lastname, phone, email, Password);
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                firstname=inputFullName.getText().toString().trim();
                lastname=lastnameEdit.getText().toString().trim();
                phone=inputPhone.getText().toString().trim();
                email =inputEmail.getText().toString().trim();
                Password=inputPassword.getText().toString().trim();
                if ((!firstname.isEmpty()) &&(!lastname.isEmpty()) && (!email.isEmpty()) && (!Password.isEmpty()) && !errorFlag) {
                    registerUser(firstname,lastname,phone,email,Password);

                }
            }
        });
    }

    private class MyTextWatcher implements TextWatcher
    {
        private View view;

        private MyTextWatcher(View paramView)
        {
            this.view = paramView;
        }

        public void afterTextChanged(Editable paramEditable)
        {
            switch (this.view.getId())
            {

                case R.id.username:
                    validateNameFirst();
                    return;
                case R.id.usernameSecond:
                    validateNameLast();
                    return;
                case R.id.etPhoneNumber:
                    validatePhone();
                    return;
                case R.id.etEmailAddress:
                    validateEmail();
                    return;
                case R.id.etPassword:
                    validatePassword();
                    return;
                case R.id.etRepeatPassword:
                    validateRepeatPassword();
                default:
            }

        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }

    private void hideSoftInput()
    {
        ((InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.ProgressOverlay.getWindowToken(), 2);
    }

    private static boolean isValidEmail(String paramString)
    {
        return (!TextUtils.isEmpty(paramString)) && (Patterns.EMAIL_ADDRESS.matcher(paramString).matches());
    }

    private void registerUser(final String firstname, final String lastname, final String phone, final String email, final String password) {
        hideSoftInput();
        inAnimation = new AlphaAnimation(0.0F, 1.0F);
        inAnimation.setDuration(200L);
        ProgressOverlay.setAnimation(this.inAnimation);
        ProgressOverlay.setVisibility(View.VISIBLE);
        final String URL = Config.URL_REGISTER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phone", phone);
        params.put("email", email);
        params.put("password", password);
        params.put("privilege","user");
        JsonObjectRequest str = new JsonObjectRequest(URL,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    String status = response.getString("status");
                    if (status.equals("success")){
                        Toast.makeText(getApplicationContext(),"Registered successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("fail")){
                        progressBar.setVisibility(View.GONE);
                        retryButton.setVisibility(View.VISIBLE);
                        errorTextView.setText(response.getString("message"));
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.GONE);
                    retryButton.setVisibility(View.VISIBLE);
                    errorTextView.setText("Unknown error has occurred.try again");
                    errorTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
      {
        public void onErrorResponse(VolleyError e)
        {
           progressBar.setVisibility(View.GONE);
           retryButton.setVisibility(View.VISIBLE);
           errorTextView.setText("No Internet connection.Make sure you are connected to in-store wifi,then try again");
           errorTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    });

        new DefaultRetryPolicy(0, 1, 1.0F);
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_register");
    }
    private void requestFocus(View paramView)
    {
        if (paramView.requestFocus()) {
            getWindow().setSoftInputMode(5);
        }
    }

    private boolean validateEmail(){
        String str = this.inputEmail.getText().toString().trim();
        if ((str.isEmpty()) || (!isValidEmail(str)))
        {
            errorFlag = true;
            inputLEmail.setError("invalid email address");
            requestFocus(inputEmail);
            return false;
        }
            errorFlag = false;
            inputLEmail.setErrorEnabled(false);
            return true;
    }
    private boolean validateNameFirst()
    {
        firstname = this.inputFullName.getText().toString().trim();
        if (firstname.isEmpty())
        {
            this.inputLName.setError("Empty field username");
            requestFocus(inputFullName);
            return false;
        }
            errorFlag = false;
            inputLName.setErrorEnabled(false);
            return true;

    }

    private boolean validateNameLast()
    {
        lastname = lastnameEdit.getText().toString().trim();
        if (lastname.isEmpty())
        {
            errorFlag = true;
            lastnameInput.setError("Empty field lastname");
            requestFocus(lastnameEdit);
            return false;
        }
            errorFlag = false;
            inputLName.setErrorEnabled(false);
            return true;
    }

    private boolean validatePassword()
    {
        this.Password = this.inputPassword.getText().toString().trim();
        if (this.Password.isEmpty())
        {
            errorFlag = true;
            this.inputLPassword.setError("empty field please enter password");
            requestFocus(this.inputPassword);
            return false;
        }
        if (this.Password.length() < 6)
        {
            errorFlag = true;
            this.inputLPassword.setError("Enter password with more than 6 characters");
            requestFocus(this.inputPassword);
            return false;
        }
            errorFlag = false;
            inputLPassword.setErrorEnabled(false);
            return true;
    }

    private boolean validatePhone()
    {
        phone = this.inputPhone.getText().toString().trim();
        if(phone.length()>12||phone.length()<12){
            errorFlag = true;
            inputLPhone.setError("Invalid phone number");
            requestFocus(inputPhone);
            return false;
        }
        if (!phone.startsWith("254"))
        {
            errorFlag = true;
            inputLPhone.setError("Invalid phone number. Must start with 254");
            requestFocus(inputPhone);
            return false;
        }
            errorFlag = false;
            inputLPhone.setErrorEnabled(false);
            return true;

    }
    private boolean validateRepeatPassword(){
        Password = inputPassword.getText().toString().trim();
        repeatPassword=inputRepeatPassword.getText().toString().trim();
        if (!repeatPassword.equals(Password))
        {
            errorFlag = true;
            inputLRepeatPassword.setError("Password does not match");
            requestFocus(inputRepeatPassword);
            return false;
        }else {
            errorFlag = false;
            inputLRepeatPassword.setErrorEnabled(false);
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RegisterActivity.this.finish();
    }
}
