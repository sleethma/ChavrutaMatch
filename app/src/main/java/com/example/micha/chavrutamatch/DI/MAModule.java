package com.example.micha.chavrutamatch.DI;

import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by micha on 2/26/2018.
 */
@Module
@Singleton
public class MAModule {

    //provides presenter concrete class object with model instance which is the implementation of the Presenter interface
    @Provides
    public MAContractMVP.Presenter providesLunchActivityPresenter(MAContractMVP.Model model){
        return new MAPresenter(model);
    }

    //provides and constructs the Model instance when it is requested
    @Provides
    public MAContractMVP.Model providesLunchActivityModel(){
        return new Model();

}
