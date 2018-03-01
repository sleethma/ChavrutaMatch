package com.example.micha.chavrutamatch.MVPConstructs;

import android.content.SharedPreferences;

/**
 * Created by micha on 2/26/2018.
 */

public interface MAContractMVP {

    interface View{
    void sendToast();
    }
    interface Presenter{
        void setView(MAContractMVP.View view);
        void testMVPToast();
    }


    interface Model{

    }
}
