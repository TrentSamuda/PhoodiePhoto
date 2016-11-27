package com.fte.feedthecause;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class RegisterUser extends AppCompatActivity {

    private EditText
            fName,
            lName,
            email,
            username,
            password,
            cPassword;

    private String
            _fName,
            _lName,
            _email,
            _username,
            _password,
            _cPassword,
            _picValue;

    private Button
            btnCancel,
            btnRegister;

    private boolean registerSuccess;

    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    public static final String FILE_NAME = "temp.jpg";

    private Bitmap bp = null;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
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
        
        fab.setVisibility(View.INVISIBLE);
        fab.setEnabled(false);

        fName = (EditText) findViewById(R.id.eTxtFName);
        lName = (EditText) findViewById(R.id.eTxtLName);
        email = (EditText) findViewById(R.id.eTxtEmail);
        username = (EditText) findViewById(R.id.eTxtUsername);
        password = (EditText) findViewById(R.id.eTxtPassword);
        cPassword = (EditText) findViewById(R.id.eTxtCPassword);

        btnCancel = (Button) findViewById(R.id.btnCregister);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        imgProfile = (ImageView) findViewById(R.id.imgRegPic);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegiser();
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUser.this);
        builder
                //.setMessage(R.string.dialog_select_prompt)
                .setMessage("Take profile picture?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //startCamera();
                    }
                });
        builder.create().show();

    }


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            //uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                bp = setImage(Uri.fromFile(getCameraFile()));
                imgProfile.setImageBitmap(bp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            startCamera();
        }
        /*
        try {
            StartForm();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }

    private Bitmap setImage(Uri uri) throws IOException {
        return scaleBitmapDown(
                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                1200);
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

    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }
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

    private void attemptRegiser() {

        if (isSafe()) {
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });

                    //postPostGRESQL();
                    new RegisterUserTask().execute();
                }
            }).start();
        }
    }

    private boolean isSafe() {
        //check all the the fields
        boolean safe = true;
        String errorMessage = "";
        if(fName.getText().toString().trim().equals("")){
            safe = false;
            errorMessage += "Enter a first name.\n";
        }else{
            _fName = fName.getText().toString();
        }

        if(lName.getText().toString().trim().equals("")){
            safe = false;
            errorMessage += "Enter a last name.\n";
        }else{
            _lName = lName.getText().toString();
        }
        if(!isEmail(email.getText().toString().trim())){
            errorMessage += "Enter a valid email address.\n";
            safe = false;
        }else{
            _email = email.getText().toString();
        }
        if(username.getText().toString().trim().equals("")){
            safe = false;
            errorMessage += "Enter a username\n";
        }else{
            if(username.getText().toString().length() < 5){
                safe = false;
                errorMessage += "Enter a username of at least 5 characters\n";
            }else if(username.getText().toString().contains(" ")){
                safe = false;
                errorMessage += "Enter a username without any spaces\n";
            }else
                _username = username.getText().toString();
        }
        if(!checkPasswords()){
            safe = false;
        }else{
            _password = password.getText().toString();
        }
        if(!safe)
            Toast.makeText(getApplicationContext(), errorMessage,
                    Toast.LENGTH_LONG).show();
        return safe;
    }

    //checking passwords
    private boolean checkPasswords(){
        //check the passwords to equal each other
        //make sure they arent empty
        //make sure at least 5 in length
        if (password.getText().toString().equals(cPassword.getText().toString()) ){
            if (password.getText().toString().length() >= 7) {
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "Please enter a password with at least 7 length.",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please make sure passwords matches.",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //check emails
    private boolean isEmail(String tEmail){
        return  (tEmail.contains("@") ) && (tEmail.substring(tEmail.indexOf("@")).length() > 2) && (tEmail.indexOf("@") > 2);
        /*if(tEmail.contains("@") ){
            if(tEmail.substring(tEmail.indexOf("@")).length() > 2){
                if(tEmail.indexOf("@") > 2){
                    return true;
                }
            }
        }

            return false;
            */

    }

    private class RegisterUserTask extends AsyncTask<String, String, String> {

        // String... arg0 is the same as String[] args
        protected String doInBackground(String... args) {

            try {


                String urlstr = "http://foodpickready.co.nf/signup.php";
                // Get the XML URL that was passed in

                URL url = new URL(urlstr);
                JSONObject postRegisterInParams = new JSONObject();
                postRegisterInParams.put("username", _username);
                postRegisterInParams.put("email", _email);
                postRegisterInParams.put("password", new Encrypt(_password).toString());
                postRegisterInParams.put("fname", _fName);
                postRegisterInParams.put("lname", _lName);
                postRegisterInParams.put("date_created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

                if(bp != null) {
                    _picValue = BitmapToString(bp);
                    postRegisterInParams.put("image_path", _picValue);
                }

                // connection is the communications link between the
                // application and a URL that we will read from.


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postRegisterInParams));

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
                    registerSuccess = true;
                    return sb.toString();

                }
                else {
                    registerSuccess = false;
                    return new String("false : "+responseCode);
                }


            } catch (MalformedURLException e) {
                Log.d("ERROR", "MalformedURLException", e);
                registerSuccess = false;
            } catch (IOException e) {
                Log.d("ERROR", "IOException", e);
                registerSuccess = false;
            } catch (JSONException e) {
                e.printStackTrace();
                registerSuccess = false;
            } catch (Exception e) {
                e.printStackTrace();
                registerSuccess = false;
            } finally {
            }



            return null;
        }

        protected void onPostExecute(String result){
            //if (registerSuccess) {
            if(result.equals("New user created successfully")){
                // setContentView(R.layout.login_fail);
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
                Intent in=new Intent(RegisterUser.this,LoginActivity2.class);
                finish();
                startActivity(in);
            } else {

                Toast.makeText(getApplicationContext(), "Failed to Register" + result,
                        Toast.LENGTH_LONG).show();
                Log.i("Failed to login", result);
                //dbTools.savepassword(userName, password);
                //Sharedprefrence is class which is used for storing the data permintely in mobile
                // un till and unless user uninstall the application from mobile. Here we are saving the data into sharedprefrence

//                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                SharedPreferences.Editor edit=sh.edit();
//                edit.putString("username",userName);
//                edit.putString("password",password);

                //Intent theIntent = new Intent(getApplication(), ShowAB.class);
                //startActivity(theIntent);
            }
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
}


