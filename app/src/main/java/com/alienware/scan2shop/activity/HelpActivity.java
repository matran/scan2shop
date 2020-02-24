package com.alienware.scan2shop.activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.config.Config;

import java.util.Objects;

/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class HelpActivity  extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    protected void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        setContentView(R.layout.help_layout);
        toolbar = findViewById(R.id.toolbarHelp);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");
        webView = findViewById(R.id.myWebView);
        webView.loadUrl(Config.URL_HELP);
        webView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        ProgressBar progress = findViewById(R.id.progressBar);
        private MyWebViewClient() {

        }
        public void onPageFinished(WebView paramWebView, String paramString) {
            super.onPageFinished(paramWebView, paramString);
            progress.setVisibility(View.GONE);
            progress.setProgress(100);
        }
        public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap) {
            super.onPageStarted(paramWebView, paramString, paramBitmap);
            this.progress.setVisibility(View.VISIBLE);
            setProgress(0);
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            paramWebView.loadUrl(paramString);
            return false;
        }
    }
}
