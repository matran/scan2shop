package com.alienware.scan2shop.activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alienware.scan2shop.R;

public class Customer360 extends Fragment {
    private ViewFlipper mViewFlipper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_360_layout, container, false);
        mViewFlipper = view.findViewById(R.id.view_flipper);
        this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out));
        this.mViewFlipper.setAutoStart(true);
        this.mViewFlipper.setFlipInterval(3000);
        this.mViewFlipper.startFlipping();
        animateSlideshow();
        return  view;
    }
    private void animateSlideshow() {
        this.mViewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation paramAnonymousAnimation) {}

            public void onAnimationRepeat(Animation paramAnonymousAnimation) {}

            public void onAnimationStart(Animation paramAnonymousAnimation) {}
        });
    }
}
