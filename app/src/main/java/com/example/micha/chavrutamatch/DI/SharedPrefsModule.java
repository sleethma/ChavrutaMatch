package com.example.micha.chavrutamatch.DI;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Models.SharedPrefsModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by micha on 2/26/2018.
 */

@Module
@Singleton
public class SharedPrefsModule {

    @Provides
    @Inject//injects context via method injection
     SharedPrefsModel providesSharedPrefClass(SharedPreferences sp){
        return new SharedPrefsModel(sp);
    }

 @Provides
    @Inject
    SharedPreferences providesSharedPreferences(Context context){
        return context.getSharedPreferences("user_data", MODE_PRIVATE);
    }
}
