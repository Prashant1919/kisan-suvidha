package com.example.greenlifeuser.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.greenlifeuser.R;
import com.example.greenlifeuser.interfaces.PostItemClickListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvUsername, tvTitle;
    public ImageView imgPost;
    public CircleImageView imgPostProfile;
    public ProgressBar progressBar;
    public PostItemClickListener listener;
    private final Context context;

    public PostAdapter(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        tvUsername = itemView.findViewById(R.id.row_post_profile_name);
        tvTitle = itemView.findViewById(R.id.row_post_title);
        imgPost = itemView.findViewById(R.id.row_post_img);
        imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
    }

    public void setItemClickListener(PostItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }

}
