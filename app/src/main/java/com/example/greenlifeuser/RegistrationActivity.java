package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationActivity extends BaseActivity {

    private ImageView userPhoto;
    private static final int PRE_REQ_CODE = 1;
    private static final int REQUEST_CODE = 1;
    private Uri pickedImgUri;

    private TextView loginHere, showBtn, showBtn1, hideBtn, hideBtn1;
    private EditText userFullName, userEmail, userPhone, userPassword, userConfirmPassword;
    private Button registerBtn;
    private ProgressBar loadingProgress;

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String userID;

    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize views
        userFullName = findViewById(R.id.edit_text_fullname);
        userEmail = findViewById(R.id.edit_text_email);
        userPassword = findViewById(R.id.edit_text_password);
        userConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        registerBtn = findViewById(R.id.btn_register);
        loadingProgress = findViewById(R.id.regProgressBar);
        userPhoto = findViewById(R.id.regUserPhoto);
        loginHere = findViewById(R.id.text_view_login);

        // Set click listener for login
        loginHere.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        // Hide progress bar initially
        loadingProgress.setVisibility(View.INVISIBLE);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Register button click listener
        registerBtn.setOnClickListener(view -> {
            loadingProgress.setVisibility(View.VISIBLE);
            final String email = userEmail.getText().toString().trim();
            final String password = userPassword.getText().toString().trim();
            final String password2 = userConfirmPassword.getText().toString().trim();
            final String name = userFullName.getText().toString().trim();

            // Validate the input fields
            if (!isInputValid(email, name, password, password2)) {
                loadingProgress.setVisibility(View.INVISIBLE);
                return;
            }

            createUserAccount(email, name, password);
        });

        // Photo selection from gallery
        userPhoto.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkAndRequestPermission();
            } else {
                openGallery();
            }
        });

        // Password show/hide toggle setup
        setupPasswordToggle();
    }

    private boolean isInputValid(String email, String name, String password, String password2) {
        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            showMessage("All fields are required.");
            return false;
        }
        if (!isEmailValid(email)) {
            showMessage("Please enter a valid email.");
            return false;
        }
        if (!password.equals(password2)) {
            showMessage("Passwords do not match.");
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        ).matcher(email).matches();
    }

    // Function to create a new user account
    private void createUserAccount(String email, String name, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showMessage("Account created successfully");
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("user_profile").document(userID);

                        // Create a user profile map
                        Map<String, Object> userProfile = new HashMap<>();
                        userProfile.put("Name", name);
                        userProfile.put("Email", email);
                        documentReference.set(userProfile)
                                .addOnSuccessListener(aVoid -> {
                                    showMessage("Registration Complete");
                                    loadingProgress.setVisibility(View.INVISIBLE);
                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                    loadingProgress.setVisibility(View.INVISIBLE);
                                });

                        // Upload user photo if available
                        if (pickedImgUri != null) {
                            uploadUserImage(name, pickedImgUri, mAuth.getCurrentUser());
                        } else {
                            updateUserInfoWithoutPhoto(name, mAuth.getCurrentUser());
                        }
                    } else {
                        showMessage("Account creation failed: " + task.getException().getMessage());
                        loadingProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    // Function to upload image and update user profile
    private void uploadUserImage(String name, Uri pickedImgUri, FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();

                // Update user profile with name and image
                currentUser.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showMessage("Registration Complete");
                        DocumentReference documentReference = fStore.collection("user_profile").document(userID);
                        documentReference.update("imageUrl", uri.toString());
                        updateUI();
                    }
                });
            });
        }).addOnFailureListener(e -> {
            showMessage("Image upload failed: " + e.getMessage());
            loadingProgress.setVisibility(View.INVISIBLE);
        });
    }

    private void updateUserInfoWithoutPhoto(String name, FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profileUpdate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showMessage("Registration Complete");
                updateUI();
            }
        });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this,
                    Manifest.permission.READ_MEDIA_IMAGES)) {
                Toast.makeText(RegistrationActivity.this, "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegistrationActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PRE_REQ_CODE);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            pickedImgUri = data.getData();
            userPhoto.setImageURI(pickedImgUri);
        }
    }

    private void setupPasswordToggle() {
        // Show/Hide password functionality
        showBtn = findViewById(R.id.showTV1);
        hideBtn = findViewById(R.id.hideTV1);
        showBtn.setOnClickListener(view -> {
            userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            userConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showBtn.setVisibility(View.GONE);
            hideBtn.setVisibility(View.VISIBLE);
        });
        hideBtn.setOnClickListener(view -> {
            userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            userConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideBtn.setVisibility(View.GONE);
            showBtn.setVisibility(View.VISIBLE);
        });
    }
}
