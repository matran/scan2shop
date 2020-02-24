package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.helpers.Utils;

/**
 * Created by henry cheruiyot on 3/3/2018.
 */
public class LoyaltyActivity extends AppCompatActivity {
private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loyalty_layout);
        this.textView = findViewById(R.id.tvToolbarText);
        this.textView.setText("Loyalty");
        Utils.setUpToolbar(this);
    }
}

