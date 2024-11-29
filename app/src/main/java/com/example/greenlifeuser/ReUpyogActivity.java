package com.example.greenlifeuser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenlifeuser.adapters.OrganicManureAdapter;
import com.example.greenlifeuser.models.OrganicManurePost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReUpyogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrganicManureAdapter adapter;
    private List<OrganicManurePost> organicManureList;

    private Dialog uploadDialog;
    private ImageView newsImg,back;  // Declare it globally to reuse properly
    private Uri imageUri;  // Store the image URI here
    private final int PICK_IMAGE_REQUEST = 71;  // Image request code
    // Firebase references
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_upyog);
            back=findViewById(R.id.back);
        // Initialize Firebase references
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("organicmanure");  // Storage path
        databaseReference = FirebaseDatabase.getInstance().getReference("organic_manure");  // Database path

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.cropsRecyclerView);  // Ensure you have this in your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        organicManureList = new ArrayList<>();
        adapter = new OrganicManureAdapter(organicManureList, this);
        recyclerView.setAdapter(adapter);

        loadOrganicManurePosts();  // Load posts from Firebase

        // Initialize the upload button
        FloatingActionButton uploadBtn = findViewById(R.id.ogbtn);

        // Set click listener to open dialog
        uploadBtn.setOnClickListener(view -> openUploadDialog());
        back.setOnClickListener(view -> {
            startActivity(new Intent(ReUpyogActivity.this,HomeActivity.class));
            finish();
        });
    }

    private void loadOrganicManurePosts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                organicManureList.clear();  // Clear the list before adding new items
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrganicManurePost post = snapshot.getValue(OrganicManurePost.class);
                    if (post != null) {
                        organicManureList.add(post);  // Add each post to the list
                    }
                }
                adapter.notifyDataSetChanged();  // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Method to open the upload dialog
    private void openUploadDialog() {
        uploadDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        uploadDialog.setContentView(R.layout.organic_manure);  // Use the dialog layout

        // Initialize views inside the dialog
        EditText postTitle = uploadDialog.findViewById(R.id.og_post);
        EditText postDesc = uploadDialog.findViewById(R.id.og_desc);
        EditText postAddress = uploadDialog.findViewById(R.id.og_address);
        EditText postNumber = uploadDialog.findViewById(R.id.og_number);
        newsImg = uploadDialog.findViewById(R.id.news_img);  // Initialize ImageView
        ImageView popupAdd = uploadDialog.findViewById(R.id.popup_add);
        ProgressBar popupProgressBar = uploadDialog.findViewById(R.id.popup_progressBar);

        // Set click listener to choose an image
        newsImg.setOnClickListener(view -> chooseImage());

        // Set click listener to upload the data
        popupAdd.setOnClickListener(view -> {
            if (imageUri != null) {
                uploadImage(postTitle.getText().toString(), postDesc.getText().toString(),
                        postAddress.getText().toString(), postNumber.getText().toString(),
                        popupProgressBar);
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });

        uploadDialog.show();  // Show the dialog
    }

    // Method to open the image picker
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();  // Store the selected image URI

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                if (newsImg != null) {
                    newsImg.setImageBitmap(bitmap);  // Update the ImageView inside the dialog
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to upload the image and data to Firebase
    private void uploadImage(String title, String desc, String address, String number, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        // Generate a unique ID for the image
        String imageId = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child(imageId);
        // user profile add in database

        // Upload the image to Firebase Storage
        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();  // Get the download URL
                    saveImageData(title, desc, address, number, imageUrl, progressBar);
                }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to save image data to Firebase Realtime Database
    private void saveImageData(String title, String desc, String address, String number, String imageUrl, ProgressBar progressBar) {
        String postId = databaseReference.push().getKey();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String currentuser=mAuth.getCurrentUser().getDisplayName();
        String userprofile= String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
        OrganicManurePost post = new OrganicManurePost(postId, title, desc, address, number, imageUrl,currentuser,userprofile);

        if (postId != null) {
            databaseReference.child(postId).setValue(post)
                    .addOnSuccessListener(aVoid -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        uploadDialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
