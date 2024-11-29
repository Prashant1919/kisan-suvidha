package com.example.greenlifeuser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;

public class CropPredictionActivity extends AppCompatActivity {
    RadioGroup activeGroup, pulseGroup;
    RadioButton nitro1, nitro2, nitro3;
    RadioButton phospho1, phospho2, phospho3;
    RadioButton pota1, pota2, pota3;
    RadioButton temp1, temp2, temp3;
    RadioButton rain1, rain2, rain3;
    Button calculateBtn;

    TextView resultTV;
    HashMap<String, String> cropMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_prediction);

        // Initialize UI components
        activeGroup = findViewById(R.id.active_group);
        pulseGroup = findViewById(R.id.pulse_group);
        calculateBtn = findViewById(R.id.calculate_btn);
        resultTV = findViewById(R.id.result_TV);

        // Initialize RadioButtons
        nitro1 = findViewById(R.id.n20_60);
        nitro2 = findViewById(R.id.n60_100);
        nitro3 = findViewById(R.id.n100_140);
        phospho1 = findViewById(R.id.ph10_30);
        phospho2 = findViewById(R.id.ph30_50);
        phospho3 = findViewById(R.id.ph50_70);
        pota1 = findViewById(R.id.po10_30);
        pota2 = findViewById(R.id.po30_50);
        pota3 = findViewById(R.id.po50_70);
        temp1 = findViewById(R.id.t10_20);
        temp2 = findViewById(R.id.t20_30);
        temp3 = findViewById(R.id.t30_40);
        rain1 = findViewById(R.id.r100_175);
        rain2 = findViewById(R.id.r175_350);
        rain3 = findViewById(R.id.r350_450);

        // Initialize crop mapping
        cropMap = new HashMap<>();
        initializeCropMap();

        // Set up button click listener
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = getCropResult();
                resultTV.setText(result);
            }
        });
    }

    private void initializeCropMap() {
        cropMap.put("nitro1-phospho1-pota1-temp1-rain1", "Onion");
        cropMap.put("nitro1-phospho1-pota1-temp1-rain2", "No Crop!!\nTry select other options!");
        cropMap.put("nitro1-phospho1-pota1-temp1-rain3", "No Crop!!\nTry select other options!");
        cropMap.put("nitro1-phospho1-pota1-temp2-rain1", "Wheat, Millet, Oilseeds, Tea, Coffee, Chilli");
        cropMap.put("nitro1-phospho1-pota1-temp2-rain2", "Rice, Ginger, Jute");
        cropMap.put("nitro1-phospho1-pota1-temp2-rain3", "No Crop!!\nTry select other options!");
        // Add all other conditions here...
        cropMap.put("nitro1-phospho1-pota3-temp2-rain2", "Barley, Lentils, Sesame");
        cropMap.put("nitro1-phospho2-pota1-temp1-rain1", "Cabbage, Cauliflower, Brinjal");
        cropMap.put("nitro1-phospho2-pota1-temp1-rain2", "No Crop!!\nTry select other options!");
        cropMap.put("nitro1-phospho2-pota1-temp2-rain1", "Tomato, Capsicum");
        cropMap.put("nitro1-phospho2-pota1-temp3-rain2", "Watermelon, Pumpkin");
        cropMap.put("nitro1-phospho2-pota2-temp1-rain3", "No Crop!!\nTry select other options!");
        cropMap.put("nitro2-phospho1-pota1-temp1-rain1", "Ragi, Pearl Millet");
        cropMap.put("nitro2-phospho1-pota2-temp2-rain2", "Chickpea, Sunflower");
        cropMap.put("nitro2-phospho2-pota3-temp1-rain1", "Sorghum, Kidney Bean");
        cropMap.put("nitro2-phospho3-pota1-temp1-rain3", "Pulses (Various types)");
        cropMap.put("nitro3-phospho2-pota1-temp1-rain2", "Bajra, Groundnut");
        cropMap.put("nitro3-phospho2-pota3-temp2-rain1", "Tobacco, Fenugreek");
        cropMap.put("nitro3-phospho3-pota2-temp1-rain2", "No Crop!!\nTry select other options!");
        cropMap.put("nitro3-phospho3-pota3-temp2-rain3", "Barley, Sweet Potato");
        cropMap.put("nitro1-phospho1-pota3-temp2-rain1", "Mango, Papaya, Green Gram");

    }

    private String getCropResult() {
        String nitro = nitro1.isChecked() ? "nitro1" : nitro2.isChecked() ? "nitro2" : "nitro3";
        String phospho = phospho1.isChecked() ? "phospho1" : phospho2.isChecked() ? "phospho2" : "phospho3";
        String pota = pota1.isChecked() ? "pota1" : pota2.isChecked() ? "pota2" : "pota3";
        String temp = temp1.isChecked() ? "temp1" : temp2.isChecked() ? "temp2" : "temp3";
        String rain = rain1.isChecked() ? "rain1" : rain2.isChecked() ? "rain2" : "rain3";

        String key = nitro + "-" + phospho + "-" + pota + "-" + temp + "-" + rain;
        String crop = cropMap.containsKey(key) ? cropMap.get(key) : "No Crop!!\nTry select other options!";
        return crop;

    }


        // Back button method
        public void back (View view){
            startActivity(new Intent(CropPredictionActivity.this, HomeActivity.class));
            finish();
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

