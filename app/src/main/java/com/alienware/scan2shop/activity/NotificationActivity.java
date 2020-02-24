package com.alienware.scan2shop.activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alienware.scan2shop.R;

public class NotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.notification_layout);
        toolbar = findViewById(R.id.toolbarNOTI);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
