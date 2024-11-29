package com.example.greenlifeuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class organic_productdetails extends AppCompatActivity {

    private ImageView og_imgdetails;
    private TextView og_detailstitle, descript,og_detailscontact,og_address;
    private Button btn_supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organic_productdetails);

        // Initialize views
        og_imgdetails = findViewById(R.id.og_imgdetails);
        og_detailstitle = findViewById(R.id.og_detailstitle);
        descript = findViewById(R.id.descript);
        og_detailscontact=findViewById(R.id.og_detailscontact);
        og_address=findViewById(R.id.og_address);
        btn_supplier=findViewById(R.id.btn_supplier);


        // Get data from the Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String number=getIntent().getStringExtra("number");
        String address=getIntent().getStringExtra("address");

        // Set the data to views
        og_detailstitle.setText(" "+title);
        descript.setText(description);
        og_detailscontact.setText("Contact Number :-"+number);
        og_address.setText(""+address);


        // Load the image using Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(og_imgdetails);
        btn_supplier.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > 22) {

                if (ActivityCompat.checkSelfPermission(organic_productdetails.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(organic_productdetails.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+91" + number));
                startActivity(callIntent);
            } else {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+91" + number));
                startActivity(callIntent);
            }
        });

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
