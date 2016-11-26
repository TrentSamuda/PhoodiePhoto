package com.fte.feedthecause;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class ImageForm4 extends AppCompatActivity {


    private String errMessage;
    private TextView txtErrMessage;


    private Button btnCancelPost;
    private Button btnPost;

    private EditTextListener
            txtTitle,
            txtKeywords,
            txtDescription;

    private EditTextListener
            txtIngredients,
            txtInstructions,
            txtOther;

    private String
            _title,
            _keywords,
            _description,
            _ingredients,
            _instructions,
            _other,
            _picture,
            _visionKeys;

    private Bitmap picture;
    private byte[] bArray4Blob;

    //final private String strUrl = "foodappinstance.cxdmekncyujj.us-west-fbfb2.rds.amazonaws.com:5432";
    //final private String strUrl = "ftp://ftp.gear.host/php/allpics.php";
    final private String strUrl = "http://food1.gear.host/php/allpics.php";


    String currentDateTime = getCurrentTimeStamp(); // Find todays date
    Uri passedUri;


    private ProgressDialog prgDialog;
    public static final int progress_bar_type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_form4);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //dumpIntent(getIntent());

        txtErrMessage = (TextView) findViewById(R.id.txtErrorDisplay);
        errMessage = "Please do the following:\n";


        btnCancelPost = (Button) findViewById(R.id.btnCPost);
        btnPost = (Button) findViewById(R.id.btnPost);

        //mandatory EditText
        txtTitle = (EditTextListener) findViewById(R.id.eTxtTitle);
        txtTitle.setPartner((TextView)findViewById(R.id.txtTitle));

        txtKeywords = (EditTextListener) findViewById(R.id.eTxtKeyWords);
        txtKeywords.setPartner((TextView)findViewById(R.id.txtKeyWords));

        txtDescription = (EditTextListener) findViewById(R.id.eTxtDescription);
        txtDescription.setPartner((TextView)findViewById(R.id.txtDescription));


        //the optional EditText
        txtIngredients = (EditTextListener) findViewById(R.id.eTxtIngredients);
        txtIngredients.setPartner((TextView)findViewById(R.id.txtIngredients));

        txtInstructions = (EditTextListener) findViewById(R.id.eTxtInstructions);
        txtInstructions.setPartner((TextView)findViewById(R.id.txtInstructions));

        txtOther = (EditTextListener) findViewById(R.id.ETxtOther);
        txtOther.setPartner((TextView)findViewById(R.id.txtOther));
        String holdUri = getIntent().getStringExtra("uriString");

        passedUri = Uri.parse(holdUri);
        //picture = decompressByteArray();
        //String temp = getIntent().getStringExtra("keywords"); //("keywords");
        _keywords = getIntent().getStringExtra("keywords");
        //_keywords = temp;
        txtKeywords.setText(_keywords);

        _picture = uriToBitmapString();
        //bArray4Blob = uriToBitmapToBlob();
        //Thread tCloud = new Thread(new UseCloudVision());
        //tCloud.start();

        btnCancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postContent();
                //new SendPostRequest().execute();
            }
        });
    }




    private String uriToBitmapString() {
        Bitmap bp;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(passedUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            bp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return BitmapToString(bp);
        } catch (IOException e) {
            e.printStackTrace();
            return "I lost the image";
        }
    }

    private byte[] uriToBitmapToBlob(){
        Bitmap bb;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(passedUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            bb = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            picture = bb;
            parcelFileDescriptor.close();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bb.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "image has been lost",
                    Toast.LENGTH_LONG).show();
            return null;

        }


    }

    /*
    public static String BitmapToString(Bitmap bitmap) {
        String temp=null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap=Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            String file = Base64.encodeToString(data, 0);
            //String image_file = file;
            return file;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            Log.e("OutOfMemory", e.toString());
            return null;
        }

        return temp;
    }
    */


    //creating the potential image
    //new way of doing it
    public String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String temp = null;

        try {

            System.gc();

            temp = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {

            e.printStackTrace();

        } catch (OutOfMemoryError e) {

            baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            b = baos.toByteArray();

            temp = Base64.encodeToString(b, Base64.DEFAULT);
            //temp = "it went to catch";

            Log.e("EWN", "Out of memory error catched");

        }

        return temp;
    }


    public static void dumpIntent(Intent i){

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e("intentent passed:","Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e("intent passed:","[" + key + "=" + bundle.get(key)+"]");
            }
            Log.e("Intent passed:","Dumping Intent end");
        }
    }


    private boolean postContent(){
        if(isAllThere()){

            //gathers all the metadata needed
            _title = txtTitle.getText().toString();
            _description = txtDescription.getText().toString();
            _keywords = txtKeywords.getText().toString();
            _ingredients = txtIngredients.getText().toString();
            _instructions = txtInstructions.getText().toString();
            _other = txtOther.getText().toString();
            if(_ingredients.trim().equals(""))
                _ingredients = "N/A";
            if(_instructions.trim().equals(""))
                _instructions = "N/A";
            if(_other.trim().equals(""))
                _other = "N/A";


            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });

                    //postPostGRESQL();
                    new SendPostRequest().execute();
                }
            }).start();
        }

        else{

            Toast.makeText(getContext4Cloud(), errMessage,
                    Toast.LENGTH_LONG).show();
            errMessage = "Please do the following.\n";

        }
        return true;
    }


    /*Method to check all EditText to ensure they have correct parameters*/
    private boolean isAllThere(){
        boolean allGood = true;
        String cleanTitle = txtTitle.getText().toString().replaceAll("[^A-Za-z]","");
        if(txtTitle.getText().toString().equals("") ){
            allGood = false;
            errMessage += "Title is empty. Please provide a title of at least 5 letters.\n";

        }else if(cleanTitle.length() < 5){
            allGood = false;
            errMessage += "Please provide a title of at least 5 letters.\n";
        }
        if(txtKeywords.getText().toString().trim().equals("")){
            allGood = false;
            errMessage += "Keywords is empty. Please enter related words to the food.\n";
        }
        if(txtDescription.getText().toString().trim().equals("")){
            allGood = false;
            errMessage += "Description is empty. Please provide a description of the food\n";
        }
        return allGood;
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... arg0) {
            try{

                //URL url = new URL("http://studytutorial.in/post.php");

                URL url = new URL("http://foodpickready.co.nf/newpics.php");
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("title", _title);
                postDataParams.put("description", _description);
                _keywords = _keywords.replaceAll(",","@");
                postDataParams.put("keywords", _keywords);
                postDataParams.put("ingredients", _ingredients);
                postDataParams.put("instructions", _instructions);
                postDataParams.put("other", _other);
                //postDataParams.put("date_created", currentDateTime);
                postDataParams.put("date_created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                postDataParams.put("owner", new Controller().getUserId());
                String forBArray =  String.valueOf(bArray4Blob);
                postDataParams.put("image_path", _picture);





                /*
                postDataParams.put("title", _title);
                postDataParams.put("description", _description);
                postDataParams.put("keywords", _keywords);
                postDataParams.put("ingredients", _ingredients);
                postDataParams.put("instructions", _instructions);
                postDataParams.put("other", _other);
                postDataParams.put("picture", _picture);
                postDataParams.put("postTime", currentDateTime);
                postDataParams.put("userid", new Controller().getUserId());
                */


                //postDataParams.put("name", "abc");
                //postDataParams.put("email", "abc@gmail.com");
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
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
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();



            if(result.equals("New record created successfully"))
                finish();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }




    private String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find today's date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }







    // Show Dialog Box with Progress bar
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Downloading Mp3 file. Please wait...");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }




    public Context getContext4Cloud(){
        return getApplicationContext();
    }




    private String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class UseCloudVision implements Runnable{

        @Override
        public void run() {
            try {
                CloudVisionInfo c = new CloudVisionInfo(picture);
                _visionKeys = c.getResult();

                Toast.makeText(getContext4Cloud(), "launched cloud",
                        Toast.LENGTH_LONG).show();

                txtKeywords.setText(_visionKeys);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
