package com.example.micha.chavrutamatch.DI.Components;

import android.content.Context;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.DI.Modules.SharedPrefsModule;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;
import com.example.micha.chavrutamatch.MVPConstructs.Presenters.MAPresenter;
import com.example.micha.chavrutamatch.MainActivity;

import dagger.Component;

/**
 * Created by micha on 2/28/2018.
 */

@Component(modules = {MAModule.class, SharedPrefsModule.class}, dependencies = ApplicationComponent.class)
@MAScope
public interface MAComponent {
    void inject(MainActivity target);
    void inject(AccountActivity target);
    //todo: check if below needed
    void inject(Context target);
}
