package com.example.greenlifeuser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class languageselection extends BaseActivity {

    private CardView cardHindi, cardEnglish;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge experience
        EdgeToEdge.enable(this);

        // Load the saved language preference and set content view
        setContentView(R.layout.activity_languageselection);

        // Adjust view padding for insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize card views
        cardHindi = findViewById(R.id.card_hindi);
        cardEnglish = findViewById(R.id.card_english);


        // Set click listeners for language selection
        cardHindi.setOnClickListener(view -> {
            cardEnglish.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ggreen));
            setLanguage("hi");
            startMainActivity();
        });

//        cardMarathi.setOnClickListener(view -> {
//            setLanguage("mr");
//            startMainActivity();
//        });

        cardEnglish.setOnClickListener(view -> {
            setLanguage("en");
            cardEnglish.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ggreen));
            startMainActivity();
        });
    }

    private void setLanguage(String languageCode) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", languageCode);
        editor.apply();

        // Show a toast message to confirm language change
        Toast.makeText(this, "Language set to " + languageCode, Toast.LENGTH_SHORT).show();

        // Restart the activity to apply the new language
        recreate();
    }

    private void startMainActivity() {
        Intent intent = new Intent(languageselection.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
