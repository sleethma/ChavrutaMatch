package com.example.micha.chavrutamatch.DI.Components;

import android.content.Context;

import com.example.micha.chavrutamatch.DI.Modules.ApplicationModule;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Scopes.AppScope;
import com.example.micha.chavrutamatch.DI.Modules.SharedPrefsModule;
import com.example.micha.chavrutamatch.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by micha on 2/16/2018.
 */

@AppScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
}
