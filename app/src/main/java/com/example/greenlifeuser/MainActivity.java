package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RelativeLayout contentView, pests;
    Button detectBtn;
    static final float END_SCALE = 0.7f;
    ImageView menuIccon;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        menuIccon = findViewById(R.id.menu_icon);
        //drawer hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //navigation drawer profile image
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText(mAuth.getCurrentUser().getDisplayName());
        CircleImageView userImage = headerView.findViewById(R.id.user_image);
        Glide.with(MainActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(userImage);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        userEmail.setText(mAuth.getCurrentUser().getEmail());

        navigationDrawer();

        detectBtn = (Button) findViewById(R.id.detectBtn);



        startActivties();

        pests = findViewById(R.id.pests);
        pests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PestsActivity.class));
            }
        });

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.detection);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                            MainActivity.this.startActivity(new Intent(MainActivity.this,
                                    HomeActivity.class));
                            MainActivity.this.finish();
                            MainActivity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.detection) {
                            return true;
                        } else if (itemId == R.id.products) {
                            MainActivity.this.startActivity(new Intent(MainActivity.this,
                                    ProductActivity.class));
                            MainActivity.this.finish();
                            MainActivity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.community) {
                            MainActivity.this.startActivity(new Intent(MainActivity.this,
                                    CommunityActvity.class));
                            MainActivity.this.finish();
                            MainActivity.this.overridePendingTransition(0, 0);
                            return true;
                        }
                        return false;
                    }
                });

    }




    private void startActivties() {
        detectBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, DetectionActivity.class));
        });
    }




    //Navigation Drawer Function
    private void navigationDrawer() {

        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIccon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        // drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else if (itemId == R.id.nav_soil_card) {
            startActivity(new Intent(getApplicationContext(), SoilHealthActivity.class));
            finish();
        } else if (itemId == R.id.nav_crop_prediction) {
            startActivity(new Intent(getApplicationContext(), CropPredictionActivity.class));
            finish();
        } else if (itemId == R.id.nav_crop_analysis) {
            startActivity(new Intent(getApplicationContext(), CropAnalysisActivity.class));
            finish();
        } else if (itemId == R.id.nav_crop_disease) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else if (itemId == R.id.nav_reupyog) {
            startActivity(new Intent(getApplicationContext(), ReUpyogActivity.class));
            finish();
        } else if (itemId == R.id.nav_govt_scheme) {
            startActivity(new Intent(getApplicationContext(), GovernmentSchemeActivity.class));
            finish();
        } else if (itemId == R.id.nav_product) {
            startActivity(new Intent(getApplicationContext(), ProductActivity.class));
            finish();
        } else if (itemId == R.id.nav_contactUs) {
            startActivity(new Intent(getApplicationContext(), ContactUs.class));
            finish();
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent2);
            finish();
        }
        else if(itemId == R.id.nav_machinery){
            Log.d("Hii","Hello");
            startActivity(new Intent(getApplicationContext(), ProductActivity.class));

            finish();
        }
        else if(itemId == R.id.nav_expert){
            Log.d("Hii","Hello");
            startActivity(new Intent(getApplicationContext(), ProductActivity.class));
            finish();
        }
        else if(itemId == R.id.nav_livestock){
            Log.d("Hii","Hello");
            startActivity(new Intent(getApplicationContext(), ProductActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}