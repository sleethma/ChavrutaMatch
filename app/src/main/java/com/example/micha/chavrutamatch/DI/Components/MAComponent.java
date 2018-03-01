package com.example.micha.chavrutamatch.DI.Components;

import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Modules.SharedPrefsModule;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.MainActivity;

import dagger.Component;

/**
 * Created by micha on 2/28/2018.
 */

@Component(modules = {MAModule.class, SharedPrefsModule.class}, dependencies = ApplicationComponent.class)
@MAScope
public interface MAComponent {

    void inject(MainActivity target);
}
