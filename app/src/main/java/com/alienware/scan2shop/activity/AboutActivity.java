package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alienware.scan2shop.R;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class AboutActivity extends AppCompatActivity {
    private TextView linktourl;
    private Toolbar toolbar;
    public void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        setContentView(R.layout.about_layout);
        toolbar = findViewById(R.id.toolbarAbout);
        linktourl = findViewById(R.id.linktourl);
        linktourl.setMovementMethod(LinkMovementMethod.getInstance());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

