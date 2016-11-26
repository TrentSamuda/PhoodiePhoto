package com.fte.feedthecause;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;

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

/*
        gridView = (GridView)findViewById(R.id.gridview);
        photoAdapter = new PhotoAdapter(this, mThumbIds);
        gridView.setAdapter(photoAdapter);
*/
    }




        class GetJSON extends AsyncTask<String, Void, String>{
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
                //ArrayList<ImageBreakdown> holder = new ArrayList<>();
                parseJSONString(s);//where the parsing is being done

                ImageView here = (ImageView) findViewById(R.id.imgLook);
                /*
                String base =imageHolder.get(8).getForBitmap();
                base = base.replaceAll("\\\\","");
                String base2 = "";

                byte[] decodedString = Base64.decode(imageHolder.get(7).getForBitmap(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                here.setImageBitmap(bmp);*/
            }
        }






    public void parseJSONString(String jString){
        jString = jString.replaceAll("\\[","");
        jString = jString.replaceAll("\\]","");
        jString = jString.replaceAll("\\{","");
        jString = jString.replaceAll("\\}","");
        jString = jString.replaceAll("\"","");
        jString = jString.replaceAll("result","");
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
            if(key.equals("likes"))
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
