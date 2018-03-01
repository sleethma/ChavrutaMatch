package com.example.micha.chavrutamatch.MVPConstructs.Presenters;

import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;

/**
 * Created by micha on 2/26/2018.
 */

public class MAPresenter implements MAContractMVP.Presenter {

    private MAContractMVP.Model sharedPrefsModel;
    private MAContractMVP.View mainActivityView;

    public MAPresenter(MAContractMVP.Model sharedPrefsModel) {
        this.sharedPrefsModel = sharedPrefsModel;
    }

    @Override
    public void setView(MAContractMVP.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void testMVPToast(){
        if (mainActivityView != null) {
            mainActivityView.sendToast();
        }
    }

}
