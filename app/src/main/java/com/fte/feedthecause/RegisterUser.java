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
            _cPassword;

    private Button
            btnCancel,
            btnRegister;

    private boolean registerSuccess;

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

        fName = (EditText) findViewById(R.id.eTxtFName);
        lName = (EditText) findViewById(R.id.eTxtLName);
        email = (EditText) findViewById(R.id.eTxtEmail);
        username = (EditText) findViewById(R.id.eTxtUsername);
        password = (EditText) findViewById(R.id.eTxtPassword);
        cPassword = (EditText) findViewById(R.id.eTxtCPassword);

        btnCancel = (Button) findViewById(R.id.btnCregister);
        btnRegister = (Button) findViewById(R.id.btnRegister);

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
            errorMessage += "Enter a first name\n";
        }else{
            _fName = fName.getText().toString();
        }

        if(lName.getText().toString().trim().equals("")){
            safe = false;
        }else{
            _lName = lName.getText().toString();
        }
        if(!isEmail(email.getText().toString().trim())){
            safe = false;
        }else{
            _email = email.getText().toString();
        }
        if(username.getText().toString().trim().equals("")){
            safe = false;
        }else{
            _username = username.getText().toString();
        }
        if(!checkPasswords()){
            safe = false;
        }else{
            _password = password.getText().toString();
        }

        return safe;
    }

    private boolean checkPasswords(){
        //check the passwords to equal each other
        //make sure they arent empty
        //make sure at least 5 in length
        if(5==5)
        return true;
        else
            return false;
    }

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


