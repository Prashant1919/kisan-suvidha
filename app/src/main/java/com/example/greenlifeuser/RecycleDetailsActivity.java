package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.greenlifeuser.models.CropModel;
import com.example.greenlifeuser.models.RecycleModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecycleDetailsActivity extends AppCompatActivity {

    TextView titleTV, subTV1, desc, subTV2, link1, link2, link3;
    ImageView image;
    private String recycleID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_details);

        recycleID = getIntent().getStringExtra("rid");

        titleTV = findViewById(R.id.recycle_title);
        image = findViewById(R.id.recycle_image);
        subTV1 = findViewById(R.id.subtitle_1);
        subTV2 = findViewById(R.id.subtitle_2);
        desc = findViewById(R.id.description);
        link1 = findViewById(R.id.link_1);
        link2 = findViewById(R.id.link_2);
        link3 = findViewById(R.id.link_3);

        getRecycleDetails(recycleID);
    }

    private void getRecycleDetails(String recycleID) {

        DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference().child("recycle");

        cropRef.child(recycleID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    RecycleModel recycleModel = dataSnapshot.getValue(RecycleModel.class);

                    titleTV.setText(recycleModel.getTitle());
                    subTV1.setText(recycleModel.getSubTitle1());
                    subTV2.setText(recycleModel.getSubTitle2());
                    desc.setText(recycleModel.getDescription());
                    link1.setText(recycleModel.getLink1());
                    link2.setText(recycleModel.getLink2());
                    link3.setText(recycleModel.getLink3());
                    //Picasso.get().load(CropModel.getmImg()).into(mainImageView);
                    Glide.with(RecycleDetailsActivity.this).load(recycleModel.getImage()).into(image);

                    link1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(link1.toString()); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                    link2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(link2.toString()); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                    link3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(link3.toString()); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}