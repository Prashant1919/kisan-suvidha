package com.example.greenlifeuser;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        // Apply the saved language on every activity start
        loadLocale();
    }

    protected void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String languageCode = prefs.getString("My_Lang", "en"); // Default to English

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
