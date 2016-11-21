package com.fte.feedthecause;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class ImageViewer2 extends AppCompatActivity {

    //Button reTake;
    Button confirm;
    private Bitmap bp;
    private String uriString;
    private String keys = "";
    private Thread tCloudVis;



    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    //private static final String CLOUD_VISION_API_KEY = "AIzaSyDPIsTqtuwe_F13eK7ykfxg0ND59Ijqn0g";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyC1WzZNuchOq7051bl5UBPefdxOfjfVIMc";
    //private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG = "";
    public static final String FILE_NAME = "temp.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer2);


        //reTake = (Button) findViewById(R.id.btnRetakeShot);
        confirm = (Button) findViewById(R.id.btnConfirm);

            /*reTake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCamera();
                }
            });*/

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    StartForm();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



        if(savedInstanceState == null) {
            startCamera();

        }

    }
/*
    private void startCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
        startActivityForResult(intent, 0);
    }
*/

    public void startCamera() {
      /*  if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
        startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        //}
    }

    private void StartForm() throws InterruptedException {
        Intent intent = new Intent(ImageViewer2.this, ImageForm4.class);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        //intent.putExtra("BitmapArray", byteArray);
        intent.putExtra("uriString", uriString);
        //if(keys == null) keys = "N/A";
        //tCloudVis.join();
        intent.putExtra("keywords", keys);
        finish();
        startActivity(intent);
    }

    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }



/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            bp = (Bitmap) data.getExtras().get("data");
            StartForm();
        }
    }
*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uploadImage(Uri.fromFile(getCameraFile()));

        }
        try {
            StartForm();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                //uriToBitmap(uri);//converts uri into bitmap unmodified
                final Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);
                bp = bitmap;
                uriString = uri.toString();
                //callCloudVision(bitmap);
                //mMainImage.setImageBitmap(bitmap);
                /*new Thread("google vision")  {
                    public void run() {

                        try {
                            CloudVisionInfo call = new CloudVisionInfo(bitmap);
                            call.callCloudVision(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };*/
                //keys = call.getResult();
                keys = "food";

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            bp = BitmapFactory.decodeFileDescriptor(fileDescriptor);


            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
