package com.example.micha.chavrutamatch.MVPConstructs;

import android.content.SharedPreferences;

/**
 * Created by micha on 2/26/2018.
 */

public interface MAContractMVP {

    interface View{
    void sendToast();
    void setToolbarUnderline();
    void setUserAvatar();
    }

    interface Presenter{
        void setView(MAContractMVP.View view);
        void testMVPToast();
        void setupToolbar();
        void getAccountKit();
        void getJsonChavrutaString();
    }


    interface Model{
        void putStringDataInSP(String key, String value);
        void getAllUserDetailsFromSP();
    void setAllSPValuesToUserDetails();
    boolean isVerifiedAsLoggedIn();
       void putBooleanDataInSP(String key, boolean value);
    }
}
