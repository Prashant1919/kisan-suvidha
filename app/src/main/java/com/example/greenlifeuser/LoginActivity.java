package com.example.greenlifeuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    TextView registerHere, forgotPassword, showBtn, hideBtn;
    ImageView Profile;
    private EditText userEail, userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        registerHere = findViewById(R.id.text_view_register);
        Profile = findViewById(R.id.profile);
        userEail = findViewById(R.id.edit_text_email);
        userPassword = findViewById(R.id.edit_text_password);
        btnLogin = findViewById(R.id.btn_login);
        loginProgress = findViewById(R.id.regProgressBar);
        mAuth = FirebaseAuth.getInstance();
        MainActivity = new Intent(this, HomeActivity.class);

        // Register button click listener
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
            }
        });

        // Forgot password button click listener
        forgotPassword = findViewById(R.id.forget_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        // Initially hide progress bar
        loginProgress.setVisibility(View.INVISIBLE);

        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                final String mail = userEail.getText().toString();
                final String password = userPassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify All Fields");
                    loginProgress.setVisibility(View.INVISIBLE);
                } else {
                    signIn(mail, password);
                }
            }
        });

        // Show/Hide password functionality
        showBtn = findViewById(R.id.showTV);
        hideBtn = findViewById(R.id.hideTV);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showBtn.setVisibility(View.GONE);
                hideBtn.setVisibility(View.VISIBLE);
            }
        });

        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showBtn.setVisibility(View.VISIBLE);
                hideBtn.setVisibility(View.GONE);
            }
        });
    }

    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);

                    // Get the current user
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        fetchUserProfile(user.getUid());

                    } else {
                        updateUI();
                    }
                } else {
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void fetchUserProfile(String userId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fStore.collection("user_profile").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the image URL from Firestore
                        String imageUrl = document.getString("imageUrl");

                        // Load the image using Glide
                        if (imageUrl != null) {
                            Glide.with(LoginActivity.this)
                                    .load(imageUrl)
                                    .into(Profile);  // Set the ImageView to the profile picture
                        }
                        updateUI();
                    }
                }
            }
        });
    }

    private void updateUI() {
        startActivity(MainActivity);
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is already connected, redirect to home page
            updateUI();
        }
    }
}
