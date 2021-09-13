package com.example.project.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.project.activity.LoginActivity;
import com.example.project.activity.SplashActivity;
import com.example.project.db.DatabaseUtil;
import com.example.project.bean.MyPlan;
import com.example.project.db.FirestoreManager;
import com.example.project.utils.PreferenceHandler;
import com.example.project.widget.CircleImageView;
import com.example.project.widget.adapter.MyPlanAdapter;
import com.example.project.R;
import com.example.project.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
//    private DatabaseUtil databaseUtil;
    private User user;
    private MyPlanAdapter adapter;
    private List<MyPlan> myPlans = new ArrayList<>();



    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
    }

    @Override
    public void onResume() {
        super.onResume();
//        databaseUtil.open();
//        Cursor cursor = databaseUtil.fetchUser(1);
        FirestoreManager.getUser(new PreferenceHandler(getContext()).getLoginName(), new FirestoreManager.OnFirestoreUserListener() {
            @Override
            public void success(User user) {

                TextView nameTv = rootView.findViewById(R.id.et_an);
                EditText userNameTv = rootView.findViewById(R.id.et_sd);

                EditText passTv = rootView.findViewById(R.id.et_ed);

                Spinner genderSp = rootView.findViewById(R.id.spinner);

                EditText birthTv = rootView.findViewById(R.id.et_bd);
                CircleImageView circleImageView = rootView.findViewById(R.id.civ);

                birthTv.setText(user.gender);
                String[] strs = new String[]{"boy", "girl"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_tv, strs);
                genderSp.setAdapter(adapter);
                genderSp.setSelection(user.gender.equals(strs[1])?1:0);
                genderSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        databaseUtil.open();
                        if (!user.gender.equals(strs[position])) {
                            user.gender = strs[position];
                            FirestoreManager.updateUser(user);
                        }

                        FirestoreManager.getPlans(user.name, new FirestoreManager.OnFirestorePlansListener() {
                            @Override
                            public void success(List<MyPlan> myStorePlans, String name, String imgTag) {
                                myPlans.clear();
                                if (myStorePlans != null && !myStorePlans.isEmpty()) {
                                    for (MyPlan myPlan : myStorePlans) {
                                        if (myPlan.isDel.equals("1")) {
                                            myPlans.add(myPlan);
                                        }
                                    }
                                }
                                MineFragment.this.adapter.submitList(myPlans);
                                MineFragment.this.adapter.notifyDataSetChanged();


                            }

                            @Override
                            public void failed(String msg) {

                            }
                        });

//                        databaseUtil.updateUser(1, user.name, user.userName, user.pass, user.gender, user.birthDate);
//                        databaseUtil.close();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                rootView.findViewById(R.id.iv_bd).setOnClickListener(new View.OnClickListener() {
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
//                                databaseUtil.open();
                                user.birthDate = year + "-" + month + "-" + dayOfMonth;
//                                databaseUtil.updateUser(1, user.name, user.userName, user.pass, user.gender, user.birthDate);
                                birthTv.setText(user.birthDate);
                                FirestoreManager.updateUser(user);
//                                databaseUtil.close();

                            }
                        });
                        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                    }
                });

                nameTv.setText(user.name);
                userNameTv.setText(user.userName);
                passTv.setText(user.pass);
                birthTv.setText(user.birthDate);
                circleImageView.setImageDrawable(getResources().getDrawable(User.parseImageRes(user.imgTag)));

            }

            @Override
            public void failed(String msg) {

            }
        });
//        if (cursor != null && cursor.moveToFirst()) {
//            user = new User(cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
//
//
//        }
//        Cursor planCursor = databaseUtil.fetchAll();
//        if (planCursor != null) {
//            myPlans.clear();
//            while (planCursor.moveToNext()) {
//                int id = planCursor.getInt(0);
//                String name = planCursor.getString(1);
//                String sd = planCursor.getString(2);
//                String ed = planCursor.getString(3);
//                String ac = planCursor.getString(4);
//                String isDel = planCursor.getString(5);
//                String isDone = planCursor.getString(6);
//                String isShare = planCursor.getString(7);
//                String doneDate = planCursor.getString(8);
//                int thumb = planCursor.getInt(9);
//                if (isDel.equals("1")) {
//                    myPlans.add(new MyPlan(id, name, sd, ed,ac, isDel, isDone, isShare, doneDate, thumb));
//                }
//
//            }
//        }
//        adapter.submitList(myPlans);
//        adapter.notifyDataSetChanged();
//        databaseUtil.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv);
        rootView.findViewById(R.id.btn_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);

                }

            }
        });
        rootView.findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                .setMessage("Sure log out?")
                .setNegativeButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new PreferenceHandler(getContext()).removeLogin();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        dialog.dismiss();
                        getActivity().finish();

                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create().show();
            }
        });
        adapter = new MyPlanAdapter(true);
        recyclerView.setAdapter(adapter);
//        databaseUtil = new DatabaseUtil(getContext());
        return rootView;
    }

}