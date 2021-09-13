package com.example.project.base;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.gyf.immersionbar.ImmersionBar;

public class BaseActivity extends AppCompatActivity {

    protected ImmersionBar mImmersionBar;



    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();

    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.barColor(setStatusBarColor());
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected int setStatusBarColor(){
        return android.R.color.white;
    }

    protected void setToolbarCustomTheme() {
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        if(upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
        }
    }
}
