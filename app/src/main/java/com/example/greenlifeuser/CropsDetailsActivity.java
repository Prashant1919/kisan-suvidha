package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.greenlifeuser.models.CropModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropsDetailsActivity extends AppCompatActivity {

    CircleImageView cropImage;
    TextView cropName, nitro, phos, pota, temp, humid, ph, rain;
    private String cropID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops_details);

        cropID = getIntent().getStringExtra("cid");

        cropImage = findViewById(R.id.crop_image);
        cropName = findViewById(R.id.crop_name);
        nitro = findViewById(R.id.nitrogen);
        phos = findViewById(R.id.phosphorus);
        pota =findViewById(R.id.potassium);
        temp = findViewById(R.id.temperature);
        humid = findViewById(R.id.humidity);
        ph = findViewById(R.id.pH);
        rain = findViewById(R.id.rainfall);

        getCropDetails(cropID);

    }
    private void getCropDetails(String cropID) {

        DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference().child("crops");

        cropRef.child(cropID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    CropModel CropModel = dataSnapshot.getValue(CropModel.class);

                    cropName.setText(CropModel.getName());
                    nitro.setText(CropModel.getNitrogen()+" kg/acre");
                    phos.setText(CropModel.getPhosphorus()+" kg/acre");
                    pota.setText(CropModel.getPotassium()+" kg/acre");
                    temp.setText(CropModel.getTemperature()+" °C");
                    humid.setText(CropModel.getHumidity()+" %");
                    ph.setText(CropModel.getpH());
                    rain.setText(CropModel.getRainfall()+" cm");
                    //Picasso.get().load(CropModel.getmImg()).into(mainImageView);
                    Glide.with(CropsDetailsActivity.this).load(CropModel.getImage()).into(cropImage);
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