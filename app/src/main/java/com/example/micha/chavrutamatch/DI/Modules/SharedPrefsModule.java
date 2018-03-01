package com.example.micha.chavrutamatch.DI.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.DI.Modules.ApplicationModule;
import com.example.micha.chavrutamatch.DI.Scopes.AppScope;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
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
public class SharedPrefsModule {

    @Provides
    @MAScope //success on sp
    SharedPrefsModel providesSharedPrefClass(SharedPreferences sp) {
        return new SharedPrefsModel(sp);
    }

    @Provides
    @MAScope
    @Inject//success on context
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("user_data", MODE_PRIVATE);
    }
}
