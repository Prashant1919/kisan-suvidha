//package com.example.greenlifeuser.detectionActivities
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Matrix
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.Gravity
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import com.example.greenlifeuser.Classifier
//import com.example.greenlifeuser.R
//import kotlinx.android.synthetic.main.activity_mango_detection.*
//import kotlinx.android.synthetic.main.custom_dailog.view.*
//import java.io.IOException
//
//class MangoDetection : AppCompatActivity() {
//
//    private lateinit var mClassifier: Classifier
//    private lateinit var mBitmap: Bitmap
//
//    private val mCameraRequestCode = 0
//    private val mGalleryRequestCode = 2
//
//    private val mInputSize = 224
//    private val mModelPath = "model_mango.tflite"
//    private val mLabelPath = "mango_labels.txt"
//    private val mSamplePath = "diagram.png"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_mango_detection)
//
//        mClassifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
//
//        resources.assets.open(mSamplePath).use {
//            mBitmap = BitmapFactory.decodeStream(it)
//            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
//            mPhotoImageView.setImageBitmap(mBitmap)
//        }
//
//        mCameraButton.setOnClickListener {
//            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(callCameraIntent, mCameraRequestCode)
//        }
//
//        mGalleryButton.setOnClickListener {
//            val callGalleryIntent = Intent(Intent.ACTION_PICK)
//            callGalleryIntent.type = "image/*"
//            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
//        }
//        mDetectButton.setOnClickListener {
//            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
//            mResultTextView.text= results?.title
//            //+"\n Confidence:"+results?.confidence
//
//            mShowPrecaution.alpha = 1.0f
//            mResultTextView.alpha = 1.0f
//            detectAs.alpha = 1.0f
//            demoTxt.alpha = 0.0f
//        }
//
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == mCameraRequestCode){
//            //Considérons le cas de la caméra annulée
//            if(resultCode == Activity.RESULT_OK && data != null) {
//                mBitmap = data.extras!!.get("data") as Bitmap
//                mBitmap = scaleImage(mBitmap)
//                val toast = Toast.makeText(this, ("Image crop to: w= ${mBitmap.width} h= ${mBitmap.height}"), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.BOTTOM, 0, 20)
//                toast.show()
//                mPhotoImageView.setImageBitmap(mBitmap)
//                mResultTextView.text= "Your photo image set now."
//                mDetectButton.alpha = 1.0f
//                demoTxt.alpha = 0.0f
//                mResultTextView.alpha = 1.0f
//            } else {
//                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show()
//            }
//        } else if(requestCode == mGalleryRequestCode) {
//            if (data != null) {
//                val uri = data.data
//
//                try {
//                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//                println("Success!!!")
//                mBitmap = scaleImage(mBitmap)
//                mPhotoImageView.setImageBitmap(mBitmap)
//                mDetectButton.alpha = 1.0f
//                demoTxt.alpha = 0.0f
//                mResultTextView.alpha = 1.0f
//
//            }
//        } else {
//            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()
//
//        }
//    }
//
//
//    fun scaleImage(bitmap: Bitmap?): Bitmap {
//        val orignalWidth = bitmap!!.width
//        val originalHeight = bitmap.height
//        val scaleWidth = mInputSize.toFloat() / orignalWidth
//        val scaleHeight = mInputSize.toFloat() / originalHeight
//        val matrix = Matrix()
//        matrix.postScale(scaleWidth, scaleHeight)
//        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true)
//    }
//
//    fun precaution(view: View) {
//        val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
//
//        when (results?.title) {
//            "Mango leaf twisting weevil" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_w, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango White Scale" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_ws, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Red Wax Scale" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_rws, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Cyberoptics Kenya" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_ck, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Breadfruit Whitefly" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_bw, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Seychelles Scale" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_ss, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Shoot Borer" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_sb, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Neomelicharia Sparsa" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_ns, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//            "Mango Healthy" -> {
//                val view = View.inflate(this@MangoDetection, R.layout.mango_healthy, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//
//            else -> {
//                //Toast.makeText(this, "Nothing", Toast.LENGTH_SHORT).show()
//                val view = View.inflate(this@MangoDetection, R.layout.custom_dailog, null)
//                val builder = AlertDialog.Builder(this@MangoDetection)
//                builder.setView(view)
//
//                val dialog = builder.create()
//                dialog.show()
//                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                view.got_it_btn.setOnClickListener {
//                    dialog.dismiss()
//                }
//            }
//        }
//    }
//
//}