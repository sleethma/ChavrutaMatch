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

public class UserDetails extends AppCompatActivity {

    private static String userId, currentUserId, userPassword, userAccountKitId, userFirstName, userName,
            userLastName, userPhoneNumber, userEmail, userCityState, userCustomAvatarBase64String,
            userImagePathString, userBio, userCustomAvatarUriString;

    private static SharedPreferences preferences;
    private static Context context;
    private static Uri userCustomAvatarUri;
    private static byte[] userCustomAvatarBase64ByteArray;
    public static String userLoginType = "none";
    private static boolean newUserKey;
    private static String userAvatarNumberString = "0";
    private String bestCustomUserAvatarType;


    public UserDetails(String id, String password, String phoneNumber, String email
    ) {
        userId = id;
        userPassword = password;
        userPhoneNumber = phoneNumber;
        userEmail = email;
    }

    //sets all user data from AddBio.class
    public void setAllUserDataToUserDetails(String... param) {
        userName = param[0];
        userAvatarNumberString = param[1];
        userFirstName = param[2];
        userLastName = param[3];
        userPhoneNumber = param[4];
        userEmail = param[5];
        userCityState = param[6];
        userCustomAvatarUriString = param[7];
        userId = param[8];
        userCustomAvatarBase64String = param[9];
        userBio = param[10];
}

    public void setAllAddBioUserDataToUserDetails(String... param) {
        userName = param[0];
        userAvatarNumberString = param[1];
        userFirstName = param[2];
        userLastName = param[3];
        userPhoneNumber = param[4];
        userEmail = param[5];
        userCityState = param[6];
        userCustomAvatarUriString = param[7];
        userBio = param[8];
    }


    public static boolean getNewUserKey() {
        return newUserKey;
    }

    public void setNewUserKey(boolean newUserKey) {
        this.newUserKey = newUserKey;
    }

    public void setLoginType(String loginType) {
        this.userLoginType = loginType;
    }

    public String getUserAvatarBase64String() {
        return userCustomAvatarBase64String;
    }

    public void setsApplicationContext(Context applicationContext) {
        context = applicationContext;
    }


    public void setmUserAvatarNumberString(String mUserAvatarNumberString) {
        userAvatarNumberString = mUserAvatarNumberString;
    }

    public void setmUserCustomAvatarUriString(String UserCustomAvatarUriString) {
        userCustomAvatarUriString = UserCustomAvatarUriString;
    }

    //        mUserCityState = prefs.getString("user city state key", null);
    public byte[] getUserCustomAvatarBase64ByteArray() {
        return userCustomAvatarBase64ByteArray;
    }

    public void setByteArrayFromString(String stringToDecode) {
        userCustomAvatarBase64ByteArray = Base64.decode(stringToDecode, Base64.DEFAULT);
    }

    public void setUserCityState(String userCityStateIn) {
        userCityState = userCityStateIn;
    }

    public String getUserCityState() {
        return userCityState;
    }

    public String getUserCallFormattedCityState() {
        return userCityState.replaceAll(" ", "%20");
    }

    @Nullable
    public void setUserId(String userIdIn) {
        userId = userIdIn;
    }

    public String getUserId() {
        return this.userId;
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static String getmUserPhoneNumber() {
        return userPhoneNumber;
    }

    public static void setmUserPhoneNumber(String userPhoneNumberIn) {
        userPhoneNumber = userPhoneNumberIn;
    }

    public static Uri getUserAvatarUri() {
        return userCustomAvatarUri;
    }

    public static String getmUserEmail() {
        return userEmail;
    }

    public static void setmUserEmail(String userEmailIn) {
        userEmail = userEmailIn;
    }

    public static String getmUserName() {
        return userName;
    }

    public static String getmUserAvatarNumberString() {
        return userAvatarNumberString;
    }

    public static String getmUserFirstName() {
        return userFirstName;
    }

    public static String getmUserLastName() {
        return userLastName;
    }

    public void setmUserName(String userNameIn) {
        userName = userNameIn;
    }

    public void setmUserFirstName(String mUserFirstName) {
        userFirstName = mUserFirstName;
    }

    public void setmUserLastName(String mUserLastName) {
        userLastName = mUserLastName;
    }

    public static void setmUserBio(String mUserBio) {
        userBio = mUserBio;
    }

    public static void setmUserPassword(String mUserPassword) {
        userPassword = mUserPassword;
    }

    public static void setmUserCityState(String mUserCityState) {
        mUserCityState = mUserCityState;
    }

    public static void setmUserCustomAvatarUri(Uri mUserCustomAvatarUri) {
        userCustomAvatarUri = mUserCustomAvatarUri;
    }

    public static void setmUserCustomAvatarBase64String(String mUserCustomAvatarBase64String) {
        userCustomAvatarBase64String = mUserCustomAvatarBase64String;
    }

    public void setmUserCustomAvatarBase64ByteArray(byte[] mUserCustomAvatarBase64ByteArray) {
        this.userCustomAvatarBase64ByteArray = mUserCustomAvatarBase64ByteArray;
    }



    public static String getUserAccountKitId() {
        return userAccountKitId;
    }

    public static void setUserAccountKitId(String userId) {
        userAccountKitId = userId;
    }
}
