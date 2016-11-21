package com.fte.feedthecause;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class FeedAct2 extends AppCompatActivity {

    private GridView gridView;
    private PhotoAdapter photoAdapter;

    private ArrayList<ImageBreakdown> imageHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_act2);

        imageHolder = new ArrayList<>();
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });

                //postPostGRESQL();
                new GetJSON().execute();
            }
        }).start();


        gridView = (GridView)findViewById(R.id.gridview);
        photoAdapter = new PhotoAdapter(this, mThumbIds);
        gridView.setAdapter(photoAdapter);

    }



    //private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(FeedAct2.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = "http://www.simplifiedcoding.16mb.com/myjson.php";


                BufferedReader bufferedReader = null;
                try {
                    //URL url = new URL(uri);
                    URL url = new URL("http://foodpickready.co.nf/allpics.php");

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
                ArrayList<ImageBreakdown> holder = new ArrayList<>();
                parseJSONString(s);
               /*
                try {
                    JSONArray junky = new JSONArray(s);

                    for(int i=0 ; i< junky.length(); i++){   // iterate through jsonArray
                        JSONObject jsonObject = junky.getJSONObject(i);  // get jsonObject @ i position
                        System.out.println("jsonObject " + i + ": " + jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }
        }

    //}




    public void parseJSONString(String jString){
        jString = jString.replaceAll("\\[","");
        jString = jString.replaceAll("\\{","");
        jString = jString.replaceAll("result","");
        int chop = jString.indexOf(":");
        jString= jString.substring(chop+1);

        String hold[] = jString.split(",");



        for(int i=0;i<hold.length;i++){
            String split2[] = hold[i].split(":");
            String key = split2[0];
            String value = split2[1];
            ImageBreakdown me = new ImageBreakdown(getApplicationContext());
            switch(key){
                case "title":
                    me.setTitle(value);
                    break;
                case "description":
                    me.setDescription(value);
                    break;
                case "keywords":
                    me.setKeywords(value);
                    break;
                case "ingredients":
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
            imageHolder.add(me);
        }

    }



    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };

    /*
    private [] books = {
            new Book(R.string.abc_an_amazing_alphabet_book, R.string.dr_seuss, R.drawable.abc,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/abc.jpg"),
            new Book(R.string.are_you_my_mother, R.string.dr_seuss, R.drawable.areyoumymother,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/areyoumymother.jpg"),
            new Book(R.string.where_is_babys_belly_button, R.string.karen_katz, R.drawable.whereisbabysbellybutton,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/whereisbabysbellybutton.jpg"),
            new Book(R.string.on_the_night_you_were_born, R.string.nancy_tillman, R.drawable.onthenightyouwereborn,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/onthenightyouwereborn.jpg"),
            new Book(R.string.hand_hand_fingers_thumb, R.string.dr_seuss, R.drawable.handhandfingersthumb,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/handhandfingersthumb.jpg"),
            new Book(R.string.the_very_hungry_caterpillar, R.string.eric_carle, R.drawable.theveryhungrycaterpillar,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/theveryhungrycaterpillar.jpg"),
            new Book(R.string.the_going_to_bed_book, R.string.sandra_boynton, R.drawable.thegoingtobedbook,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/thegoingtobedbook.jpg"),
            new Book(R.string.oh_baby_go_baby, R.string.dr_seuss, R.drawable.ohbabygobaby,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/ohbabygobaby.jpg"),
            new Book(R.string.the_tooth_book, R.string.dr_seuss, R.drawable.thetoothbook,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/thetoothbook.jpg"),
            new Book(R.string.one_fish_two_fish_red_fish_blue_fish, R.string.dr_seuss, R.drawable.onefish,
                    "http://www.raywenderlich.com/wp-content/uploads/2016/03/onefish.jpg")
    };
    */

}
