package com.fte.feedthecause;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by trenton on 11/15/16.
 */
public class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        switch (item){
            case "Logout":
               // Intent intent = new Intent(FeedACT.this, LoginActivity.class);
               // finish();
               // startActivity(intent);
                item = "Logout2";
                break;
            case "Settings":
                /*
                Intent intentSettings = new Intent(FeedACT.this, LoginActivity.class);
                finish();
                startActivity(intentSettings);
                break;
                */
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
