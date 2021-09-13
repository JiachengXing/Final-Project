package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.MainActivity;
import com.example.project.bean.Comment;
import com.example.project.bean.User;
import com.example.project.db.DatabaseUtil;
import com.example.project.bean.MyPlan;
import com.example.project.R;
import com.example.project.base.BaseActivity;
import com.example.project.db.FirestoreManager;
import com.example.project.utils.PreferenceHandler;
import com.example.project.utils.SPUtils;
import com.example.project.widget.adapter.MyCommentAdapter;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.Calendar;

public class PlanDetailActivity extends BaseActivity {

    public static final String KEY_MY_PLAN = "key_my_plan";
    private static final String KEY_IS_SHARE_DETAIL = "key_is_share_detail";
    private static final String KEY_MY_PLAN_HISTORY = "key_my_plan_history";
    private MyPlan myPlan;
    //    private DatabaseUtil databaseUtil;
    private boolean isShareDetail, isForHistory;
    private User user;

    public static void startActivity(Context context, MyPlan myPlan) {
        Intent intent = new Intent(context, PlanDetailActivity.class);
        intent.putExtra(KEY_MY_PLAN, myPlan);
        context.startActivity(intent);

    }

    public static void startActivityForHistory(Context context, MyPlan myPlan) {
        Intent intent = new Intent(context, PlanDetailActivity.class);
        intent.putExtra(KEY_MY_PLAN, myPlan);
        intent.putExtra(KEY_MY_PLAN_HISTORY, true);
        context.startActivity(intent);

    }

    public static void startActivity(Context context, MyPlan myPlan, boolean isShareDetail) {
        Intent intent = new Intent(context, PlanDetailActivity.class);
        intent.putExtra(KEY_MY_PLAN, myPlan);
        intent.putExtra(KEY_IS_SHARE_DETAIL, isShareDetail);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        myPlan = (MyPlan) getIntent().getSerializableExtra(KEY_MY_PLAN);
        isShareDetail = getIntent().getBooleanExtra(KEY_IS_SHARE_DETAIL, false);
        isForHistory = getIntent().getBooleanExtra(KEY_MY_PLAN_HISTORY, false);
//        databaseUtil = new DatabaseUtil(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setToolbarCustomTheme();
        TextView anTv = findViewById(R.id.et_an);
        TextView sdTv = findViewById(R.id.et_sd);
        TextView edTv = findViewById(R.id.et_ed);
        TextView acTv = findViewById(R.id.spinner);
        TextView noteTv = findViewById(R.id.tv_note);
        TextView rgdTv = findViewById(R.id.tv_rgd);
        Button doneBtn = findViewById(R.id.btn_done);

        anTv.setText(myPlan.name);
        sdTv.setText(myPlan.sd);
        edTv.setText(myPlan.ed);
        acTv.setText(myPlan.ac);
        noteTv.setText(myPlan.note);
        rgdTv.setText(String.valueOf(myPlan.remindingGap));
        doneBtn.setText(myPlan.isDone.equals("1") ? "SHARE" : "DONE");
        doneBtn.setVisibility((myPlan.isShare.equals("1") || myPlan.isDel.equals("1")) ? View.GONE : View.VISIBLE);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doneBtn.getText().equals("DONE")) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                    if (month == 12) {
                        month = 1;
                    } else {
                        month = month + 1;
                    }
                    myPlan.doneDate = year + "-" + month + "-" + dayOfMonth;
                    myPlan.isDone = "1";
                    FirestoreManager.updatePlan(myPlan);
                    doneBtn.setText("SHARE");
                } else {
                    FirestoreManager.updatePlan(myPlan);
                    myPlan.isShare = "1";
                    FirestoreManager.updatePlan(myPlan);
                    doneBtn.setVisibility(View.GONE);
                }
            }
        });
        TextView delTv = findViewById(R.id.tv_del);
        if (isForHistory) {
            delTv.setVisibility(View.GONE);
        } else {
            delTv.setVisibility(View.VISIBLE);
            delTv.setVisibility(myPlan.userName.equals(new PreferenceHandler(PlanDetailActivity.this).getLoginName()) ? View.VISIBLE : View.GONE);
            delTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPlan.isDel = "1";
                    FirestoreManager.updatePlan(myPlan);
//                databaseUtil.open();
//                databaseUtil.update(0, myPlan.name, myPlan.sd, myPlan.ed, myPlan.ac, "1",myPlan.isDone, myPlan.isShare, myPlan.doneDate, myPlan.thumb);
//                databaseUtil.close();
                    setResult(111, new Intent(PlanDetailActivity.this, MainActivity.class));
                    finish();
                }
            });
        }


        if (isShareDetail) {
            FirestoreManager.getUser(new PreferenceHandler(PlanDetailActivity.this).getLoginName(), new FirestoreManager.OnFirestoreUserListener() {
                @Override
                public void success(User user) {
                    PlanDetailActivity.this.user = user;
                }

                @Override
                public void failed(String msg) {

                }
            });

            ShineButton btStart = findViewById(R.id.btn_shine);
            TextView tcTv = findViewById(R.id.tv_tc);
            TextView etTv = findViewById(R.id.et_tc);
            TextView ddTv = findViewById(R.id.tv_dd);
            TextView ddEt = findViewById(R.id.et_dd);

            tcTv.setVisibility(View.VISIBLE);
            etTv.setVisibility(View.VISIBLE);
            ddTv.setVisibility(View.VISIBLE);
            ddEt.setVisibility(View.VISIBLE);
            ddEt.setText(myPlan.doneDate);
            etTv.setText(String.valueOf(myPlan.thumb));

            btStart.setChecked((Boolean) SPUtils.get(PlanDetailActivity.this, "btStart-" + myPlan.name, false));
            btStart.setVisibility(View.VISIBLE);
            btStart.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean b) {
                    if (b) {
                        myPlan.thumb = myPlan.thumb + 1;
                    } else {
                        myPlan.thumb = myPlan.thumb - 1;
                    }
                    SPUtils.put(PlanDetailActivity.this, "btStart-" + myPlan.name, b);

                    etTv.setText(String.valueOf(myPlan.thumb));
                    FirestoreManager.updatePlan(myPlan);
                }
            });

            btStart.init(this);

            Button commentBtn = findViewById(R.id.btn);
            EditText contentEt = findViewById(R.id.et_content);
            RecyclerView recyclerView = findViewById(R.id.rv);

            commentBtn.setVisibility(View.VISIBLE);
            contentEt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            MyCommentAdapter adapter = new MyCommentAdapter();
            recyclerView.setAdapter(adapter);

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user == null) {
                        return;
                    }
                    String content = contentEt.getText().toString();
                    if (!content.isEmpty()) {
                        if (myPlan.comments == null) {
                            myPlan.comments = new ArrayList<>();
                        }
                        myPlan.comments.add(new Comment(user.name, user.imgTag, String.valueOf(System.currentTimeMillis()), content));
                        FirestoreManager.updatePlan(myPlan);
                        adapter.submitList(myPlan.comments);
                        adapter.notifyDataSetChanged();

                    }
                }
            });
            if (myPlan.comments != null) {
                adapter.submitList(myPlan.comments);
                adapter.notifyDataSetChanged();
            }
        }
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