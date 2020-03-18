package com.alienware.scan2shop.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.helpers.StartUpManager;
/**
 * Created by henry cheruiyot on 2/4/2018.
 */
public class WelcomeActivity extends AppCompatActivity {
    private Button btnext;
    private Button btskip;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private StartUpManager prefManager;
    private ViewPager viewPager;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener(){
        public void onPageScrollStateChanged(int paramAnonymousInt) {}

        public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {}

        public void onPageSelected(int paramAnonymousInt)
        {
            addBottomDots(paramAnonymousInt);
            if (paramAnonymousInt == WelcomeActivity.this.layouts.length - 1)
            {
                btnext.setText(getString(R.string.start));
                btskip.setVisibility(View.GONE);
                return;
            }
            btnext.setText(getString(R.string.next));
            btskip.setVisibility(View.VISIBLE);
        }
    };

    private void addBottomDots(int paramInt){
        this.dots = new TextView[this.layouts.length];
        int[] arrayOfInt1 = getResources().getIntArray(R.array.array_dot_active);
        int[] arrayOfInt2 = getResources().getIntArray(R.array.array_dot_inactive);
        this.dotsLayout.removeAllViews();
        int i = 0;
        while (i < this.dots.length)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35.0F);
            dots[i].setTextColor(arrayOfInt2[paramInt]);
            dotsLayout.addView(dots[i]);
            i += 1;
        }
        if (dots.length > 0) {
            dots[paramInt].setTextColor(arrayOfInt1[paramInt]);
        }
    }

    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= 21)
        {
            Window localWindow = getWindow();
            localWindow.addFlags(Integer.MIN_VALUE);
            localWindow.setStatusBarColor(0);
        }
    }

    private int getItem(int paramInt)
    {
        return viewPager.getCurrentItem() + paramInt;
    }

    private void launchHomeScreen(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    protected void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);
        prefManager = new StartUpManager(this);
        if (!prefManager.isFirstTimeLaunch())
        {
            launchHomeScreen();
            finish();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
        }
        setContentView(R.layout.welcome);
        this.viewPager = findViewById(R.id.view_pager);
        this.dotsLayout = findViewById(R.id.layoutDots);
        this.btskip = findViewById(R.id.btn_skip);
        this.btnext = findViewById(R.id.btn_next);
        this.layouts = new int[] { R.layout.screen1_layout, R.layout.screen2_layout, R.layout.screen3_layout};
        addBottomDots(0);
        changeStatusBarColor();
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btskip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView)
            {
                WelcomeActivity.this.launchHomeScreen();
            }
        });
        this.btnext.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                int i = WelcomeActivity.this.getItem(1);
                if (i < WelcomeActivity.this.layouts.length)
                {
                    WelcomeActivity.this.viewPager.setCurrentItem(i);
                    return;
                }
                prefManager.setFirstTimeLaunch(false);
                WelcomeActivity.this.launchHomeScreen();
            }
        });
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {}

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject){
            paramViewGroup.removeView((View)paramObject);
        }
        public int getCount() {
            return layouts.length;
        }

        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            layoutInflater = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            View localView = layoutInflater.inflate(layouts[paramInt], paramViewGroup, false);
            paramViewGroup.addView(localView);
            return localView;
        }

        public boolean isViewFromObject(View paramView, Object paramObject) {
            return paramView == paramObject;
        }
    }
}
