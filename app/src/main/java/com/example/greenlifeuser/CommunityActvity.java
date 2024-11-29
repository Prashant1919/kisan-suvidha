package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.greenlifeuser.adapters.PostAdapter;
import com.example.greenlifeuser.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityActvity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    RelativeLayout contentView;

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;

    Dialog popAddPost;
    CircleImageView popupUserImage;
    TextView popupUserName;
    ImageView popupPostImage, popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private RecyclerView postRecyclerView;
    private DatabaseReference postRef;
    private LinearLayout postProgressBar;

    static final float END_SCALE = 0.7f;
    ImageView menuIccon;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_actvity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        menuIccon = findViewById(R.id.menu_icon);
        //drawer hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        //navigation drawer profile image
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText(mAuth.getCurrentUser().getDisplayName());
        CircleImageView userImage = headerView.findViewById(R.id.user_image);
        Glide.with(CommunityActvity.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(userImage);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        userEmail.setText(mAuth.getCurrentUser().getEmail());

        navigationDrawer();

        //init popup
        iniPopup();

        setupPopupImageClick();

        Button uploadPost = findViewById(R.id.btn_public_post);
        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.community);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                            CommunityActvity.this.startActivity(new Intent(CommunityActvity.this,
                                    HomeActivity.class));
                            CommunityActvity.this.finish();
                            CommunityActvity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.detection) {
                            CommunityActvity.this.startActivity(new Intent(CommunityActvity.this,
                                    MainActivity.class));
                            CommunityActvity.this.finish();
                            CommunityActvity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.products) {
                            CommunityActvity.this.startActivity(new Intent(CommunityActvity.this,
                                    ProductActivity.class));
                            CommunityActvity.this.finish();
                            CommunityActvity.this.overridePendingTransition(0, 0);
                            return true;
                        } else if (itemId == R.id.community) {
                            return true;
                        }
                        return false;
                    }
                });

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postRecyclerView= findViewById(R.id.post_recyclerView);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        postProgressBar = findViewById(R.id.crops_progree_bar);
        postProgressBar.setVisibility(View.VISIBLE);

        postsList();

    }

    private void postsList() {
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postRef, Post.class)
                .build();
        FirebaseRecyclerAdapter<Post, PostAdapter> postAdapter = new FirebaseRecyclerAdapter<Post,
                PostAdapter>(options) {
            @NonNull
            @Override
            public PostAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent,
                        false);
                PostAdapter holder = new PostAdapter(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull PostAdapter holder, final int position, @NonNull final Post model) {

                postProgressBar.setVisibility(View.GONE);
                holder.tvTitle.setText(model.getTitle());
                holder.tvUsername.setText(model.getUserName());
                Glide.with(CommunityActvity.this).load(model.getPicture()).into(holder.imgPost);

                String userImg = model.getUserPhoto();
                if (userImg != null)
                {
                    Glide.with(CommunityActvity.this).load(userImg).into(holder.imgPostProfile);
                    Glide.with(CommunityActvity.this)
                            .load(userImg)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                   // holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                   // holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(holder.imgPostProfile);

                }
                else {
                    Glide.with(CommunityActvity.this).load(R.mipmap.ic_launcher).into(holder.imgPostProfile);

                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent postDetailActivity = new Intent(CommunityActvity.this, PostDetailsActivity.class);

                        postDetailActivity.putExtra("title",model.getTitle());
                        postDetailActivity.putExtra("userName", model.getUserName());
                        postDetailActivity.putExtra("postImage",model.getPicture());
                        postDetailActivity.putExtra("description",model.getDescription());
                        postDetailActivity.putExtra("postKey",model.getPostKey());
                        postDetailActivity.putExtra("userPhoto",model.getUserPhoto());

                        // will fix this later i forgot to add user name to post object
                        //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                        long timestamp  = (long) model.getTimeStamp();
                        postDetailActivity.putExtra("postDate",timestamp) ;
                        startActivity(postDetailActivity);
                    }
                });
            }
        };

        postRecyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
                // we did this before in register activity I'm just going to copy the code to save time ...

                checkAndRequestForPermission();


            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(CommunityActvity.this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CommunityActvity.this,
                    Manifest.permission.READ_MEDIA_IMAGES)) {

                Toast.makeText(CommunityActvity.this, "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(CommunityActvity.this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PReqCode);
            }

        } else
            // everything goes well : we have permission to access user gallery
            openGallery();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    // when user picked an image ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            popupPostImage.setImageURI(pickedImgUri);

        }


    }

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupUserName = popAddPost.findViewById(R.id.pop_up_ser_name);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        // load Current user profile photo
        if (currentUser.getPhotoUrl() != null)
        {
            popupUserName.setText(currentUser.getDisplayName());
            Glide.with(CommunityActvity.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        }
        else {
            popupUserName.setText(currentUser.getDisplayName());
            Glide.with(CommunityActvity.this).load(R.mipmap.ic_launcher).into(popupUserImage);

        }

        //Add post click listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();

                                    if (currentUser.getPhotoUrl() != null) {
                                        Post post = new Post(popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                imageDownloadLink,
                                                currentUser.getUid(),
                                                currentUser.getPhotoUrl().toString(),
                                                currentUser.getDisplayName());

                                        //add post to firebase
                                        addPost(post);
                                    } else {
                                        Post post = new Post(popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                imageDownloadLink,
                                                currentUser.getUid(),
                                                null,
                                                currentUser.getDisplayName());

                                        //add post to firebase
                                        addPost(post);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture

                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                } else {
                    showMessage("Please verify all input fields and choose Post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }

            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and upadte post key
        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });
    }

    private void showMessage(String s) {

        Toast.makeText(CommunityActvity.this, s, Toast.LENGTH_LONG).show();
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
        } else if(itemId == R.id.nav_machinery){
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
            Intent intent2 = new Intent(CommunityActvity.this, LoginActivity.class);
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