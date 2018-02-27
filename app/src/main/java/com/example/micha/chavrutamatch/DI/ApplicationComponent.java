package com.example.micha.chavrutamatch.DI;

import com.example.micha.chavrutamatch.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by micha on 2/16/2018.
 */

@Singleton
@Component(modules = {ApplicationModule.class, SharedPrefsModule.class})
public interface ApplicationComponent {

    void inject(MainActivity target);
}
