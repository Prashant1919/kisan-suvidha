package com.example.greenlifeuser.detectionActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greenlifeuser.Classifier;
import com.example.greenlifeuser.R;

import java.io.IOException;

public class MangoDetection extends AppCompatActivity {

    private Classifier mClassifier;
    private Bitmap mBitmap;

    private final int mCameraRequestCode = 0;
    private final int mGalleryRequestCode = 2;

    private final int mInputSize = 224;
    private final String mModelPath = "model_mango.tflite";
    private final String mLabelPath = "mango_labels.txt";
    private final String mSamplePath = "mango_sample.png"; // Update the sample image as needed

    private ImageView mPhotoImageView;
    private TextView mResultTextView;
    private View mCameraButton;
    private View mGalleryButton;
    private View mDetectButton;
    private View mShowPrecaution;
    private TextView detectAs;
    private TextView demoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mango_detection);

        // Initialize views
        mPhotoImageView = findViewById(R.id.mPhotoImageView);
        mResultTextView = findViewById(R.id.mResultTextView);
        mCameraButton = findViewById(R.id.mCameraButton);
        mGalleryButton = findViewById(R.id.mGalleryButton);
        mDetectButton = findViewById(R.id.mDetectButton);
        mShowPrecaution = findViewById(R.id.mShowPrecaution);
        detectAs = findViewById(R.id.detectAs);
        demoTxt = findViewById(R.id.demoTxt);

        // Initialize classifier
        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);

        // Set sample image
        try {
            mBitmap = BitmapFactory.decodeStream(getAssets().open(mSamplePath));
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true);
            mPhotoImageView.setImageBitmap(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set camera button click listener
        mCameraButton.setOnClickListener(view -> {
            Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(callCameraIntent, mCameraRequestCode);
        });

        // Set gallery button click listener
        mGalleryButton.setOnClickListener(view -> {
            Intent callGalleryIntent = new Intent(Intent.ACTION_PICK);
            callGalleryIntent.setType("image/*");
            startActivityForResult(callGalleryIntent, mGalleryRequestCode);
        });

        // Set detect button click listener
        mDetectButton.setOnClickListener(view -> {
            Classifier.Recognition results = mClassifier.recognizeImage(mBitmap).get(0);
            mResultTextView.setText(results.getTitle());
            mShowPrecaution.setAlpha(1.0f);
            mResultTextView.setAlpha(1.0f);
            detectAs.setAlpha(1.0f);
            demoTxt.setAlpha(0.0f);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = (Bitmap) data.getExtras().get("data");
                mBitmap = scaleImage(mBitmap);
                Toast toast = Toast.makeText(this, "Image cropped to: w= " + mBitmap.getWidth() + " h= " + mBitmap.getHeight(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 20);
                toast.show();
                mPhotoImageView.setImageBitmap(mBitmap);
                mResultTextView.setText("Your photo image is set now.");
                mDetectButton.setAlpha(1.0f);
                demoTxt.setAlpha(0.0f);
                mResultTextView.setAlpha(1.0f);
            } else {
                Toast.makeText(this, "Camera canceled..", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == mGalleryRequestCode) {
            if (data != null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    mBitmap = scaleImage(mBitmap);
                    mPhotoImageView.setImageBitmap(mBitmap);
                    mDetectButton.setAlpha(1.0f);
                    demoTxt.setAlpha(0.0f);
                    mResultTextView.setAlpha(1.0f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap scaleImage(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = ((float) mInputSize) / originalWidth;
        float scaleHeight = ((float) mInputSize) / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }

    public void precaution(View view) {
        Classifier.Recognition results = mClassifier.recognizeImage(mBitmap).get(0);

        AlertDialog.Builder builder;
        View customView;
        AlertDialog dialog;

        switch (results.getTitle()) {
            case "Mango Anthracnose":
                customView = View.inflate(this, R.layout.mango_w, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(customView);
                dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customView.findViewById(R.id.got_it_btn).setOnClickListener(v -> dialog.dismiss());
                break;

            case "Mango Bacterial Black Spot":
                customView = View.inflate(this, R.layout.mango_bw, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(customView);
                dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customView.findViewById(R.id.got_it_btn).setOnClickListener(v -> dialog.dismiss());
                break;

            case "Mango Healthy":
                customView = View.inflate(this, R.layout.mango_healthy, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(customView);
                dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customView.findViewById(R.id.got_it_btn).setOnClickListener(v -> dialog.dismiss());
                break;

            default:
                customView = View.inflate(this, R.layout.custom_dailog, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(customView);
                dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customView.findViewById(R.id.got_it_btn).setOnClickListener(v -> dialog.dismiss());
                break;
        }
    }
}
