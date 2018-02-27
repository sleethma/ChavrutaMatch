package com.example.micha.chavrutamatch;

import android.app.Application;

import com.example.micha.chavrutamatch.DI.ApplicationComponent;
import com.example.micha.chavrutamatch.DI.ApplicationModule;
import com.example.micha.chavrutamatch.DI.DaggerApplicationComponent;
import com.example.micha.chavrutamatch.DI.SharedPrefsModule;

/**
 * Created by micha on 1/21/2018.
 * Application level class used for holding large JavaObjects in string format until populated!!
 */

public class ChavrutaMatch extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .sharedPrefsModule(new SharedPrefsModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

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
