package com.example.micha.chavrutamatch.DI.Modules;

import android.app.Application;
import android.content.Context;

import com.example.micha.chavrutamatch.DI.Scopes.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by micha on 2/16/2018.
 */

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides
    @AppScope
    public Context provideContext(){
        return application;
    }

}
