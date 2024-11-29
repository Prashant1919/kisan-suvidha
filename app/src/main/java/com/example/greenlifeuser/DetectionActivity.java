package com.example.greenlifeuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.greenlifeuser.detectionActivities.AppleDetection;
import com.example.greenlifeuser.detectionActivities.BananaDetection;
import com.example.greenlifeuser.detectionActivities.CitrusDetection;
import com.example.greenlifeuser.detectionActivities.CornDetection;
import com.example.greenlifeuser.detectionActivities.GrapeDetection;
import com.example.greenlifeuser.detectionActivities.MangoDetection;
import com.example.greenlifeuser.detectionActivities.PapayaDetection;
//import com.example.greenlifeuser.detectionActivities.ChilliDetection;
//import com.example.greenlifeuser.detectionActivities.CitrusDetection;
//import com.example.greenlifeuser.detectionActivities.CornDetection;
//import com.example.greenlifeuser.detectionActivities.CottonDetection;
//import com.example.greenlifeuser.detectionActivities.GrapeDetection;
//import com.example.greenlifeuser.detectionActivities.MangoDetection;
//import com.example.greenlifeuser.detectionActivities.PapayaDetection;
//import com.example.greenlifeuser.detectionActivities.PotatoDetection;
//import com.example.greenlifeuser.detectionActivities.RiceDetection;
//import com.example.greenlifeuser.detectionActivities.RoseDetection;
//import com.example.greenlifeuser.detectionActivities.TeaDetection;
//import com.example.greenlifeuser.detectionActivities.TomatoDetection;

public class DetectionActivity extends BaseActivity {

    CardView appleCard, bananaCard, chilliCard, citrusCard, cornCard, cottonCard, grapeCard, mangoCard,
            papayaCard, potatoCard, riceCard, teaCard, tomatoCard, roseCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        appleCard = (CardView) findViewById(R.id.appleCard);
        bananaCard = (CardView) findViewById(R.id.bananaCard);
        chilliCard = (CardView) findViewById(R.id.chilliCard);
        citrusCard = (CardView) findViewById(R.id.citrusCard);
        cornCard = (CardView) findViewById(R.id.maizeCard);
        cottonCard = (CardView) findViewById(R.id.cottonCard);
        grapeCard = (CardView) findViewById(R.id.grapesCard);
        mangoCard = (CardView) findViewById(R.id.mangoCard);
        papayaCard = (CardView) findViewById(R.id.papayaCard);
        potatoCard = (CardView) findViewById(R.id.potatoCard);
        riceCard = (CardView) findViewById(R.id.riceCard);
        teaCard = (CardView) findViewById(R.id.teaCard);
        tomatoCard = (CardView) findViewById(R.id.tomatoCard);
        roseCard = (CardView) findViewById(R.id.roseCard);

        startActivties();

    }
    private void startActivties() {
//        detectBtn.setOnClickListener(view -> {
//            startActivity(new Intent(DetectionActivity.this, TensorActivity.class));
//        });

        appleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, AppleDetection.class));
            }
        });
        bananaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, BananaDetection.class));
            }
        });
//        chilliCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, ChilliDetection.class));
//            }
//        });
        citrusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, CitrusDetection.class));
            }
        });
        cornCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, CornDetection.class));
            }
        });
//        cottonCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, CottonDetection.class));
//            }
//        });
        grapeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, GrapeDetection.class));
            }
        });
        papayaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, PapayaDetection.class));
            }
        });
        mangoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectionActivity.this, MangoDetection.class));
            }
        });
//        potatoCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, PotatoDetection.class));
//            }
//        });
//        riceCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, RiceDetection.class));
//            }
//        });
//        teaCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, TeaDetection.class));
//            }
//        });
//        tomatoCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, TomatoDetection.class));
//            }
//        });
//
//        roseCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetectionActivity.this, RoseDetection.class));
//            }
//        });

    }
    
}