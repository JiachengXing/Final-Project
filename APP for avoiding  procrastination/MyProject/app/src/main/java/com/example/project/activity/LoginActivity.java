package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.base.BaseActivity;
import com.example.project.bean.User;
import com.example.project.db.FirestoreManager;
import com.example.project.utils.PreferenceHandler;

public class LoginActivity extends BaseActivity {
//    private DatabaseUtil databaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        databaseUtil = new DatabaseUtil(this);
        findViewById(R.id.et_realPass).setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        EditText name = findViewById(R.id.et_text);
        EditText pass = findViewById(R.id.et_pass);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
//                    databaseUtil.open();
                    FirestoreManager.getUser(name.getText().toString(), new FirestoreManager.OnFirestoreUserListener() {
                        @Override
                        public void success(User user) {
                            if (user.name.equals(name.getText().toString()) && user.pass.equals(pass.getText().toString())) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                new PreferenceHandler(LoginActivity.this).setLogin(user.name);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "name or passworld error", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void failed(String msg) {
                            Toast.makeText(getApplicationContext(), "the user does not exist", Toast.LENGTH_LONG).show();

                        }
                    });
//                    Cursor cursor = databaseUtil.fetch(1);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        String dbName = cursor.getString(1);
//                        String dbPass = cursor.getString(2);
//                        if (dbName.equals(name.getText().toString()) && dbPass.equals(pass.getText().toString())) {
//                            databaseUtil.close();
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        } else {
//                            databaseUtil.close();
//                            Toast.makeText(getApplicationContext(), "name or passworld error", Toast.LENGTH_LONG).show();
//                        }
//
//                    } else {
//                        databaseUtil.close();
//                        Toast.makeText(getApplicationContext(), "the user does not exist", Toast.LENGTH_LONG).show();
//                    }
                }


            }
        });
    }


    @Override
    protected int setStatusBarColor() {
        return R.color.windowBackground;
    }
}