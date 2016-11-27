package com.fte.feedthecause;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FeedACT extends AppCompatActivity {
    Button btnPost;
    Button btnSignOut;
    Button btnFeed;
    Button btnProfile;
    Spinner items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_act);


        items = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();
        list.add("options");
        list.add("Settings");
        list.add("Logout");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        items.setAdapter(dataAdapter);
        //items.setOnItemClickListener(this.onItemSelected(this.getParent(), ););

        addButtonListeners();

    }

    public void addListenerOnSpinnerItemSelection(){

        items.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addButtonListeners(){

        btnPost = (Button)findViewById(R.id.btnPost);
        btnSignOut = (Button)findViewById(R.id.btnSignOut);
        btnFeed = (Button)findViewById(R.id.btnGetFeed);
        btnProfile = (Button)findViewById(R.id.btnProfile);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedACT.this, LoginActivity2.class);
                finish();
                startActivity(intent);

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


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        switch (item){
            case "Logout":
                Intent intent = new Intent(FeedACT.this, LoginActivity.class);
                finish();
                startActivity(intent);
                break;
            case "Settings":
                Intent intentSettings = new Intent(FeedACT.this, LoginActivity.class);
                finish();
                startActivity(intentSettings);
                break;
        }
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
