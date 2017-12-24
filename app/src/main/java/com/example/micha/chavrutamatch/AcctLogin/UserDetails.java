package com.example.micha.chavrutamatch.AcctLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.defaultValue;
import static android.R.attr.targetActivity;
import static android.content.Context.MODE_PRIVATE;

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
        mUserCityState = param[7];
        mUserCustomAvatarUriString = param[8];
    }

    public static String[]  getUserDataFromSP(){
        SharedPreferences mPreferences = mContext.getSharedPreferences("user_data", MODE_PRIVATE);
        mUserPhoneNumber = mPreferences.getString("user phone number key",null);
        mUserName = mPreferences.getString("User Name", null);
        mUserEmail = mPreferences.getString("User Email", null);
        mUserAvatarNumberString = mPreferences.getString("User Avatar",null);
        mUserFirstName = mPreferences.getString("User First Name",null);
        mUserLastName = mPreferences.getString("User Last Name",null);

       return new String[]  {mUserId, mUserName, mUserAvatarNumberString, mUserFirstName, mUserLastName};
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
    public static void setUserAvatarBase64String(String base64String){
        mUserCustomAvatarBase64String = base64String;
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



    public static String convertUserAvatarByteArrayToString(){
        return mUserCustomAvatarBase64String.toString();
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

    public static Uri getHostAvatarUri(){
        return mUserCustomAvatarUri;
    }

    public static void setHostAvatarUri(Uri userImgUri){
        mUserCustomAvatarUri = userImgUri;
    }
    public static  void setUserImgPathString(String userImagePathString){
        mUserImagePathString = userImagePathString;
    }

    public static String getUserImgPathString(){
        return mUserImagePathString;
    }

    public static String getmUserEmail() {
        return mUserEmail;
    }

    public static String getmUserBio() {
        return mUserBio;
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
