package com.example.greenlifeuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView newsImage;
    TextView newsTitle,newsDateName,newsSource, newsPrice, newsDescription;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        newsImage = findViewById(R.id.news_detail_img);
        newsTitle = findViewById(R.id.news_detail_title);
        newsDateName = findViewById(R.id.news_detail_date_name);
        newsSource = findViewById(R.id.news_detail_source);
        newsPrice = findViewById(R.id.news_detail_price);
        newsDescription = findViewById(R.id.news_detail_desc);

        String contactNumber = getIntent().getExtras().getString("number");

        String postImage = getIntent().getExtras().getString("postImage") ;
        Glide.with(this).load(postImage).into(newsImage);

        String postTitle = getIntent().getExtras().getString("title");
        newsTitle.setText(postTitle);

        String postDescription = getIntent().getExtras().getString("description");
        newsDescription.setText(postDescription);

        String postSource = getIntent().getExtras().getString("source");
        newsSource.setText(postSource);

        String postPrice = getIntent().getExtras().getString("price");
        newsPrice.setText(postPrice);

        // get post id
        PostKey = getIntent().getExtras().getString("postKey");

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        newsDateName.setText(date);


        Button contactSellerBtn = findViewById(R.id.contactSeller);
        contactSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(ProductDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProductDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                        return;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+91" + contactNumber));
                    startActivity(callIntent);
                } else {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+91" + contactNumber));
                    startActivity(callIntent);
                }
            }
        });


    }
    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }

    public void backToMain(View view) {
        onBackPressed();
    }
}