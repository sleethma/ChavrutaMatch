package com.example.micha.chavrutamatch;

import android.app.Application;

/**
 * Created by micha on 1/21/2018.
 * Application level class used for holding large JavaObjects in string format until populated!!
 */

public class ChavrutaMatch extends Application {

    private static String myChavrutaJsonString;
    private static String openHostsJsonString;


    public static String getMyChavrutaJsonString() {
        return myChavrutaJsonString;
    }

    public static void setMyChavrutaJsonString(String jsonString) {
        myChavrutaJsonString = jsonString;
    }

    public static String getOpenHostsJsonString() {
        return openHostsJsonString;
    }

    public static void setOpenHostsJsonString(String jsonString) {
        openHostsJsonString = jsonString;
    }
}
