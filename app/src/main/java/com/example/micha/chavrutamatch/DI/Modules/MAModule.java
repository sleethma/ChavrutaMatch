package com.example.micha.chavrutamatch.DI.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.DI.Scopes.AppScope;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Models.SharedPrefsModel;
import com.example.micha.chavrutamatch.MVPConstructs.Presenters.MAPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

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
    //provides and constructs the Model instance when it is requested
    @Provides
    @MAScope
    @Inject //success!
    public MAContractMVP.Model providesSharedPrefModel(SharedPreferences sp) {
        return new SharedPrefsModel(sp);
    }

    //provides presenter concrete class object with model instance which is the implementation of the Presenter interface
    @Provides
    @MAScope
    public MAContractMVP.Presenter providesMAPresenter(MAContractMVP.Model model){
        return  new MAPresenter(model);
    }


}
