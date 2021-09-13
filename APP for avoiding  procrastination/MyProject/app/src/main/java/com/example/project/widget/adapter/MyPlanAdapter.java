package com.example.project.widget.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.activity.PlanDetailActivity;
import com.example.project.bean.MyPlan;

public class MyPlanAdapter extends ListAdapter<MyPlan, RecyclerView.ViewHolder> {

    private boolean isHistory;
    private OnItemClickListener onItemClickListener;

    public MyPlanAdapter(boolean isHistory) {
        super(new DiffUtil.ItemCallback<MyPlan>() {
            @Override
            public boolean areItemsTheSame(@NonNull  MyPlan oldItem, @NonNull MyPlan newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull MyPlan oldItem, @NonNull MyPlan newItem) {
                return oldItem == newItem;
            }
        });
        this.isHistory = isHistory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPlanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        MyPlanViewHolder viewHolder = (MyPlanViewHolder)holder;
        MyPlan myPlan = getItem(position);
        viewHolder.bind(myPlan, isHistory, onItemClickListener);


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class MyPlanViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        public MyPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        protected void bind(MyPlan myPlan, boolean isHistory, OnItemClickListener onItemClickListener) {
            TextView anTv = itemView.findViewById(R.id.tv_an);
            TextView dataTv = itemView.findViewById(R.id.tv_data);
            anTv.setText(myPlan.name);
            dataTv.setText(myPlan.sd + "  to  " + myPlan.ed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isHistory) {
                        PlanDetailActivity.startActivityForHistory(v.getContext(), myPlan);
                    } else {
                        if (onItemClickListener != null) {
                            onItemClickListener.onClick(myPlan);
                        } else {
                            PlanDetailActivity.startActivity(v.getContext(), myPlan);

                        }

                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onClick(MyPlan myPlan);
    }

}

