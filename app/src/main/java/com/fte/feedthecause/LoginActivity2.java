package com.fte.feedthecause;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


//using to login
public class LoginActivity2 extends AppCompatActivity {

    EditText
        eUsername,
        ePassword;

    String
        sUsername,
        sPassword;

    Button
            btnLogIn,
        btnRegister;

    boolean loginsuccess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        eUsername = (EditText) findViewById(R.id.txtUserId);
        sUsername = eUsername.getText().toString();

        ePassword = (EditText) findViewById(R.id.txtPass);

        btnLogIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity2.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogIn();
            }
        });

    }

    private void attemptLogIn() {

        if(isSafe()){
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });

                    //postPostGRESQL();
                    new UserLoginTask().execute();
                }
            }).start();
        }
    }

    private boolean isSafe() {
        //check the fields
        boolean safe = true;
        String errorMessage = "Please solve these problems\n";
        if(eUsername.getText().toString().trim().equals("")){
            safe = false;
            errorMessage += "Enter a valid user name.\n";
        }else{
            sUsername = eUsername.getText().toString();
        }
        if(ePassword.getText().toString().trim().equals("")){
            safe = false;
            errorMessage += "Enter a valid password.\n";
        }else{
            sPassword = ePassword.getText().toString();
        }
        return safe;
    }


    private class UserLoginTask extends AsyncTask<String, String, String> {

        // String... arg0 is the same as String[] args
        protected String doInBackground(String... args) {

            try {

                //String urlstr = "http://collab.ncat.edu/NCAT/login.php?username="+userName+"&password="+password;
                String urlstr = "http://foodpickready.co.nf/login.php";
                // Get the XML URL that was passed in

                URL url = new URL(urlstr);
                JSONObject postSignInParams = new JSONObject();
                postSignInParams.put("username", sUsername);
                postSignInParams.put("password", sPassword);

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
                writer.write(getPostDataString(postSignInParams));

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
                    loginsuccess = true;
                    return sb.toString();

                }
                else {
                    loginsuccess = false;
                    return new String("false : "+responseCode);
                }


            } catch (MalformedURLException e) {
                Log.d("ERROR", "MalformedURLException", e);
                loginsuccess = false;
            } catch (IOException e) {
                Log.d("ERROR", "IOException", e);
                loginsuccess = false;
            } catch (JSONException e) {
                e.printStackTrace();
                loginsuccess = false;
            } catch (Exception e) {
                e.printStackTrace();
                loginsuccess = false;
            } finally {
            }



            return null;
        }

        protected void onPostExecute(String result){
            if (result.equals("Sucess!")) {
                // setContentView(R.layout.login_fail);
                Controller c = new Controller();
                c.setUserId(sUsername);
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
                Intent in=new Intent(LoginActivity2.this,FeedACT.class);
                finish();
                startActivity(in);
            } else {

                Toast.makeText(getApplicationContext(), "Failed to login" + result,
                        Toast.LENGTH_LONG).show();

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
