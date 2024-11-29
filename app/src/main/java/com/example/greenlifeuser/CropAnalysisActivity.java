package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.greenlifeuser.adapters.CropViewHolder;
import com.example.greenlifeuser.models.CropModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CropAnalysisActivity extends AppCompatActivity {

    private RecyclerView cropsRecyclerView;
    private DatabaseReference cropRef;
    private LinearLayout cropsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_analysis);

        cropRef = FirebaseDatabase.getInstance().getReference().child("crops");
        cropsRecyclerView = findViewById(R.id.cropsRecyclerView);
        cropsRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        cropsRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        cropsProgressBar = findViewById(R.id.crops_progree_bar);
        cropsProgressBar.setVisibility(View.VISIBLE);

        crops();

    }

    private void crops() {
        FirebaseRecyclerOptions<CropModel> options = new FirebaseRecyclerOptions.Builder<CropModel>()
                .setQuery(cropRef, CropModel.class)
                .build();
        FirebaseRecyclerAdapter<CropModel, CropViewHolder> cropAdapter = new FirebaseRecyclerAdapter<CropModel,
                CropViewHolder>(options) {
            @NonNull
            @Override
            public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_item, parent,
                        false);
                CropViewHolder holder = new CropViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CropViewHolder holder, final int position,
                                            @NonNull final CropModel model) {

                cropsProgressBar.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.image);
                //Glide.with(HomeActivity.this).load(model.getImage()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CropAnalysisActivity.this, CropsDetailsActivity.class);
                        intent.putExtra("cid", model.getCid());
                        startActivity(intent);

                    }
                });
            }
        };

        cropsRecyclerView.setAdapter(cropAdapter);
        cropAdapter.startListening();
    }

    public void back(View view) {
        startActivity(new Intent(CropAnalysisActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}