package com.example.project.widget.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.bean.Comment;
import com.example.project.bean.User;
import com.example.project.widget.CircleImageView;

public class MyCommentAdapter extends ListAdapter<Comment, RecyclerView.ViewHolder> {

    public MyCommentAdapter() {
        super(new DiffUtil.ItemCallback<Comment>() {
            @Override
            public boolean areItemsTheSame(@NonNull  Comment oldItem, @NonNull Comment newItem) {
                return oldItem.name.equals(newItem.name);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem == newItem;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyCommentViewHolder viewHolder = (MyCommentViewHolder)holder;
        Comment comment = getItem(position);
        viewHolder.bind(comment);
    }

    public static class MyCommentViewHolder extends  RecyclerView.ViewHolder {

        private View itemView;
        public MyCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
        protected void bind(Comment comment) {
            CircleImageView civ = itemView.findViewById(R.id.civ);
            TextView nameTv = itemView.findViewById(R.id.tv_name);
            TextView contentTv = itemView.findViewById(R.id.tv_content);
            civ.setImageDrawable(itemView.getContext().getResources().getDrawable(User.parseImageRes(comment.imgTag)));
            nameTv.setText(comment.name);
            contentTv.setText(comment.content);

        }
    }
}
