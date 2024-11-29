package com.example.greenlifeuser;

import com.example.greenlifeuser.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greenlifeuser.adapters.NewsAdapter2;
import com.example.greenlifeuser.models.Post2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    RelativeLayout contentView;

    private RecyclerView newsRecyclerView;
    private DatabaseReference newsRef;
    LinearLayout newsProgressBar;

    String PostKey;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference likeReference;
    Boolean isLike = false;

    static final float END_SCALE = 0.7f;
    ImageView menuIccon;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menuIccon = findViewById(R.id.menu_icon);
        //drawer hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //

        //navigation drawer profile image
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText(mAuth.getCurrentUser().getDisplayName());
        CircleImageView userImage = headerView.findViewById(R.id.user_image);
        Glide.with(ProductActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(userImage);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        userEmail.setText(mAuth.getCurrentUser().getEmail());

        navigationDrawer();

        likeReference = FirebaseDatabase.getInstance().getReference("likes");

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.products);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                            ProductActivity.this.startActivity(new Intent(ProductActivity.this,
                                    HomeActivity.class));
                            ProductActivity.this.finish();
                            ProductActivity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.detection) {
                            ProductActivity.this.startActivity(new Intent(ProductActivity.this,
                                    MainActivity.class));
                            ProductActivity.this.finish();
                            ProductActivity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.products) {
                            return true;
                        } else if (itemId == R.id.community) {
                            ProductActivity.this.startActivity(new Intent(ProductActivity.this,
                                    CommunityActvity.class));
                            ProductActivity.this.finish();
                            ProductActivity.this.overridePendingTransition(0, 0);
                            return true;
                        }
                        return false;
                    }
                });

        newsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        newsRecyclerView = findViewById(R.id.productRecyclerView);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        newsProgressBar = findViewById(R.id.crops_progree_bar);
        newsProgressBar.setVisibility(View.VISIBLE);

        newsList();

    }



    private void newsList() {

        FirebaseRecyclerOptions<Post2> options = new FirebaseRecyclerOptions.Builder<Post2>()
                .setQuery(newsRef, Post2.class)
                .build();
        FirebaseRecyclerAdapter<Post2, NewsAdapter2> postAdapter = new FirebaseRecyclerAdapter<Post2,
                NewsAdapter2>(options) {
            @NonNull
            @Override
            public NewsAdapter2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent,
                        false);
                NewsAdapter2 holder = new NewsAdapter2(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull NewsAdapter2 holder, final int position, @NonNull final Post2 model) {

                newsProgressBar.setVisibility(View.GONE);
                holder.newsTitle.setText(model.getTitle());
                holder.newsSource.setText(" "+model.getSource());
                holder.newsPrice.setText("₹ "+model.getPrice());

                Glide.with(ProductActivity.this).load(model.getPicture()).into(holder.newsImage);

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = firebaseUser.getUid();
                String postKey = getRef(position).getKey();

                holder.getLikeButtonStatus(postKey, userId);

                holder.likeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isLike = true;

                        likeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (isLike == true){
                                    if (snapshot.child(postKey).hasChild(userId)){

                                        likeReference.child(postKey).removeValue();
                                        isLike = false;

                                    }else {
                                        likeReference.child(postKey).child(userId).setValue(true);
                                        isLike = false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent postDetailActivity = new Intent(ProductActivity.this, ProductDetailsActivity.class);

                        postDetailActivity.putExtra("title", model.getTitle());
                        postDetailActivity.putExtra("postImage", model.getPicture());
                        postDetailActivity.putExtra("description", model.getDescription());
                        postDetailActivity.putExtra("source", model.getSource());
                        postDetailActivity.putExtra("price", model.getPrice());
                        postDetailActivity.putExtra("number", model.getNumber());
                        postDetailActivity.putExtra("postKey", model.getPostKey());

                        // will fix this later i forgot to add user name to post object
                        //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                        long timestamp = (long) model.getTimeStamp();
                        postDetailActivity.putExtra("postDate", timestamp);
                        startActivity(postDetailActivity);
                    }
                });

                String pTitle = model.getTitle();
                holder.newsShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.newsImage.getDrawable();
                        if (bitmapDrawable == null) {
                            shareTextOnly(pTitle);
                        } else {
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            shareImageAndText(pTitle, bitmap);
                        }
                    }
                });

            }
        };

        newsRecyclerView.setAdapter(postAdapter);
        postAdapter.startListening();

    }

    private void shareImageAndText(String pTitle, Bitmap bitmap) {
        String shareBody = pTitle;
        Uri uri = saveImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share Via"));

    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.example.greenlifeuser.fileprovider", file);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private void shareTextOnly(String pTitle) {
        String shareBody = pTitle;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
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


    @SuppressLint("NonConstantResourceId")
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
        }
        else if(itemId == R.id.nav_machinery){
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
            Intent intent2 = new Intent(ProductActivity.this, LoginActivity.class);
            startActivity(intent2);
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