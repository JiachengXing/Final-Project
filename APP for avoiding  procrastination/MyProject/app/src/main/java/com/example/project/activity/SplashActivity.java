package com.example.project.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import com.example.project.db.DatabaseUtil;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.base.BaseActivity;
import com.example.project.utils.PreferenceHandler;

public class SplashActivity extends BaseActivity {

//    private DatabaseUtil databaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        databaseUtil = new DatabaseUtil(this);
        new Handler(getMainLooper()).postDelayed(() -> {
//            databaseUtil.open();
//            Cursor cursor = databaseUtil.fetchUser(1);
            String loginName = new PreferenceHandler(SplashActivity.this).getLoginName();
            if (!loginName.isEmpty()) {
//                User user = new User(cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
//                databaseUtil.close();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
//                databaseUtil.close();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
//            if (new PreferenceHandler(SplashActivity.this).getLoginName() != "") {

//            }
        }, 300);
    }

    @Override
    protected int setStatusBarColor() {
        return R.color.windowBackground;
    }
}