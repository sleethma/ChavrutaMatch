package com.example.micha.chavrutamatch.AcctLogin;

/**
 * Created by micha on 8/23/2017.
 */

public class UserDetails {

    private static String mUserId;
    private static String mUserPassword;
    private static String mUserPhoneNumber;
    private static String mUserEmail;

    UserDetails(String id, String password, String phoneNumber, String email){
        mUserId = id;
        mUserPassword =password;
        mUserPhoneNumber = phoneNumber;
        mUserEmail = email;
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

    public static void setmUserPhoneNumber(String mUserPhoneNumber) {
        UserDetails.mUserPhoneNumber = mUserPhoneNumber;
    }

    public static String getmUserEmail() {
        return mUserEmail;
    }

    public static void setmUserEmail(String mUserEmail) {
        UserDetails.mUserEmail = mUserEmail;
    }
}
