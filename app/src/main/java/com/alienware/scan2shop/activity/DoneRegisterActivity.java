package com.alienware.scan2shop.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alienware.scan2shop.R;
public class DoneRegisterActivity extends AppCompatActivity {
    private Button doneButton;
    private TextView textSuccess;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_verification_layout);
        doneButton=findViewById(R.id.continueButton);
        textSuccess=findViewById(R.id.text2);
        textSuccess.setText("Your account has been created successfully");
        doneButton.setText("Done");
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoneRegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(DoneRegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
