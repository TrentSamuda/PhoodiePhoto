package com.fte.feedthecause;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class FeedACT extends AppCompatActivity {
    Button btnPost;
    Button btnFeed;
    Button btnProfile;
    Spinner items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_act);



        addButtonListeners();

    }



    public void addButtonListeners(){

        btnPost = (Button)findViewById(R.id.btnPost);

        btnFeed = (Button)findViewById(R.id.btnGetFeed);
        btnProfile = (Button)findViewById(R.id.btnProfile);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });



        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedACT.this, FeedAct2.class);
                //finish();
                startActivity(intent);

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedACT.this, OwnProfile.class);
                //finish();
                startActivity(intent);

            }
        });
    }




    //Intent to open the camera
    private void openCamera() {
        //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, 0);

        Intent intent = new Intent(FeedACT.this, ImageViewer2.class);//    ImageViewer.class);
        //Intent intent = new Intent(FeedACT.this, ImageForm4.class);//    ImageViewer.class);
        startActivity(intent);
    }


}
