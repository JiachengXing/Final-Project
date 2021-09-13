package com.example.project.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.project.MainActivity;
import com.example.project.bean.MyPlan;
import com.example.project.db.DatabaseUtil;
import com.example.project.db.FirestoreManager;
import com.example.project.fragment.DatePickerFragment;
import com.example.project.R;
import com.example.project.base.BaseActivity;
import com.example.project.utils.PreferenceHandler;

public class AddPlanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setToolbarCustomTheme();

        EditText sdEt = findViewById(R.id.et_sd);
        EditText edEt = findViewById(R.id.et_ed);
        EditText anEt = findViewById(R.id.et_an);
        EditText noteEt = findViewById(R.id.et_note);
        TextView rgdTv = findViewById(R.id.tv_rgd);


        findViewById(R.id.iv_sd).setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    if (month == 12) {
                        month = 1;
                    } else {
                        month = month + 1;
                    }
                    sdEt.setText(year + "-" + month + "-" + dayOfMonth);

                }
            });
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        findViewById(R.id.iv_ed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (month == 12) {
                            month = 1;
                        } else {
                            month = month + 1;
                        }
                        edEt.setText(year + "-" + month + "-" + dayOfMonth);

                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        String[] strs = new String[]{"study", "sports", "labor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_tv, strs);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        final int[] acPosition = {0};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                acPosition[0] = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String an = anEt.getText().toString();
                String sd = sdEt.getText().toString();
                String ed = edEt.getText().toString();

                if (!an.isEmpty() && !sd.isEmpty() && !ed.isEmpty() && ed.compareTo(sd)>=0) {
                    FirestoreManager.insertPlan(new MyPlan(an, sd, ed, strs[acPosition[0]], "", "","","",0,
                            new PreferenceHandler(AddPlanActivity.this).getLoginName(),noteEt.getText().toString(),Integer.parseInt(rgdTv.getText().toString())));
                    setResult(111, new Intent(AddPlanActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Input error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rgd = Integer.parseInt(rgdTv.getText().toString());
                rgdTv.setText(String.valueOf(rgd+1));
            }
        });
        findViewById(R.id.iv_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rgd = Integer.parseInt(rgdTv.getText().toString());
                rgdTv.setText(String.valueOf(Math.max(rgd - 1, 0)));
            }
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