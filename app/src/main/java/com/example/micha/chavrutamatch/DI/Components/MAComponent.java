package com.example.micha.chavrutamatch.DI.Components;

import android.content.Context;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.AcctLogin.LoginActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Modules.SharedPrefsModule;
import com.example.micha.chavrutamatch.DI.Modules.UserDetailsModule;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.HostSelect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;
import com.example.micha.chavrutamatch.MVPConstructs.Presenters.MAPresenter;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.NewHost;
import com.example.micha.chavrutamatch.OpenChavrutaAdapter;

import dagger.Component;

/**
 * Created by micha on 2/28/2018.
 */

@Component(dependencies = ApplicationComponent.class , modules = {MAModule.class, SharedPrefsModule.class})
@MAScope
public interface MAComponent {
    Context context();


    void inject(MainActivityModel target);
    void inject(AccountActivity target);
    void inject(MainActivity target);
    //todo: check if below needed
    void inject(Context target);
}
