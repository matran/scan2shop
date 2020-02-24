package com.alienware.scan2shop.activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alienware.scan2shop.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
public class DiscoverFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    public static String TAG="discoverfragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"oncreate");

    }
    @Override
    public void onResume() {
        super.onResume();
        setupViewPager(viewPager);
        Log.e(TAG,"onResume");

    }

    @Override
    public void setUserVisibleHint(boolean paramBoolean) {
        super.setUserVisibleHint(paramBoolean);
        Log.e(TAG,"onvisible");

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"oattach");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.discover_main,container,false);
        tabLayout= view.findViewById(R.id.tabs);
        viewPager= view.findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        return  view;
    }
    private void setupViewPager(ViewPager paramViewPager) {
        ViewPagerAdapter localViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        localViewPagerAdapter.addFragment(new Customer360(), "Customer360");
        localViewPagerAdapter.addFragment(new com.alienware.scan2shop.activity.LoyaltyFragment(), "Loyalty");
        localViewPagerAdapter.addFragment(new com.alienware.scan2shop.activity.ProductsFragment(),"products");
        paramViewPager.setAdapter(localViewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> FragmentList = new ArrayList();
        private final List<String> FragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager paramFragmentManager) {
            super(paramFragmentManager);
        }

        public void addFragment(Fragment paramFragment, String paramString) {
            FragmentList.add(paramFragment);
            FragmentTitleList.add(paramString);
        }
        public int getCount() {
            return FragmentList.size();
        }

        public Fragment getItem(int paramInt) {
            return FragmentList.get(paramInt);
        }
        public CharSequence getPageTitle(int paramInt) {
            return FragmentTitleList.get(paramInt);
        }
    }
}
