package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.base.BaseActivity;
import com.example.project.bean.User;
import com.example.project.db.FirestoreManager;
import com.example.project.utils.PreferenceHandler;

import java.util.Random;

public class RegisterActivity extends BaseActivity {

//    private DatabaseUtil databaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setToolbarCustomTheme();
        EditText nameEt = findViewById(R.id.et_text);
        EditText passEt = findViewById(R.id.et_pass);
        EditText passRetryEt = findViewById(R.id.et_realPass);
//        databaseUtil = new DatabaseUtil(this);
        findViewById(R.id.btn_register).setOnClickListener(v -> {
            String name = nameEt.getText().toString();
            String pass = passEt.getText().toString();
            String passRetry = passRetryEt.getText().toString();

            if (!name.isEmpty() && !pass.isEmpty() && pass.equals(passRetry)) {
//                databaseUtil.open();
//                databaseUtil.deleteAllUser();
//                databaseUtil.insertUser(name, "",pass, "","");
//                databaseUtil.close();
                FirestoreManager.userAdd(new User(name, "", pass, "", "", String.valueOf(new Random().nextInt(4))), new FirestoreManager.OnFirestoreUserAddListener() {
                    @Override
                    public void success(User user) {
                        new PreferenceHandler(RegisterActivity.this).setLogin(user.name);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void failed(String msg) {
                        Toast.makeText(getApplicationContext(), "error:" + msg, Toast.LENGTH_LONG).show();

                    }
                });

//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            } else {

                Toast.makeText(getApplicationContext(), "input error!", Toast.LENGTH_LONG).show();


            }




//            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int setStatusBarColor() {
        return R.color.windowBackground;
    }
}