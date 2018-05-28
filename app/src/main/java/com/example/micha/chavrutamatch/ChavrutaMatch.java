package com.example.micha.chavrutamatch;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.micha.chavrutamatch.DI.Components.ApplicationComponent;
import com.example.micha.chavrutamatch.DI.Components.DaggerApplicationComponent;
import com.example.micha.chavrutamatch.DI.Components.DaggerMAComponent;
import com.example.micha.chavrutamatch.DI.Components.MAComponent;
import com.example.micha.chavrutamatch.DI.Modules.ApiModule;
import com.example.micha.chavrutamatch.DI.Modules.ApplicationModule;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Modules.UserDetailsModule;

/**
 * Created by micha on 1/21/2018.
 * Application level class used for holding large JavaObjects in string format until populated!!
 */

public class ChavrutaMatch extends Application {
    private  Context applicationContext = this;
    private ApplicationComponent applicationComponent;
    private MAComponent maComponent;



    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .userDetailsModule(new UserDetailsModule())
                .build();

        maComponent = DaggerMAComponent.builder()
                .mAModule(new MAModule(this))
                .applicationComponent(applicationComponent)
                .apiModule(new ApiModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public  MAComponent getMAComponent() {
        return maComponent;
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
