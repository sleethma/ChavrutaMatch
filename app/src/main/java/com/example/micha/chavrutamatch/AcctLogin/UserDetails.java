package com.example.micha.chavrutamatch.AcctLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;

import static android.R.attr.defaultValue;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by micha on 8/23/2017.
 */

public class UserDetails {
    public final static String LOG_TAG = LoginActivity.class.getSimpleName();

    private static String mUserId;
    private static String mUserPassword;
    private static String mUserName;
    private static String mUserAvatarNumberString;
    private static String mUserFirstName;
    private static String mUserLastName;
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

    //sets all user data from AddBio.class
    public static void setAllUserDataFromAddBio(String...param){
        mUserId = param[0];
         mUserName=param[1];
         mUserAvatarNumberString=param[2];
         mUserFirstName=param[3];
        mUserLastName=param[4];
        mUserPhoneNumber=param[5];
        mUserEmail=param[6];
        Log.i(LOG_TAG, mUserId + " " + mUserName + " " + mUserAvatarNumberString + " " +
        mUserFirstName + " " + mUserLastName + " " + mUserPhoneNumber + " " +  mUserEmail);
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

    public static String getmUserName() {
        return mUserName;
    }

    public static void setmUserName(String mUserName) {
        UserDetails.mUserName = mUserName;
    }

    public static String getmUserAvatarNumberString() {
        return mUserAvatarNumberString;
    }

    public static void setmUserAvatarNumberString(String mUserAvatarNumberString) {
        UserDetails.mUserAvatarNumberString = mUserAvatarNumberString;
    }

    public static String getmUserFirstName() {
        return mUserFirstName;
    }

    public static void setmUserFirstName(String mUserFirstName) {
        UserDetails.mUserFirstName = mUserFirstName;
    }

    public static String getmUserLastName() {
        return mUserLastName;
    }

    public static void setmUserLastName(String mUserLastName) {
        UserDetails.mUserLastName = mUserLastName;
    }

}
