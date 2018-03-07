package com.example.micha.chavrutamatch.DI.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;
import com.example.micha.chavrutamatch.MVPConstructs.Presenters.MAPresenter;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;


/**
 * Created by micha on 2/26/2018.
 */
@Module
public class MAModule {
    Context maContext;

    public MAModule(Context context){
        this.maContext = context;
    }

    @Provides
    @MAScope
    Context providesAppContext(){
        return maContext;
    }


    @Provides
    @MAScope
    public AccountActivity providesAccountActivity(){
        return new AccountActivity();
    }

    //provides and constructs the Model instance when it is requested
    @Provides
    @MAScope
    @Inject
    public MAContractMVP.Model providesSharedPrefModel(SharedPreferences sp, AccountActivity accountActivity) {
        return new MainActivityModel(sp, accountActivity);
    }

    //provides presenter concrete class object with model instance which is the implementation of the Presenter interface
    @Provides
    @MAScope
    public MAContractMVP.Presenter providesMAPresenter(MAContractMVP.Model model){
        return  new MAPresenter(model);
    }


}