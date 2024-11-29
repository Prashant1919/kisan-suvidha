package com.example.greenlifeuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(SplashActivity.this, languageselection.class));
                finish();
            }
            else {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 2000);


    }
}