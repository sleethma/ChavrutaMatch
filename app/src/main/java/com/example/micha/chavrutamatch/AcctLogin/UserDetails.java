package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

/**
 * Created by micha on 8/23/2017.
 */

public class UserDetails extends AppCompatActivity{
    public final static String LOG_TAG = LoginActivity.class.getSimpleName();

    private static String mUserId;
    private static String mUserPassword;
    private static String mUserName;
    private static String mUserAvatarNumberString= "0";
    private static String mUserFirstName;
    private static String mUserLastName;
    private static String mUserPhoneNumber;
    private static String mUserEmail;
    private static String mUserCityState;
    private static  SharedPreferences mPreferences;
    private static Context mContext;
    private static String mUserCustomAvatarUriString;
    private static Uri mUserCustomAvatarUri;
    private static String mUserImagePathString;
    private static String mUserCustomAvatarBase64String;
    private  static byte[] mUserCustomAvatarBase64ByteArray;
    private static String mUserBio;
    public static String mUserLoginType = "none";

    public UserDetails(String id, String password, String phoneNumber, String email
    ){
        mUserId = id;
        mUserPassword =password;
        mUserPhoneNumber = phoneNumber;
        mUserEmail = email;
    }

    //sets all user data from AddBio.class
    public static void setAllUserDataFromAddBio(String...param){
         mUserName=param[0];
         mUserAvatarNumberString=param[1];
         mUserFirstName=param[2];
        mUserLastName=param[3];
        mUserPhoneNumber=param[4];
        mUserEmail=param[5];
        mUserCityState = param[6];
        mUserCustomAvatarUriString = param[7];
    }

    public static void  setUserDetailsFromSP(Context context){
        SharedPreferences prefs = context.getSharedPreferences("user_data", MODE_PRIVATE);

        //get info from newUserLogin if exists
        mUserPhoneNumber = prefs.getString("user phone number key", null);
        mUserEmail = prefs.getString("user email key", null);
        mUserName = prefs.getString("user name key", null);
        mUserFirstName = prefs.getString("user first name key", null);
        mUserLastName = prefs.getString("user last name key", null);
        mUserAvatarNumberString = prefs.getString("user avatar number key", null);
        mUserBio = prefs.getString("user bio key", null);
        mUserId = prefs.getString("user account id key", null);
        mUserCustomAvatarUriString = prefs.getString("user custom avatar key", null);
        mUserCustomAvatarBase64String = prefs.getString("user avatar base 64 key", "none");
        mUserCityState = prefs.getString("user city state key", null);

        //convert String to Uri and save in @this
        if(mUserCustomAvatarUriString != null) {
            mUserCustomAvatarUri = Uri.parse(mUserCustomAvatarUriString);
            setByteArrayFromString(mUserCustomAvatarBase64String);
        }
    }

    public static void setLoginType(String loginType){
        mUserLoginType = loginType;
    }

    public static String getUserAvatarBase64String(){
        return mUserCustomAvatarBase64String;
    }

    //gets application context
    public static void setsApplicationContext(Context context){
        mContext = context;
    }

    public static void setmUserAvatarNumberString(String mUserAvatarNumberString) {
        UserDetails.mUserAvatarNumberString = mUserAvatarNumberString;
    }
    public static void setmUserCustomAvatarUriString(String UserCustomAvatarUriString){
        mUserCustomAvatarUriString = UserCustomAvatarUriString;
    }

    public static byte[] getUserCustomAvatarBase64ByteArray(){
        return mUserCustomAvatarBase64ByteArray;
    }

    public static void setByteArrayFromString(String stringToDecode){
        mUserCustomAvatarBase64ByteArray = Base64.decode(stringToDecode, Base64.DEFAULT);
    }

    public static String getmUserId() {
        return mUserId;
    }

    public static void setUserCityState(String userCityState) {
        mUserCityState =userCityState;
    }

    public static String getUserCityState(){
        return mUserCityState;
    }
    //sdk < 20 must have formatted url php string to succeed
    public static String getUserCallFormattedCityState(){
        String formattedCityState = mUserCityState.replaceAll(" ", "%20");
        return formattedCityState;
    }

    public static void setmUserId(String userId) {
        mUserId = userId;
    }

    public static void setmUserPhoneNumber(String mUserPhoneNumber) {
        UserDetails.mUserPhoneNumber = mUserPhoneNumber;
    }

    public static Uri getHostAvatarUri(){
        return mUserCustomAvatarUri;
    }

    public static void setmUserEmail(String mUserEmail) {
        UserDetails.mUserEmail = mUserEmail;
    }

    public static String getmUserName() {
        return mUserName;
    }

    public static String getmUserAvatarNumberString() {
        return mUserAvatarNumberString;
    }

    public static String getmUserFirstName() {
        return mUserFirstName;
    }

    public static String getmUserLastName() {
        return mUserLastName;
    }

}
