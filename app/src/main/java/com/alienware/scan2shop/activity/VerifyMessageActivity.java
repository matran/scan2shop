package com.alienware.scan2shop.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.alienware.scan2shop.R;
public class VerifyMessageActivity extends AppCompatActivity {
    private Button continueButton;
    private String phoneno=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_verification_layout);
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
        continueButton=findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VerifyMessageActivity.this,RegisterActivity.class);
                intent.putExtra("phone",phoneno);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(VerifyMessageActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
