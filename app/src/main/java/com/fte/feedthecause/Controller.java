package com.fte.feedthecause;

/**
 * Created by trenton on 11/16/16.
 */

public class Controller {

    private static String userId;

    public Controller(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        Controller.userId = userId;
    }
}
