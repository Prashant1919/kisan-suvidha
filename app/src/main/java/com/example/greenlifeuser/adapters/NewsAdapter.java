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

public class NewsAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView newsTitle, newsSource, newsDescription,newsPrice,newsNumber;
    public ImageView newsImage, newsEdit, newsDelete;
    public ProgressBar progressBar;
    public PostItemClickListener listener;
    private final Context context;

    public NewsAdapter(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        newsTitle = itemView.findViewById(R.id.news_title);
        newsSource = itemView.findViewById(R.id.news_source);
        newsPrice = itemView.findViewById(R.id.news_price);
        newsDescription = itemView.findViewById(R.id.news_description);
        newsNumber = itemView.findViewById(R.id.news_number);
        newsImage = itemView.findViewById(R.id.news_image);
        newsEdit = itemView.findViewById(R.id.edit_btn);
        newsDelete = itemView.findViewById(R.id.delete_btn);
        progressBar = itemView.findViewById(R.id.loading);

    }

    public void setItemClickListener(PostItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }

}
