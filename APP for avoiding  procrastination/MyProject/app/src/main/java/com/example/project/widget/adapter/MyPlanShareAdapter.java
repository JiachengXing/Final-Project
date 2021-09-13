package com.example.project.widget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.activity.PlanDetailActivity;
import com.example.project.bean.MyPlan;
import com.example.project.bean.User;
import com.example.project.db.DatabaseUtil;
import com.example.project.widget.CircleImageView;
import com.example.project.widget.MyClickListener;

public class MyPlanShareAdapter extends ListAdapter<MyPlan, RecyclerView.ViewHolder> {
    private DatabaseUtil databaseUtil;

    public MyPlanShareAdapter(Context context) {
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
        databaseUtil = new DatabaseUtil(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPlanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_share_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        MyPlanViewHolder viewHolder = (MyPlanViewHolder)holder;
        MyPlan myPlan = getItem(position);
        viewHolder.bind(myPlan, databaseUtil);


    }

    public static class MyPlanViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private DatabaseUtil databaseUtil;
        public MyPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        protected void bind(MyPlan myPlan, DatabaseUtil databaseUtil) {
            this.databaseUtil = databaseUtil;
            TextView anTv = itemView.findViewById(R.id.tv_an);
            TextView dataTv = itemView.findViewById(R.id.tv_data);
            TextView nameTv = itemView.findViewById(R.id.tv_name);
            CircleImageView civ = itemView.findViewById(R.id.civ);


            anTv.setText(myPlan.name);
            dataTv.setText(myPlan.doneDate);
            nameTv.setText(myPlan.userName);
            civ.setImageDrawable(itemView.getContext().getResources().getDrawable(User.parseImageRes(myPlan.imgTag)));
            itemView.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
                    PlanDetailActivity.startActivity(itemView.getContext(), myPlan, true);

                }

                @Override
                public void doubleClick() {
                    databaseUtil.open();
                    myPlan.thumb = myPlan.thumb + 1;
                    databaseUtil.update(0, myPlan.name, myPlan.sd, myPlan.ed, myPlan.ac, myPlan.isDel,myPlan.isDone, myPlan.isShare, myPlan.doneDate, myPlan.thumb);
                    databaseUtil.close();
                    Toast.makeText(itemView.getContext().getApplicationContext(), "thumb up success! current thumb is " + myPlan.thumb, Toast.LENGTH_LONG).show();

                }
            }));

        }
    }

}

