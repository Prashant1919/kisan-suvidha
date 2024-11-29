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
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greenlifeuser.Classifier;
import com.example.greenlifeuser.R;

import java.io.IOException;
import java.io.InputStream;

public class AppleDetection extends AppCompatActivity {

    private Classifier mClassifier;
    private Bitmap mBitmap;

    private final int mCameraRequestCode = 0;
    private final int mGalleryRequestCode = 2;

    private final int mInputSize = 224;
    private final String mModelPath = "model_apple.tflite";
    private final String mLabelPath = "apple_labels.txt";
    private final String mSamplePath = "diagram.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apple_detection);

        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);

        try (InputStream is = getAssets().open(mSamplePath)) {
            mBitmap = BitmapFactory.decodeStream(is);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((ImageView) findViewById(R.id.mPhotoImageView)).setImageBitmap(mBitmap);

        findViewById(R.id.mCameraButton).setOnClickListener(view -> {
            Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(callCameraIntent, mCameraRequestCode);
        });

        findViewById(R.id.mGalleryButton).setOnClickListener(view -> {
            Intent callGalleryIntent = new Intent(Intent.ACTION_PICK);
            callGalleryIntent.setType("image/*");
            startActivityForResult(callGalleryIntent, mGalleryRequestCode);
        });

        findViewById(R.id.mDetectButton).setOnClickListener(view -> {
            Classifier.Recognition results = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                results = mClassifier.recognizeImage(mBitmap).stream().findFirst().orElse(null);
            }
            ((android.widget.TextView) findViewById(R.id.mResultTextView)).setText(results != null ? results.getTitle() : null);

            findViewById(R.id.mShowPrecaution).setAlpha(1.0f);
            findViewById(R.id.mResultTextView).setAlpha(1.0f);
            findViewById(R.id.detectAs).setAlpha(1.0f);
            findViewById(R.id.demoTxt).setAlpha(0.0f);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = (Bitmap) data.getExtras().get("data");
                mBitmap = scaleImage(mBitmap);

                Toast toast = Toast.makeText(this, "Image crop to: w= " + mBitmap.getWidth() + " h= " + mBitmap.getHeight(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 20);
                toast.show();

                ((ImageView) findViewById(R.id.mPhotoImageView)).setImageBitmap(mBitmap);
                ((android.widget.TextView) findViewById(R.id.mResultTextView)).setText("Your photo image set now.");
                findViewById(R.id.mDetectButton).setAlpha(1.0f);
                findViewById(R.id.demoTxt).setAlpha(0.0f);
                findViewById(R.id.mResultTextView).setAlpha(1.0f);
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == mGalleryRequestCode) {
            if (data != null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mBitmap = scaleImage(mBitmap);
                ((ImageView) findViewById(R.id.mPhotoImageView)).setImageBitmap(mBitmap);
                findViewById(R.id.mDetectButton).setAlpha(1.0f);
                findViewById(R.id.demoTxt).setAlpha(0.0f);
                findViewById(R.id.mResultTextView).setAlpha(1.0f);
            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap scaleImage(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = ((float) mInputSize) / originalWidth;
        float scaleHeight = ((float) mInputSize) / originalHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }

    public void precaution(View view) {
        Classifier.Recognition results = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            results = mClassifier.recognizeImage(mBitmap).stream().findFirst().orElse(null);
        }
        String title = results != null ? results.getTitle() : null;

        AlertDialog.Builder builder;
        View dialogView;
        switch (title != null ? title : "") {
            case "Apple Scab":
                dialogView = View.inflate(this, R.layout.apple_scab, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                break;
            case "Apple Black Root":
                dialogView = View.inflate(this, R.layout.apple_black_rot, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                break;
            case "Apple Cedar Apple":
                dialogView = View.inflate(this, R.layout.apple_cedar_rust, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                break;
            case "Apple Healthy":
                dialogView = View.inflate(this, R.layout.apply_healthy, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                break;
            default:
                dialogView = View.inflate(this, R.layout.custom_dailog, null);
                builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                break;
        }

        AlertDialog dialog = builder.create();
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogView.findViewById(R.id.got_it_btn).setOnClickListener(v -> dialog.dismiss());
    }
}
