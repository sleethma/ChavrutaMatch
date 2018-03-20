package com.example.micha.chavrutamatch;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.micha.chavrutamatch.DI.Components.ApplicationComponent;
import com.example.micha.chavrutamatch.DI.Components.DaggerApplicationComponent;
import com.example.micha.chavrutamatch.DI.Modules.ApplicationModule;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Modules.SharedPrefsModule;

/**
 * Created by micha on 1/21/2018.
 * Application level class used for holding large JavaObjects in string format until populated!!
 */

public class ChavrutaMatch extends Application {
    private  Context applicationContext = this;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static ChavrutaMatch get(Activity activity){
        return (ChavrutaMatch) activity.getApplication();
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

    public  Context getApplicationsContext(){
        return applicationContext;
    }
}
