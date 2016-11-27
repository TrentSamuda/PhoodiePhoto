package com.fte.feedthecause;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class OwnProfile extends AppCompatActivity {

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
                URL url = new URL("http://foodpickready.co.nf/getprofile.php"+ "?username="+ myUsername);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();


                int responseCode=con.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    con.getInputStream()));
                    StringBuffer sb2 = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }

            }catch(Exception e){
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


    public void parseJSONStringProfile(String jString){
        jString = jString.replaceAll("\\[","");
        jString = jString.replaceAll("\\]","");
        jString = jString.replaceAll("\\{","");
        jString = jString.replaceAll("\\}","");
        jString = jString.replaceAll("\"","");
        jString = jString.replaceAll("result","");
        jString = jString.replaceAll("\\\\","");
        int chop = jString.indexOf(":");
        jString= jString.substring(chop+1);

        String hold[] = jString.split(",");

        ImageBreakdown me = null;

        for(int i=0;i<hold.length;i++){
            String split2[] = hold[i].split(":");

            String key="",value="";
            try {
                key = split2[0];
                value = split2[1];
            }catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Array is out of Bounds"+e);
            }


            if(split2.length > 2){
                value += ":";
                for(int j=2;j<split2.length;j++)
                    value += split2[j] + ":";
            }

            if(key.equals("photo_id")) {
                me = new ImageBreakdown(getApplicationContext());
            }
            switch(key){
                case "title":
                    me.setTitle(value);
                    break;
                case "description":
                    me.setDescription(value);
                    break;
                case "keywords":
                    //puts the comas back when its safe
                    value = value.replaceAll("@",",");
                    me.setKeywords(value);
                    break;
                case "ingredients":
                    me.setIngredients(value);
                    break;
                case "instructions":
                    me.setInstructions(value);
                    break;
                case "other":
                    me.setOther(value);
                    break;
                case "date_created":
                    me.setTimePosted(value);
                    break;
                case "owner":
                    me.setOwner(value);
                    break;
                case "image_path":
                    me.setForBitmap(value);
                    break;
                case "photo_id":
                    me.setPhotoID(value);
                    break;
                case "likes":
                    me.setLikeCounter(Integer.parseInt(value));
                    break;
            }
            //if(key.equals("likes"))
                //imageHolder.add(me);
        }

    }

}
