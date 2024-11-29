package com.example.greenlifeuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.greenlifeuser.models.Post2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadimage extends AppCompatActivity {

    private static final int PReqCode = 1;
    private static final int REQUESTCODE = 2;

    private ImageView newsImage;
    private EditText newsTitle, newsDescription, newsPrice, newsNumber, newsSource;
    private ImageView newsAddBtn;
    private ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage); // Your activity layout

        // Initialize views
        newsImage = findViewById(R.id.news_img);
        newsTitle = findViewById(R.id.news_title);
        newsDescription = findViewById(R.id.news_description);
        newsPrice = findViewById(R.id.news_price);
        newsNumber = findViewById(R.id.news_number);
        newsSource = findViewById(R.id.news_source);
        newsAddBtn = findViewById(R.id.popup_add);
        popupClickProgress = findViewById(R.id.popup_progressBar);

        // Firebase user authentication (assumed user is logged in)
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Image click listener
        newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });

        // Add post button click listener
        newsAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(uploadimage.this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(uploadimage.this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PReqCode);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            // The user has successfully picked an image
            pickedImgUri = data.getData();
            newsImage.setImageURI(pickedImgUri);
        }
    }

    private void uploadPost() {
        newsAddBtn.setVisibility(View.INVISIBLE);
        popupClickProgress.setVisibility(View.VISIBLE);

        if (!newsTitle.getText().toString().isEmpty()
                && !newsDescription.getText().toString().isEmpty()
                && !newsSource.getText().toString().isEmpty()
                && !newsNumber.getText().toString().isEmpty()
                && pickedImgUri != null) {

            // Upload the image to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products_images");
            final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());

            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("UploadImage", "Image upload successful");
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadLink = uri.toString();

                            // Create a new Post2 object and save it to Firebase Database
                            Post2 post = new Post2(
                                    newsTitle.getText().toString(),
                                    newsDescription.getText().toString(),
                                    newsSource.getText().toString(),
                                    newsPrice.getText().toString(),
                                    newsNumber.getText().toString(),
                                    imageDownloadLink,
                                    currentUser.getUid(),
                                    (currentUser.getPhotoUrl() != null) ? currentUser.getPhotoUrl().toString() : null,
                                    currentUser.getDisplayName()
                            );

                            addPostToDatabase(post);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Failed to get image download URL: " + e.getMessage());
                            popupClickProgress.setVisibility(View.INVISIBLE);
                            newsAddBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showMessage("Failed to upload image: " + e.getMessage());
                    popupClickProgress.setVisibility(View.INVISIBLE);
                    newsAddBtn.setVisibility(View.VISIBLE);
                }
            });
        } else {
            showMessage("Please verify all input fields and choose Post Image");
            popupClickProgress.setVisibility(View.INVISIBLE);
            newsAddBtn.setVisibility(View.VISIBLE);
        }
    }

    private void addPostToDatabase(Post2 post) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Products").push();
        String key = myRef.getKey();
        post.setPostKey(key); // Ensure your Post2 class has a method to set this.

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Product added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                newsAddBtn.setVisibility(View.VISIBLE);
                resetFields();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Failed to add product: " + e.getMessage());
                popupClickProgress.setVisibility(View.INVISIBLE);
                newsAddBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void resetFields() {
        newsTitle.getText().clear();
        newsSource.getText().clear();
        newsPrice.getText().clear();
        newsDescription.getText().clear();
        newsNumber.getText().clear();
        newsImage.setImageResource(R.drawable.add_post_image); // Reset to default image
    }

    private void showMessage(String message) {
        Toast.makeText(uploadimage.this, message, Toast.LENGTH_LONG).show();
    }
    // Handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(uploadimage.this, HomeActivity.class); // Replace with your home activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish current activity
    }
}
