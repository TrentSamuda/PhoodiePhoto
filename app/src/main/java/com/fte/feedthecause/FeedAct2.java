package com.fte.feedthecause;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FeedAct2 extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {


    private List<ImageBreakdown> listFoodPics;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_act2);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listFoodPics = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data
        getData();

        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new CardAdapter(listFoodPics, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


/*
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
*/
/*
        gridView = (GridView)findViewById(R.id.gridview);
        photoAdapter = new PhotoAdapter(this, mThumbIds);
        gridView.setAdapter(photoAdapter);
*/
    }


    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(FeedAct2.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }


    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            ImageBreakdown foodPic = new ImageBreakdown();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                //superHero.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                foodPic.setTitle(json.getString(Config.TAG_TITLE));
                foodPic.setDescription(json.getString(Config.TAG_DESCRIPTION));
                foodPic.setKeywords(json.getString(Config.TAG_KEYWORDS));
                foodPic.setIngredients(json.getString(Config.TAG_INGREDIENTS));
                foodPic.setInstructions(json.getString(Config.TAG_INSTRUCTIONS));
                foodPic.setOther(json.getString(Config.TAG_OTHER));
                foodPic.setTimePosted(json.getString(Config.TAG_DATE_CREATED));
                foodPic.setOwner(json.getString(Config.TAG_OWNER));
                foodPic.setImageURL(json.getString(Config.TAG_IMAGE_URL));
                foodPic.setLikeCounter(Integer.parseInt(json.getString(Config.TAG_LIKE_COUNTER)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the ImageBreakdown object to the list
            listFoodPics.add(foodPic);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }


    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getData();
        }
    }








/*
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
    /*
            }
        }
*/




/*
    public void parseJSONString(String jString){
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
               //me = new ImageBreakdown(getApplicationContext());
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
                //imageHolder.add(me);
        }

    }
*/





}
