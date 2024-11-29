package com.example.greenlifeuser;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenlifeuser.adapters.OrganicManureAdapter;
import com.example.greenlifeuser.models.OrganicManurePost;
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

public class Animalsell extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrganicManureAdapter adapter;
    private List<OrganicManurePost> organicManureList;

    private Dialog uploadDialog;
    private ImageView newsImg,back;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 71;

    // Firebase references
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animalsell);
        back=findViewById(R.id.back);

        // Initialize Firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("animalphoto");
        databaseReference = FirebaseDatabase.getInstance().getReference("animalsell");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.animal_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        organicManureList = new ArrayList<>();
        adapter = new OrganicManureAdapter(organicManureList, this);
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(view -> {

            startActivity(new Intent(Animalsell.this, HomeActivity.class));
            finish();
        });
        loadOrganicManurePosts();

        // Initialize the upload button
        Button uploadBtn = findViewById(R.id.animal_btn);
        uploadBtn.setOnClickListener(view -> openUploadDialog());

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
        );
    }

    private void loadOrganicManurePosts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                organicManureList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrganicManurePost post = snapshot.getValue(OrganicManurePost.class);
                    if (post != null) {
                        organicManureList.add(post);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Animalsell.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUploadDialog() {
        uploadDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        uploadDialog.setContentView(R.layout.organic_manure);

        EditText postTitle = uploadDialog.findViewById(R.id.og_post);
        EditText postDesc = uploadDialog.findViewById(R.id.og_desc);
        EditText postAddress = uploadDialog.findViewById(R.id.og_address);
        EditText postNumber = uploadDialog.findViewById(R.id.og_number);
        newsImg = uploadDialog.findViewById(R.id.news_img);
        ImageView popupAdd = uploadDialog.findViewById(R.id.popup_add);
        ProgressBar popupProgressBar = uploadDialog.findViewById(R.id.popup_progressBar);

        newsImg.setOnClickListener(view -> chooseImage());
        popupAdd.setOnClickListener(view -> {
            if (imageUri != null) {
                uploadImage(
                        postTitle.getText().toString(),
                        postDesc.getText().toString(),
                        postAddress.getText().toString(),
                        postNumber.getText().toString(),
                        popupProgressBar
                );
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });

        uploadDialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                newsImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String title, String desc, String address, String number, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        String imageId = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child(imageId);

        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveImageData(title, desc, address, number, imageUrl, progressBar);
                }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageData(String title, String desc, String address, String number, String imageUrl, ProgressBar progressBar) {
        String postId = databaseReference.push().getKey();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String currentUserName = currentUser != null ? currentUser.getDisplayName() : "Anonymous";
        String userProfileUrl = currentUser != null ? String.valueOf(currentUser.getPhotoUrl()) : "";

        OrganicManurePost post = new OrganicManurePost(postId, title, desc, address, number, imageUrl, currentUserName, userProfileUrl);

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
