package com.example.greenlifeuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GovernmentSchemeActivity extends AppCompatActivity {

    LinearLayout eNam, sammanYojana, vikasYojana, fasalBima, sinchayeeVojaya, mandhanYojana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_scheme);

        eNam = findViewById(R.id.enam);
        sammanYojana = findViewById(R.id.samman);
        vikasYojana = findViewById(R.id.vikas);
        fasalBima = findViewById(R.id.faslBima);
        sinchayeeVojaya = findViewById(R.id.sinchayee);
        mandhanYojana = findViewById(R.id.mandhan);

        eNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.enam.gov.in/web/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        sammanYojana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://pmkisan.gov.in/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        vikasYojana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://byjus.com/free-ias-prep/paramparagat-krishi-vikas-yojana/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        fasalBima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://pmfby.gov.in/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        sinchayeeVojaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://pmksy.gov.in/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mandhanYojana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://maandhan.in/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}