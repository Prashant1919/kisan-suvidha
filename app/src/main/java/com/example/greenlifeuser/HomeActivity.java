package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenlifeuser.interfaces.APIInterface;
import com.example.greenlifeuser.weatherapi.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    RelativeLayout contentView;
    static final float END_SCALE = 0.7f;
    ImageView menuIccon;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    TextView cityname,date,temperature,min_max;
    ImageView cdservice,equipment,cropdetection,cropanaysis,weather_icon;

    private WebView webView;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "23648509598542aa62b3a873773ae01b";
    private ImageView langChangeIV;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadLocale();
        setContentView(R.layout.activity_home);
        weather_icon=findViewById(R.id.weather_icon);
        cityname=findViewById(R.id.cityname);
        temperature=findViewById(R.id.temperature);
        min_max=findViewById(R.id.min_max);
        date=findViewById(R.id.date);
        langChangeIV = findViewById(R.id.langChange);
        //Access current location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Check location permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
        langChangeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        menuIccon = findViewById(R.id.menu_icon);
        //drawer hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        cdservice=(ImageView) findViewById(R.id.cdservice);
        equipment=findViewById(R.id.equipment);
        cropanaysis=findViewById(R.id.cropanaysis);
        cropdetection=findViewById(R.id.cropdetection);
        //navigation drawer profile image
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText(mAuth.getCurrentUser().getDisplayName());
        CircleImageView userImage = headerView.findViewById(R.id.user_image);
        Glide.with(HomeActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(userImage);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        userEmail.setText(mAuth.getCurrentUser().getEmail());
        Servicesdetails(cdservice,equipment,cropanaysis,cropdetection);
        // Initialize the WebView
        //webView = findViewById(R.id.webview);

        // Enable JavaScript if needed
       // WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to handle page navigation within the WebView
       // webView.setWebViewClient(new WebViewClient());

        // Load a URL
       // webView.loadUrl("https://www.krushikranti.com/bajarbhav"); // Replace with your desired URL
        navigationDrawer();

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                            return true;
                        } else if (itemId == R.id.detection) {
                            HomeActivity.this.startActivity(new Intent(HomeActivity.this,
                                    MainActivity.class));
                            HomeActivity.this.finish();
                            HomeActivity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.products) {
                            HomeActivity.this.startActivity(new Intent(HomeActivity.this,
                                    ProductActivity.class));
                            HomeActivity.this.finish();
                            HomeActivity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.community) {
                            HomeActivity.this.startActivity(new Intent(HomeActivity.this,
                                    CommunityActvity.class));
                            HomeActivity.this.finish();
                            HomeActivity.this.overridePendingTransition(0, 0);
                            return true;
                        }
                        return false;
                    }
                });

    }



    private void Servicesdetails(ImageView cdservice, ImageView equipment, ImageView cropanaysis, ImageView cropdetection) {
        cdservice.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Animalsell.class));
            finish();
        });
        equipment.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), uploadimage.class));
            finish();
        });
        cropanaysis.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), CropPredictionActivity.class));
                finish();
        });
        cropdetection.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), CropPredictionActivity.class));
            finish();
        });
    }


    // Method to show language selection dialog
    private void showLanguageDialog() {
        String[] languages = {"English", "Hindi"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    switch (which) {
                        case 0: // English
                            setLocale("en");
                            break;
                        case 1: // Hindi
                            setLocale("hi");
                            break;
                    }
                })
                .show();
    }

    // Method to change app language
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("My_Lang", languageCode);
        editor.apply();

        recreate(); // Restart activity to apply changes
    }

    // Load saved language from SharedPreferences
//    private void loadLocale() {
//        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
//        String language = preferences.getString("My_Lang", "");
//        setLocale(language); // Set the locale based on the saved language
//    }
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
            startActivity(new Intent(getApplicationContext(), uploadimage.class));
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
        }  else if(itemId == R.id.nav_machinery){
            startActivity(new Intent(getApplicationContext(), Dailymarket.class));


            finish();
        }
        else if(itemId == R.id.nav_expert){

            startActivity(new Intent(getApplicationContext(), CommunityActvity.class));
            finish();


        }
        else if(itemId == R.id.nav_livestock){
            startActivity(new Intent(getApplicationContext(), Animalsell.class));
            finish();
        }

        else if (itemId == R.id.nav_contactUs) {
            startActivity(new Intent(getApplicationContext(), ContactUs.class));
            finish();
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent2);
            finish();
        }
        return true;
    }
    //Weather fetching

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            try {
                                Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    String cityName = addresses.get(0).getLocality();
                                    cityname.setText(cityName);
                                    Toast.makeText(HomeActivity.this, "City: " + cityName, Toast.LENGTH_SHORT).show();
                                    fetchWeatherData(cityName);
                                } else {
                                    Toast.makeText(HomeActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void fetchWeatherData(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<WeatherResponse> call = apiInterface.getWeatherData(cityName, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    fetchWeatherData("Pune");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(HomeActivity.this, "Failed to fetch data", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateUI(WeatherResponse weatherData) {
        temperature.setText(weatherData.main.temp + " °C");
        date.setText(getCurrentDate());
        cityname.setText("Pune");
        min_max.setText("Max "
                + (int) weatherData.main.temp_min + "°C / "
                + (int) weatherData.main.temp_max + "°C");
        String condition = "unknown"; // Default value
        if (weatherData != null && weatherData.weather != null && !weatherData.weather.isEmpty()) {
            condition = weatherData.weather.get(0).main;
        }
        changeweather(condition);
    }
    private void changeweather(String condition) {
            switch (condition) {
                case "clear Sky":
                case "Sunny":
                case "Clear":
                    weather_icon.setBackgroundResource(R.drawable.sunrise);
                    break;

                case "partly Clouds":
                case "Clouds":
                case "Overcast":
                case "Mist":
                case "Foggy":
                case "Smoke":
                    weather_icon.setBackgroundResource(R.drawable.cloud);
                    break;

                case "Light rain":
                case "Drizzle":
                case "Moderate Rain":
                case "Showers":
                case "Heavy Rain":
                    weather_icon.setBackgroundResource(R.drawable.heavyrain);
                    break;

                default:
                    weather_icon.setBackgroundResource(R.drawable.sunrise);
                    break;
            }



    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
//current user location
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}