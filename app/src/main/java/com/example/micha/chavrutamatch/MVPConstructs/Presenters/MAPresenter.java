package com.example.micha.chavrutamatch.MVPConstructs.Presenters;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;

/**
 * Created by micha on 2/26/2018.
 */

public class MAPresenter implements MAContractMVP.Presenter {


    private MAContractMVP.Model sharedPrefsModel;
    private MAContractMVP.View mainActivityView;

    public MAPresenter(MAContractMVP.Model sharedPrefsModel) {
        this.sharedPrefsModel = sharedPrefsModel;
        sharedPrefsModel.getAllUserDetailsFromSP();
        sharedPrefsModel.setAllSPValuesToUserDetails();
    }

    @Override
    public void setView(MAContractMVP.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void testMVPToast() {
        if (mainActivityView != null) {
            mainActivityView.sendToast();
        }
    }

    @Override
    public void setupToolbar() {
        mainActivityView.setToolbarUnderline();
        mainActivityView.setUserAvatar();
    }

    @Override
    public void getAccountKit() {
       if(sharedPrefsModel.isVerifiedAsLoggedIn()) {
           sharedPrefsModel.putStringDataInSP("user account id key", UserDetails.getmUserId());
           sharedPrefsModel.putBooleanDataInSP("new_user_key", UserDetails.getNewUserKey());
           sharedPrefsModel.putStringDataInSP("user phone number key", UserDetails.getmUserPhoneNumber());
           sharedPrefsModel.putStringDataInSP("user email key", UserDetails.getmUserEmail());
       }else{
           if (mainActivityView != null) {
               mainActivityView.sendToast();
           }
       }
    }

    @Override
    public void getJsonChavrutaString() {

    }
}
