package com.example.micha.chavrutamatch.MVPConstructs.Models;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Base64;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by micha on 2/26/2018.
 */

public class MainActivityModel implements MAContractMVP.Model {
    private String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    private static Uri mUserCustomAvatarUri;
    private static byte[] mUserCustomAvatarBase64ByteArray;
    public static String mUserLoginType = "none";
    private static String userAvatarNumberString = "0";
    private static String userId, userName, userFirstName, userLastName, userPhoneNumber,
            userEmail, userCityState, userCustomAvatarUriString, userImagePathString, userCustomAvatarBase64String,
            userBio;

    public SharedPreferences sp;
    AccountActivity accountActivity;

    ServerConnect serverConnectInstance;


    public ArrayList<HostSessionData> myChavrutasArrayList;

    @Inject
    public MainActivityModel(SharedPreferences sp, AccountActivity accountActivity, ServerConnect serverConnectInstance) {
        this.sp = sp;
        this.accountActivity = accountActivity;
        this.serverConnectInstance = serverConnectInstance;
    }

    @Override
    public boolean verifyCurrentUserDataSavedInSP() {
        return (getStringDataFromSP("user name key") == null);
    }

    @Override
    public String getStringDataFromSP(String key) {
        return sp.getString(key, null);
    }

    @Override
    public ServerConnect getServerConnectInstance() {
        return serverConnectInstance;
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
    public void initAccountKit() {
        accountActivity.setAccountKitAcct();
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
    public void setUserDataFromSPToModel() {
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

    @Override
    public ArrayList<HostSessionData> getMyChavrutasAL() {
        return myChavrutasArrayList;
    }

    @Override
    public HostSessionData getMyChavrutasArrayListItem(int position) {
        return myChavrutasArrayList.get(position);
    }

    @Override
    public void parseJSONDataToArrayList(String jsonString) {
        this.jsonString = jsonString;
        String chavrutaId;
        ArrayList<HostSessionData> myChavrutasArrayList = new ArrayList<>();

        String hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                startTime, endTime, sefer, location, hostCityState, hostId,
                chavrutaRequest1Id, chavrutaRequest1Avatar, chavrutaRequest1Name,
                chavrutaRequest2Id, chavrutaRequest2Avatar, chavrutaRequest2Name,
                chavrutaRequest3Id, chavrutaRequest3Avatar, chavrutaRequest3Name,
                confirmed;
        try {

            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");

            //loop through array and extract objects, adding them individually as setter objects,
            //and adding objects to list adapter.
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                chavrutaId = jo.getString("chavruta_id");
                hostFirstName = jo.getString("hostFirstName");
                hostLastName = jo.getString("hostLastName");
                hostAvatarNumber = jo.getString("hostAvatarNumber");
                sessionMessage = jo.getString("sessionMessage");
                sessionDate = jo.getString("sessionDate");
                startTime = jo.getString("startTime");
                endTime = jo.getString("endTime");
                sefer = jo.getString("sefer");
                location = jo.getString("location");
                hostCityState = jo.getString("hostCityState");
                hostId = jo.getString("host_id");
                chavrutaRequest1Id = jo.getString("chavruta_request_1");
                chavrutaRequest1Avatar = jo.getString("chavruta_request_1_avatar");
                chavrutaRequest1Name = jo.getString("chavruta_request_1_name");
                chavrutaRequest2Id = jo.getString("chavruta_request_2");
                chavrutaRequest2Avatar = jo.getString("chavruta_request_2_avatar");

                chavrutaRequest2Name = jo.getString("chavruta_request_2_name");
                chavrutaRequest3Id = jo.getString("chavruta_request_3");
                chavrutaRequest3Avatar = jo.getString("chavruta_request_3_avatar");
                chavrutaRequest3Name = jo.getString("chavruta_request_3_name");
                confirmed = jo.getString("confirmed");

                //make user data object of UserDataSetter class
                HostSessionData myChavrutaData = new HostSessionData(chavrutaId, hostFirstName,
                        hostLastName, hostAvatarNumber, sessionMessage, sessionDate, startTime, endTime, sefer, location,
                        hostCityState, hostId, chavrutaRequest1Id, chavrutaRequest2Id, chavrutaRequest3Id,
                        chavrutaRequest1Avatar, chavrutaRequest1Name, chavrutaRequest2Avatar, chavrutaRequest2Name,
                        chavrutaRequest3Avatar, chavrutaRequest3Name,
                        confirmed);
                myChavrutasArrayList.add(myChavrutaData);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.myChavrutasArrayList = myChavrutasArrayList;
    }


}

