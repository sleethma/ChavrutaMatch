package com.example.micha.chavrutamatch.DI.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.Data.ServerConnect;
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

   @Inject
    UserDetails userDetailsInstance;

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
    @Inject
    public AccountActivity providesAccountActivity(UserDetails userDetailsInstance){
        return new AccountActivity(userDetailsInstance);
    }


    @Provides
    @MAScope
    @Inject
    public ServerConnect providesServerConnectInstance(Context maContext, UserDetails userDetailsInstance){
        return new ServerConnect(maContext, userDetailsInstance);
    }

    //todo:does need to inject?
    //provides and constructs the Model instance when it is requested
    @Provides
    @MAScope
    @Inject
    public MAContractMVP.Model providesMainActivityModel(Context appContext, SharedPreferences sp,
                                                         UserDetails userDetailsInstance,
                                                         ServerConnect serverConnectInstance,
                                                         AccountActivity accountActivity) {
        return new MainActivityModel(appContext, sp, userDetailsInstance, serverConnectInstance, accountActivity);
    }

    //provides presenter concrete class object with model instance which is the implementation of the Presenter interface
    @Provides
    @MAScope
    public MAContractMVP.Presenter providesMAPresenter(MAContractMVP.Model model){
        return  new MAPresenter(model);
    }



}
