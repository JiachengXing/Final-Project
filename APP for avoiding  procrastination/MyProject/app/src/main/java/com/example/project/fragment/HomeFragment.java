package com.example.project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.bean.MyPlan;
import com.example.project.db.DatabaseUtil;
import com.example.project.db.FirestoreManager;
import com.example.project.widget.adapter.MyPlanShareAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseUtil databaseUtil;
    private List<MyPlan> myPlans = new ArrayList<>();
    private View rootView;
    private MyPlanShareAdapter adapter;
    private int spinnerPosition = 0;
    String[] strs = new String[]{"all", "study", "sports", "labor"};

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private List<MyPlan> plansForSpinnerPosition() {
        List<MyPlan> plans = new ArrayList<>();
        if (spinnerPosition == 0) {
            plans.addAll(myPlans);
        } else {
            for (MyPlan myPlan: myPlans) {
                if (myPlan.ac.equals(strs[spinnerPosition])) {
                    plans.add(myPlan);
                }
            }
        }

        return plans;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirestoreManager.getSharePlans(new FirestoreManager.OnFirestoreSharePlansListener() {
            @Override
            public void success(List<MyPlan> mySharePlans) {
                myPlans.clear();
                myPlans.addAll(mySharePlans);
                adapter.submitList(plansForSpinnerPosition());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void failed(String msg) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Spinner spinner = rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_tv, strs);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerPosition);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv);
        adapter = new MyPlanShareAdapter(getContext());
        recyclerView.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
                adapter.submitList(plansForSpinnerPosition());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        databaseUtil = new DatabaseUtil(getContext());
        return rootView;
    }
}