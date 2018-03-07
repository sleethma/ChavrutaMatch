package com.example.micha.chavrutamatch.MVPConstructs.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Base64;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.DI.Components.DaggerMAComponent;
import com.example.micha.chavrutamatch.DI.Components.MAComponent;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;

import javax.inject.Inject;

/**
 * Created by micha on 2/26/2018.
 */

public class MainActivityModel implements MAContractMVP.Model {

    private static Uri mUserCustomAvatarUri;
    private static byte[] mUserCustomAvatarBase64ByteArray;
    public static String mUserLoginType = "none";
    private static String userAvatarNumberString = "0";
    private static String userId, userName, userFirstName, userLastName, userPhoneNumber,
            userEmail, userCityState, userCustomAvatarUriString, userImagePathString, userCustomAvatarBase64String,
            userBio;

    public SharedPreferences sp;
    AccountActivity accountActivity;

    @Inject
    public MainActivityModel(SharedPreferences sp, AccountActivity accountActivity) {
        this.sp = sp;
        this.accountActivity = accountActivity;
    }

    public String getStringDataFromSP(String key) {
        return sp.getString(key, "null");
    }


    @Override
    public void setAllSPValuesToUserDetails() {
        UserDetails.setmUserPhoneNumber(userPhoneNumber);
        UserDetails.setmUserEmail(userEmail);
        UserDetails.setmUserName(userName);
        UserDetails.setmUserFirstName(userFirstName);
        UserDetails.setmUserLastName(userLastName);
        UserDetails.setmUserAvatarNumberString(userAvatarNumberString);
        UserDetails.setmUserBio(userBio);
        UserDetails.setmUserId(userId);
        UserDetails.setmUserCustomAvatarUriString(userCustomAvatarUriString);
        UserDetails.setmUserCustomAvatarBase64String(userCustomAvatarBase64String);
        UserDetails.setUserCityState(userCityState);
        UserDetails.setmUserCustomAvatarUri(mUserCustomAvatarUri);
        UserDetails.setmUserCustomAvatarBase64ByteArray(mUserCustomAvatarBase64ByteArray);
    }

    public static void setByteArrayFromString(String stringToDecode) {
        mUserCustomAvatarBase64ByteArray = Base64.decode(stringToDecode, Base64.DEFAULT);
    }

    public void putIntDataInSP(String key, int intData) {
        sp.edit().putInt(key, intData).apply();
    }

    public int getIntDataFromSP(String key) {
        return sp.getInt(key, -1);
    }

    @Override
    public boolean isVerifiedAsLoggedIn() {
        return accountActivity.verifyAccount();
    }

    @Override
    public void putBooleanDataInSP(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    @Override
    public void putStringDataInSP(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    @Override
    public void getAllUserDetailsFromSP() {
        //get info from newUserLogin if exists
        userPhoneNumber = getStringDataFromSP("user phone number key");
        userEmail = getStringDataFromSP("user email key");
        userName = getStringDataFromSP("user name key");
        userFirstName = getStringDataFromSP("user first name key");
        userLastName = getStringDataFromSP("user last name key");
        userAvatarNumberString = getStringDataFromSP("user avatar number key");
        userBio = getStringDataFromSP("user bio key");
        userId = getStringDataFromSP("user account id key");
        userCustomAvatarUriString = getStringDataFromSP("user custom avatar key");
        userCustomAvatarBase64String = getStringDataFromSP("user avatar base 64 key");
        userCityState = getStringDataFromSP("user city state key");

        //convert String to Uri, covertToByteArray and save in @this
        if (userCustomAvatarUriString != null) {
            mUserCustomAvatarUri = Uri.parse(userCustomAvatarUriString);
            setByteArrayFromString(userCustomAvatarBase64String);
        }
    }
}
