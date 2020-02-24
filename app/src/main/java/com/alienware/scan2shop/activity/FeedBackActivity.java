package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.application.Scan2ShopApplication;
import com.alienware.scan2shop.config.Config;
import com.alienware.scan2shop.helpers.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
/**
 * Created by henry cheruiyot on 3/3/2018.
 */
public class FeedBackActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    private Button submitButton;
    private View progressView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        Utils.setUpToolbar(this);
        this.textView = findViewById(R.id.tvToolbarText);
        this.textView.setText("Feedback");
        editTextEmail=findViewById(R.id.feedbackEmail);
        editTextSubject=findViewById(R.id.feedbackSubject);
        editTextMessage=findViewById(R.id.feedbackMessage);
        progressView=findViewById(R.id.llProgressBar);
        submitButton=findViewById(R.id.feedbackSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editTextEmail.getText().toString().trim();
                String subject=editTextSubject.getText().toString().trim();
                String message=editTextMessage.getText().toString().trim();
                if(!email.equals("")&&!subject.equals("")&&!message.equals("")){
                    sendFeedBack(email,subject,message);
                }
            }
        });
    }
    private void sendFeedBack(String email,String subject,String message){
        progressView.setVisibility(View.VISIBLE);
        final String URL = Config.URL_FEEDBACK;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("subject", subject);
        params.put("message",message);
        JsonObjectRequest str = new JsonObjectRequest(URL,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressView.setVisibility(View.GONE);
                try{
                    String status=response.getString("status");
                    if (status.equals("success"))
                    {

                    }else if(status.equals("fail")) {

                        Toast.makeText(FeedBackActivity.this, "Not sent try again ", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(FeedBackActivity.this, "Not sent try again", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progressView.setVisibility(View.GONE);
                    Toast.makeText(FeedBackActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError e) {
                progressView.setVisibility(View.GONE);
                Toast.makeText(FeedBackActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        str.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1.0F));
        Scan2ShopApplication.getInstance().addToRequestQueue(str, "req_feedback");
    }
}
