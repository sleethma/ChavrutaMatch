package com.example.micha.chavrutamatch.DI.Components;

import android.content.Context;

import com.example.micha.chavrutamatch.AcctLogin.LoginActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.AddSelect;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.DI.Modules.ApplicationModule;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Modules.UserDetailsModule;
import com.example.micha.chavrutamatch.DI.Scopes.AppScope;
import com.example.micha.chavrutamatch.DI.Modules.SharedPrefsModule;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.HostSelect;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.NewHost;
import com.example.micha.chavrutamatch.OpenChavrutaAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by micha on 2/16/2018.
 */

@AppScope
@Component(modules = {ApplicationModule.class, UserDetailsModule.class})
public interface ApplicationComponent {
    UserDetails userDetailsInstance();

    void inject(HostSelect target);
    void inject(AddBio target);
    void inject(ServerConnect target);
    void inject(NewHost target);
    void inject(LoginActivity target);
    void inject(AddSelect target);



}
