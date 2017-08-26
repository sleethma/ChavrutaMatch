package com.example.micha.chavrutamatch.AcctLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;

import static android.R.attr.defaultValue;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by micha on 8/23/2017.
 */

public class UserDetails {

    private static String mUserId;
    private static String mUserPassword;
    private static String mUserPhoneNumber;
    private static String mUserEmail;




    public UserDetails(String id, String password, String phoneNumber, String email
    ){
        mUserId = id;
        mUserPassword =password;
        mUserPhoneNumber = phoneNumber;
        mUserEmail = email;
    }
    public UserDetails(){
    }

    public static String getmUserId() {
        return mUserId;
    }

    public static void setmUserId(String mUserId) {
        UserDetails.mUserId = mUserId;
    }

    public static String getmUserPassword() {
        return mUserPassword;
    }

    public static void setmUserPassword(String mUserPassword) {
        UserDetails.mUserPassword = mUserPassword;
    }

    public static String getmUserPhoneNumber() {
        return mUserPhoneNumber;
    }

    public void setmUserPhoneNumber(String mUserPhoneNumber) {
        UserDetails.mUserPhoneNumber = mUserPhoneNumber;

    }

    public static String getmUserEmail() {
        return mUserEmail;
    }

    public static void setmUserEmail(String mUserEmail) {
        UserDetails.mUserEmail = mUserEmail;
    }

}
