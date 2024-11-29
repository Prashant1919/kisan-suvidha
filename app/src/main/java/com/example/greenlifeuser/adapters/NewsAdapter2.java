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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewsAdapter2 extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView newsTitle, newsSource, newsPrice, likeText;
    public ImageView newsImage, newsShare, likeImage;
    public ProgressBar progressBar;
    public PostItemClickListener listener;
    private final Context context;
    DatabaseReference likeReference;

    public NewsAdapter2(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        newsTitle = itemView.findViewById(R.id.news_title);
        newsSource = itemView.findViewById(R.id.news_source);
        newsPrice = itemView.findViewById(R.id.news_price);
        newsImage = itemView.findViewById(R.id.news_image);
        newsShare = itemView.findViewById(R.id.news_share);
        progressBar = itemView.findViewById(R.id.loading);
        likeImage = itemView.findViewById(R.id.like_btn);
        likeText = itemView.findViewById(R.id.like_text);

    }

    public void getLikeButtonStatus(final String postKey, final String userId){
        likeReference = FirebaseDatabase.getInstance().getReference("likes");
        likeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(userId)){
                    int likeCount = (int) snapshot.child(postKey).getChildrenCount();
                    likeText.setText(likeCount+" likes");
                    likeImage.setImageResource(R.drawable.fav_fill);
                }else {
                    int likeCount = (int) snapshot.child(postKey).getChildrenCount();
                    likeText.setText(likeCount+" likes");
                    likeImage.setImageResource(R.drawable.ic_round_favorite_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setItemClickListener(PostItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }

}
