package com.example.project.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.activity.AddPlanActivity;
import com.example.project.activity.FocusedSettingsActivity;
import com.example.project.activity.PlanDetailActivity;
import com.example.project.bean.MyPlan;
import com.example.project.db.FirestoreManager;
import com.example.project.utils.PreferenceHandler;
import com.example.project.utils.SPUtils;
import com.example.project.widget.adapter.MyPlanAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActivityResultLauncher<Intent> startActivityLauncher;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private DatabaseUtil databaseUtil;
    private List<MyPlan> myPlans = new ArrayList<>();
    private View rootView;
    private MyPlanAdapter adapter;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        startActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> getPlans(), 1000);



            }
        });
    }

    public void createNotification() {
        int YEAR, MONTH, DAY_OF_MONTH;
        final Calendar c = Calendar.getInstance();
        YEAR = c.get(Calendar.YEAR);
        MONTH = c.get(Calendar.MONTH);
        DAY_OF_MONTH = c.get(Calendar.DAY_OF_MONTH);

        if (MONTH == 12) {
            MONTH = 1;
        } else {
            MONTH = MONTH + 1;
        }
        String date = YEAR + "-" + MONTH + "-" + DAY_OF_MONTH;
        if (SPUtils.get(getContext(), "DATA","").equals(date)) {
            return;
        }

        for (MyPlan myPlan: myPlans) {
            if (myPlan.remindingGap > 0) {
                if (SPUtils.get(getContext(), date + myPlan.toString(),"").equals(myPlan.toString())) {
                    continue;
                }

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            String notificationChannelId = getContext().getPackageName();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = getContext().getPackageName();
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
                notificationChannel.setDescription("description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), notificationChannelId);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(myPlan.name);
            builder.setContentText(myPlan.note);

            notificationManager.notify(myPlans.indexOf(myPlan), builder.build());









//                if (notifyDay == DAY_OF_MONTH) {
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), getContext().getPackageName())
//                            .setContentTitle (myPlan.name)
//                            .setContentText (myPlan.note)
//                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                            .setSmallIcon (R.mipmap.ic_launcher);
//
//                    Notification notification = builder.build();
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
////            NotificationManager notificationManager = (NotificationManager)getContext().getSystemService (Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify (myPlans.indexOf(myPlan), notification);
//                }
                SPUtils.put(getContext(), date + myPlan.toString(), myPlan.toString());

            }

        }
//        SPUtils.put(getContext(), "DATA", date);

    }

    private void getPlans() {
        FirestoreManager.getPlans(new PreferenceHandler(getContext()).getLoginName(), new FirestoreManager.OnFirestorePlansListener() {
            @Override
            public void success(List<MyPlan> myStorePlans, String name, String imgTag) {
                myPlans.clear();
                if (myStorePlans != null && !myStorePlans.isEmpty()) {
                    for (MyPlan myPlan: myStorePlans) {
                        if (!myPlan.isDel.equals("1")) {
                            myPlans.add(myPlan);
                        }
                    }
//                    myPlans.addAll(myStorePlans);
                }
                adapter.submitList(myPlans);
                adapter.notifyDataSetChanged();
                createNotification();
//                rootView.findViewById(R.id.iv_add).setVisibility(myPlans.size() == 0 ? View.VISIBLE:View.GONE);
//                rootView.findViewById(R.id.rv).setVisibility(myPlans.size() > 0 ? View.VISIBLE:View.GONE);

            }

            @Override
            public void failed(String msg) {
//                rootView.findViewById(R.id.iv_add).setVisibility(myPlans.size() == 0 ? View.VISIBLE:View.GONE);
//                rootView.findViewById(R.id.rv).setVisibility(myPlans.size() > 0 ? View.VISIBLE:View.GONE);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlans();

//        if (databaseUtil != null) {
//            databaseUtil.open();
//            Cursor cursor = databaseUtil.fetchAll();
//            if (cursor != null) {
//                myPlans.clear();
//                while (cursor.moveToNext()) {
//                    int id = cursor.getInt(0);
//                    String name = cursor.getString(1);
//                    String sd = cursor.getString(2);
//                    String ed = cursor.getString(3);
//                    String ac = cursor.getString(4);
//                    String isDel = cursor.getString(5);
//                    String isDone = cursor.getString(6);
//                    String isShare = cursor.getString(7);
//                    String doneDate = cursor.getString(8);
//                    int thumb = cursor.getInt(9);
//                    if (isDel.equals("0")) {
//                        myPlans.add(new MyPlan(name, sd, ed,ac, isDel, isDone, isShare, doneDate, thumb));
//                    }
//
//                }
//            }
//            databaseUtil.close();
//            adapter.submitList(myPlans);
//            adapter.notifyDataSetChanged();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_project, container, false);
        rootView.findViewById(R.id.btn_focused).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityLauncher.launch(new Intent(getContext(), FocusedSettingsActivity.class));
//                startActivityForResult(new Intent(getContext(), FocusedSettingsActivity.class), 1111);
            }
        });
//        rootView.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), AddPlanActivity.class));
//
//            }
//        });
        rootView.findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), AddPlanActivity.class));
                startActivityLauncher.launch(new Intent(getContext(), AddPlanActivity.class));

            }
        });
        RecyclerView recyclerView = rootView.findViewById(R.id.rv);
        adapter = new MyPlanAdapter(false);
        adapter.setOnItemClickListener(new MyPlanAdapter.OnItemClickListener() {
            @Override
            public void onClick(MyPlan myPlan) {
                Intent intent = new Intent(getContext(), PlanDetailActivity.class);
                intent.putExtra(PlanDetailActivity.KEY_MY_PLAN, myPlan);
                startActivityLauncher.launch(intent);

            }
        });
        recyclerView.setAdapter(adapter);
//        databaseUtil = new DatabaseUtil(getContext());
        return rootView;
    }

}