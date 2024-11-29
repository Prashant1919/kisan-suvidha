package com.example.greenlifeuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greenlifeuser.R;
import com.example.greenlifeuser.models.OrganicManurePost;
import com.example.greenlifeuser.organic_productdetails;

import java.util.List;

public class OrganicManureAdapter extends RecyclerView.Adapter<OrganicManureAdapter.OrganicManureViewHolder> {

    private List<OrganicManurePost> organicManureList;
    private Context context;

    public OrganicManureAdapter(List<OrganicManurePost> organicManureList, Context context) {
        this.organicManureList = organicManureList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrganicManureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.organicposts, parent, false);
        return new OrganicManureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganicManureViewHolder holder, int position) {
        OrganicManurePost post = organicManureList.get(position);
        holder.ogi_title.setText(post.getTitle());
       // holder.ogi_info.setText(post.getDescription());
        holder.ogi_address.setText(post.getAddress());
       // holder.ogi_contact.setText(post.getNumber());
        //Profile Load
        Glide.with(context)
                .load(post.getProfile())
                .placeholder(R.drawable.profile) // Placeholder image
                .into(holder.profile);
        //Profile name
        holder.name.setText(post.getName());
        // Load the image using Glide
        Glide.with(context)
                .load(post.getImageUrl())
                .placeholder(R.drawable.kishna_smart_large_logo) // Placeholder image
                .into(holder.news_image);
        // Set an item click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, organic_productdetails.class);
            intent.putExtra("title", post.getTitle());
            intent.putExtra("description", post.getDesc());
            intent.putExtra("imageUrl", post.getImageUrl());
            intent.putExtra("number",post.getNumber());
            intent.putExtra("address",post.getAddress());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return organicManureList.size();
    }

    static class OrganicManureViewHolder extends RecyclerView.ViewHolder {
        TextView ogi_title, ogi_address,name;
        ImageView news_image,profile;

        public OrganicManureViewHolder(@NonNull View itemView) {
            super(itemView);
            ogi_title = itemView.findViewById(R.id.ogi_title);
            ogi_address = itemView.findViewById(R.id.ogi_address);

            news_image = itemView.findViewById(R.id.news_image);
            name=itemView.findViewById(R.id.user_id);
            profile=itemView.findViewById(R.id.profile_image);
        }
    }
}

