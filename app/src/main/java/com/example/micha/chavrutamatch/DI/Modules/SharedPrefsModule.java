package com.example.micha.chavrutamatch.DI.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by micha on 2/26/2018.
 */

@Module
public class SharedPrefsModule {

    @Provides
    @MAScope
    @Inject
    MainActivityModel providesSharedPrefClass(SharedPreferences sp, AccountActivity accountActivity) {
        return new MainActivityModel(sp, accountActivity);
    }

    @Provides
    @MAScope
    @Inject//success on context
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("user_data", MODE_PRIVATE);
    }
}
