package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import io.reactivex.annotations.Nullable;

/**
 * Created by micha on 8/23/2017.
 */

public class UserDetails extends AppCompatActivity{

    private static String mUserId;
    private static String currentUserId;
    private static String mUserPassword;
    private static String userAccountKitId;

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
    private static boolean newUserKey;

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

//    public static void  setUserDetailsFromSP(Context context){
    public static boolean getNewUserKey() {
        return newUserKey;
    }

    //    }
    public void setNewUserKey(boolean newUserKey) {
        this.newUserKey = newUserKey;
    }

    //        }
    public static void setLoginType(String loginType){
        mUserLoginType = loginType;
    }

    //            setByteArrayFromString(mUserCustomAvatarBase64String);
    public static String getUserAvatarBase64String(){
        return mUserCustomAvatarBase64String;
    }

    //            mUserCustomAvatarUri = Uri.parse(mUserCustomAvatarUriString);
    //gets application context

    //        if(mUserCustomAvatarUriString != null) {
    public static void setsApplicationContext(Context context){
        mContext = context;
    }
    //        //convert String to Uri and save in @this
    public  void setmUserAvatarNumberString(String mUserAvatarNumberString) {
        this.mUserAvatarNumberString = mUserAvatarNumberString;
    }

    //
    public static void setmUserCustomAvatarUriString(String UserCustomAvatarUriString){
        mUserCustomAvatarUriString = UserCustomAvatarUriString;
    }
    //        mUserCityState = prefs.getString("user city state key", null);
    public static byte[] getUserCustomAvatarBase64ByteArray(){
        return mUserCustomAvatarBase64ByteArray;
    }

    //        mUserCustomAvatarBase64String = prefs.getString("user avatar base 64 key", "none");
    public static void setByteArrayFromString(String stringToDecode){
        mUserCustomAvatarBase64ByteArray = Base64.decode(stringToDecode, Base64.DEFAULT);
    }

    //        mUserCustomAvatarUriString = prefs.getString("user custom avatar key", "none");
    public static void setUserCityState(String userCityState) {
        mUserCityState =userCityState;
    }

    //        mUserId = prefs.getString("user account id key", null);
    public static String getUserCityState(){
        return mUserCityState;
    }

    //        mUserBio = prefs.getString("user bio key", null);
    //sdk < 20 must have formatted url php string to succeed

    //        mUserAvatarNumberString = prefs.getString("user avatar number key", "0");
    public static String getUserCallFormattedCityState(){
        return mUserCityState.replaceAll(" ", "%20");
    }
    //        mUserLastName = prefs.getString("user last name key", null);
    @Nullable
    public static void setmUserId(String userId) {
        mUserId = userId;
    }
    //        mUserFirstName = prefs.getString("user first name key", null);

    //        mUserName = prefs.getString("user name key", null);
    public static String getmUserId() {
        return mUserId;
    }

    //    }
    public static String getCurrentUserId() {
        return currentUserId;
    }

    //        currentUserId = userId;

    //    public static void setCurrentUserIdForThisSession(String userId) {
    public static String getmUserPhoneNumber() {
        return mUserPhoneNumber;
    }

    //        mUserEmail = prefs.getString("user email key", null);
    public void setmUserPhoneNumber(String mUserPhoneNumber) {
        this.mUserPhoneNumber = mUserPhoneNumber;
    }

    //        mUserPhoneNumber = prefs.getString("user phone number key", null);
    public static Uri getHostAvatarUri(){
        return mUserCustomAvatarUri;
    }

    //        //get info from newUserLogin if exists
    public static String getmUserEmail() {
        return mUserEmail;
    }

    //
    public static void setmUserEmail(String mUserEmail) {
        UserDetails.mUserEmail = mUserEmail;
    }

    //        SharedPreferences prefs = context.getSharedPreferences("user_data", MODE_PRIVATE);
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

    public static void setmUserName(String mUserName) {
        UserDetails.mUserName = mUserName;
    }

    public  void setmUserFirstName(String mUserFirstName) {
        this.mUserFirstName = mUserFirstName;
    }
    public void setmUserLastName(String mUserLastName) {
        this.mUserLastName = mUserLastName;
    }
    public static void setmUserBio(String mUserBio) {
        UserDetails.mUserBio = mUserBio;
    }

    public void setmUserPassword(String mUserPassword) {
        this.mUserPassword = mUserPassword;
    }

    public static void setmUserCityState(String mUserCityState) {
        UserDetails.mUserCityState = mUserCityState;
    }

    public static void setmUserCustomAvatarUri(Uri mUserCustomAvatarUri) {
        UserDetails.mUserCustomAvatarUri = mUserCustomAvatarUri;
    }

    public  void setmUserCustomAvatarBase64String(String mUserCustomAvatarBase64String) {
        UserDetails.mUserCustomAvatarBase64String = mUserCustomAvatarBase64String;
    }

    public static void setmUserCustomAvatarBase64ByteArray(byte[] mUserCustomAvatarBase64ByteArray) {
        UserDetails.mUserCustomAvatarBase64ByteArray = mUserCustomAvatarBase64ByteArray;
    }

    public static   String getUserAccountKitId() {
        return userAccountKitId;
    }

    public static void setUserAccountKitId(String userId) {
        userAccountKitId = userId;
    }
}
