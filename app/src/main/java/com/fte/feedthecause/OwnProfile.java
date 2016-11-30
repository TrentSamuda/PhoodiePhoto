package com.fte.feedthecause;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class OwnProfile extends AppCompatActivity {

    private String
            _picUrl,
            _fname,
            _lName,
            _email,
            _date_created,
            _username;

    private Bitmap bp;

    private ImageView ownImage;

    private TextView
            username,
            fname,
            email;

    private Button logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ownImage = (ImageView) findViewById(R.id.imgOwnProfile);

        username = (TextView) findViewById(R.id.txtUsername);
        fname = (TextView) findViewById(R.id.txtPName);
        email = (TextView) findViewById(R.id.txtPEmail);

        logout = (Button) findViewById(R.id.btnLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnProfile.this, LoginActivity2.class);
                finish();
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });

                //postPostGRESQL();
                new GetProfileJSON().execute();
            }
        }).start();


    }

    private void populateImage(){
        try {//creates the bitmap for the image
            //bp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(_picUrl));
            bp = BitmapFactory.decodeStream(new URL(_picUrl).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bp!=null)
            ownImage.setImageBitmap(bp);
        }

    class GetImage  extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String hold = "";
                if(!_picUrl.startsWith("http://"))
                    hold = "http://" + _picUrl;
                else
                hold = _picUrl;
                URL url = new URL(hold);
                //URL url = new URL("https://s20.postimg.org/b4wjhyut9/trenttanamana.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                bp = myBitmap;

            } catch (IOException e) {
                // Log exception
                return null;
            }
            return "";
        }
        protected void onPostExecute(String s){
            ownImage.setImageBitmap(bp);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    class GetProfileJSON extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(FeedAct2.this, "Please Wait...",null,true,true);
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                //URL url = new URL(uri);
                String myUsername = new Controller().getUserId();
                URL url = new URL("http://foodpickready.co.nf/getprofile.php" + "?username=" + myUsername);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();


                int responseCode = con.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    con.getInputStream()));
                    StringBuffer sb2 = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }

            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ArrayList<ImageBreakdown> holder = new ArrayList<>();
            parseJSONStringProfile(s);//where the parsing is being done

        }
    }


    public void parseJSONStringProfile(String jString) {
        jString = jString.replaceAll("\\[", "");
        jString = jString.replaceAll("\\]", "");
        jString = jString.replaceAll("\\{", "");
        jString = jString.replaceAll("\\}", "");
        jString = jString.replaceAll("\"", "");
        jString = jString.replaceAll("result", "");
        jString = jString.replaceAll("\\\\", "");
        int chop = jString.indexOf(":");
        jString = jString.substring(chop + 1);

        String hold[] = jString.split(",");

        ImageBreakdown me = null;

        for (int i = 0; i < hold.length; i++) {
            String split2[] = hold[i].split(":");

            String key = "", value = "";
            try {
                key = split2[0];
                value = split2[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Array is out of Bounds" + e);
            }


            if (split2.length > 2) {
                value += ":";
                for (int j = 2; j < split2.length; j++)
                    value += split2[j] + ":";
            }

            if (key.equals("photo_id")) {
                //me = new ImageBreakdown(getApplicationContext());
            }
            switch (key) {
                case "username":
                    _username = value;
                    break;
                case "fname":
                    _fname = value;
                    break;
                case "lname":
                    _lName = value;
                    break;
                case "image":
                    _picUrl = value;
                    if(_picUrl.endsWith(":"))
                        _picUrl = _picUrl.substring(0, _picUrl.length()-1);
                    _picUrl = _picUrl.replace("www.","");
                    break;
                case "email":
                    _email = value;
                    break;

            }

        }
        /*
        bp = getBitmapFromURL(_picUrl);
        if(bp!=null)
            ownImage.setImageBitmap(bp);
        */
        //populateImage();

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });

                //postPostGRESQL();
                new GetImage().execute();
            }
        }).start();

        populateRest();

    }

    private void populateRest() {
        username.setText("Username: "+ " " + _username);
        fname.setText("Name:" + _fname + " " + _lName);
        email.setText("Email: "+ _email);
    }
}
