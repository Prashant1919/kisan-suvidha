package com.example.greenlifeuser.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenlifeuser.R;
import com.example.greenlifeuser.interfaces.PostItemClickListener;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name;
    public CircleImageView image;
    public PostItemClickListener listener;
    private final Context context;


    public CropViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView)itemView.findViewById(R.id.crop_name);
        image = (CircleImageView) itemView.findViewById(R.id.crop_image);
    }

    public void setItemClickListener(PostItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
