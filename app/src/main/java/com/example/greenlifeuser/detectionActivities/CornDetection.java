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
import java.io.InputStream;

public class CornDetection extends AppCompatActivity {

    private Classifier mClassifier;
    private Bitmap mBitmap;

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 2;

    private static final int INPUT_SIZE = 224;
    private static final String MODEL_PATH = "model_corn.tflite";
    private static final String LABEL_PATH = "corn_labels.txt";
    private static final String SAMPLE_IMAGE = "diagram.png";

    private ImageView mPhotoImageView;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corn_detection);

        mPhotoImageView = findViewById(R.id.mPhotoImageView);
        mResultTextView = findViewById(R.id.mResultTextView);

        // Initialize the classifier
        mClassifier = new Classifier(getAssets(), MODEL_PATH, LABEL_PATH, INPUT_SIZE);

        // Load sample image
        try (InputStream inputStream = getAssets().open(SAMPLE_IMAGE)) {
            mBitmap = BitmapFactory.decodeStream(inputStream);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, INPUT_SIZE, INPUT_SIZE, true);
            mPhotoImageView.setImageBitmap(mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Camera button click listener
        findViewById(R.id.mCameraButton).setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        });

        // Gallery button click listener
        findViewById(R.id.mGalleryButton).setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        // Detect button click listener
        findViewById(R.id.mDetectButton).setOnClickListener(v -> {
            Classifier.Recognition result = mClassifier.recognizeImage(mBitmap).get(0);
            mResultTextView.setText(result.getTitle());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mBitmap = (Bitmap) data.getExtras().get("data");
            mBitmap = scaleImage(mBitmap);
            Toast toast = Toast.makeText(this, "Image resized: w=" + mBitmap.getWidth() + " h=" + mBitmap.getHeight(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 20);
            toast.show();
            mPhotoImageView.setImageBitmap(mBitmap);
        } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                mBitmap = scaleImage(mBitmap);
                mPhotoImageView.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Unrecognized request code or cancelled action", Toast.LENGTH_LONG).show();
        }
    }

    // Method to scale the bitmap to the input size
    private Bitmap scaleImage(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = ((float) INPUT_SIZE) / originalWidth;
        float scaleHeight = ((float) INPUT_SIZE) / originalHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }

    // Method to display precaution dialogs based on the detection result
    public void showPrecaution(View view) {
        Classifier.Recognition result = mClassifier.recognizeImage(mBitmap).get(0);
        String title = result.getTitle();

        int layout;
        switch (title) {
            case "Corn Cercospora leaf spot":
                layout = R.layout.corn_leaf_spot;
                break;
            case "Corn Common Rust":
                layout = R.layout.corn_common_rust;
                break;
            case "Corn Northern Leaf Blight":
                layout = R.layout.corn_northern_leaf_blight;
                break;
            case "Corn Healthy":
                layout = R.layout.corn_healthy;
                break;
            default:
                layout = R.layout.custom_dailog;
                break;
        }

        View dialogView = View.inflate(this, layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogView.findViewById(R.id.got_it_btn).setOnClickListener(v -> dialog.dismiss());
    }
}

