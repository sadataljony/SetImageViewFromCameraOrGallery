package com.sadataljony.app.android.setimageviewfromcameraorgallery;

/**
 * this project is a modified version of Tasnuva Tabassum oshin's Volley_Image_Upload project in github
 * for original source please visit https://github.com/TasnuvaOshin/Volley_Image_Upload
 **/

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private Button mBtnCaptureFromCamera, mBtnChooseFromGallery;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    private void initUi() {
        mImageView = findViewById(R.id.imageView);
        mBtnCaptureFromCamera = findViewById(R.id.btnCamera);
        mBtnChooseFromGallery = findViewById(R.id.btnGallery);
        mBtnCaptureFromCamera.setOnClickListener(this);
        mBtnChooseFromGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnCaptureFromCamera) {
            if (CheckPermission()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        }
        if (view == mBtnChooseFromGallery) {
            if (CheckPermission()) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        }
    }

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                                startActivity(new Intent(MainActivity
                                        .this, MainActivity.class));
                                MainActivity.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    mImageView.setImageBitmap(bitmap);
                }
            }
            break;

            case 1: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        mImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
