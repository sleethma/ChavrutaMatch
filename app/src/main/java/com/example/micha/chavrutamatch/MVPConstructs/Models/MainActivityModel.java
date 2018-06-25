package com.example.micha.chavrutamatch.MVPConstructs.Models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.example.micha.chavrutamatch.AcctLogin.AccountActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.Http.APIModels.MyChavrutas;
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.Data.Http.MyChavrutaAPI;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by micha on 2/26/2018.
 */

public class MainActivityModel extends AppCompatActivity implements MAContractMVP.Model {
    Context context;
    Context appContext;
    public MAContractMVP.Presenter callbackToPresenter;
    private String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    private static Uri mUserCustomAvatarUri;
    private static byte[] mUserCustomAvatarBase64ByteArray;
    public static String mUserLoginType = "none";
    private static String userAvatarNumberString = "0";
    private String userId, userName, userFirstName, userLastName, userPhoneNumber,
            userEmail, userCityState, userCustomAvatarUriString, userImagePathString, userCustomAvatarBase64String,
            userBio;
    private Disposable disposable;

    public SharedPreferences sp;
    AccountActivity accountActivity;
    ServerConnect serverConnectInstance;
    UserDetails userDetailsInstance;
    MAContractMVP.Presenter presenter;

    public ArrayList<HostSessionData> myChavrutasArrayList;
    public ArrayList<ServerResponse> myChavrutasAL;

    @Inject
    MyChavrutaAPI myChavrutaAPI;

    @Inject
    public MainActivityModel(Context appContext, SharedPreferences sp, UserDetails userDetailsInstance,
                             ServerConnect serverConnectInstance, AccountActivity accountActivity,
                             MyChavrutaAPI myChavrutaAPI) {

        this.sp = sp;
        this.appContext = appContext;
        this.accountActivity = accountActivity;
        this.serverConnectInstance = serverConnectInstance;
        this.userDetailsInstance = userDetailsInstance;
        this.myChavrutaAPI = myChavrutaAPI;
    }

    @Override
    public UserDetails getUserDetailsInstance() {
        return userDetailsInstance;
    }

    @Override
    public void checkModelForUserData() {
        if (context == null) {
            context = MainActivity.mContext;
        }
        ServerConnect serverConnect = new ServerConnect(context, userDetailsInstance);
        serverConnect.setCallbackToModel(MainActivityModel.this);
        serverConnect.execute("get UserDetails");
    }

    @Override
    public void handlePreviousUserDetailsServerCallback(String userDataJsonString) {
        if (userDataJsonString != null) {
            parseUserDetailsFromDB(userDataJsonString);
             userDetailsInstance.setAllUserDataToUserDetails(userName,userAvatarNumberString,userFirstName,userLastName
                    ,userPhoneNumber,userEmail,userCityState,userCustomAvatarUriString,
                     userId,userCustomAvatarBase64String,userBio);
            saveUserDetailsToSP();
            callbackToPresenter.refreshMaAvatarIfNeeded();
        }else{
            Intent intentToAddBio = new Intent(this.context, AddBio.class);
            intentToAddBio.putExtra("add new user to db", true);
            context.startActivity(intentToAddBio);
        }
    }

    //todo: create different SP objects holding previous users complete user data in JsonString, then can delete all below and store string to parse and populate later
    public void saveUserDetailsToSP() {
        String CUSTOM_AVATAR_NUMBER_STRING = "999";
        if (userDetailsInstance.userLoginType.equals("email"))
            putStringDataInSP("user email key", userEmail);
        if (userDetailsInstance.userLoginType.equals("phone"))
            putStringDataInSP("user phone number key", userPhoneNumber);

        putStringDataInSP("user name key", userName);
        putStringDataInSP("user Id key", userId);
        putStringDataInSP("user first name key", userFirstName);
        putStringDataInSP("user last name key", userLastName);
        putStringDataInSP("user avatar number key", userAvatarNumberString);
        putStringDataInSP("user bio key", userBio);
        putStringDataInSP("user city state key", userCityState);

        //clear sp if current avatar string not custom
        if (userAvatarNumberString.equals(CUSTOM_AVATAR_NUMBER_STRING)) {
            putStringDataInSP("user custom avatar key", userCustomAvatarUriString);
        } else {
            putStringDataInSP("user custom avatar key", null);
        }
        putStringDataInSP("user avatar base 64 key", userCustomAvatarBase64String);
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
        return new ServerConnect(context, userDetailsInstance);
    }

    @Override
    public void setMAModelValuesToUserDetails() {
        userDetailsInstance.setmUserPhoneNumber(userPhoneNumber);
        userDetailsInstance.setmUserEmail(userEmail);
        userDetailsInstance.setmUserName(userName);
        userDetailsInstance.setmUserFirstName(userFirstName);
        userDetailsInstance.setmUserLastName(userLastName);
        userDetailsInstance.setmUserAvatarNumberString(userAvatarNumberString);
        userDetailsInstance.setmUserBio(userBio);
        //todo: commented out below b/c interferes with login process, uncomment after testing
//        userDetailsInstance.setmUserId(userId);
        userDetailsInstance.setmUserCustomAvatarUriString(userCustomAvatarUriString);
        userDetailsInstance.setmUserCustomAvatarBase64String(userCustomAvatarBase64String);
        userDetailsInstance.setUserCityState(userCityState);
        userDetailsInstance.setmUserCustomAvatarUri(mUserCustomAvatarUri);
        userDetailsInstance.setmUserCustomAvatarBase64ByteArray(mUserCustomAvatarBase64ByteArray);
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
    public void requestMyChavrutas() {
        String userId = userDetailsInstance.getUserId();
        Call<MyChavrutas> call = myChavrutaAPI.getMyChavrutas(userId);
        call.enqueue(new Callback<MyChavrutas>() {

            @Override
            public void onResponse(Call<MyChavrutas> call, Response<MyChavrutas> response) {
                List<ServerResponse> myChavrutas = response.body().getMyChavrutasAL();
                createHostSessionDataObjects(myChavrutas);
            }

            @Override
            public void onFailure(Call<MyChavrutas> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void setCallbackToPresenter(MAContractMVP.Presenter presenter) {
        callbackToPresenter = presenter;
    }

    @Override
    public void observableRequestMyChavrutas() {
        disposable = myChavrutaAPI.getMyChavrutasObservable(userDetailsInstance.getUserId())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((MyChavrutas myChavrutasList) -> {
                    myChavrutasAL = (ArrayList) myChavrutasList.getMyChavrutasAL();
                    callbackToPresenter.setMyChavrutaData();
                });
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
        if (userAvatarNumberString == null) userAvatarNumberString = "0";

        userBio = getStringDataFromSP("user bio key");
        userId = getStringDataFromSP("user account id key");
        userCustomAvatarUriString = getStringDataFromSP("user custom avatar key");
        userCustomAvatarBase64String = getStringDataFromSP("user avatar base 64 key");
        setByteArrayFromString(userCustomAvatarBase64String);
        userCityState = getStringDataFromSP("user city state key");

        //convert String to Uri, covertToByteArray and save in @this
        if (userCustomAvatarUriString != null) {
            mUserCustomAvatarUri = Uri.parse(userCustomAvatarUriString);
        }
    }

    @Override
    public ArrayList<ServerResponse> getMyChavrutasAL() {
        return myChavrutasAL;
    }

    @Override
    public ServerResponse getMyChavrutasItem(int position) {
        return myChavrutasAL.get(position);
    }

    @Override
    public void parseJSONDataToArrayList(String jsonString) {
        this.jsonString = jsonString;
        ArrayList<HostSessionData> myChavrutasArrayList = new ArrayList<>();

        String chavrutaId, hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
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

    //sets user details from db call
    private void parseUserDetailsFromDB(String jsonString) {
        JSONObject jsonObject;
        JSONArray jsonArray;

        try {
            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");

            //loop through array and extract objects, adding them individually as setter objects,
            //and adding objects to list adapter.
            JSONObject jo = jsonArray.getJSONObject(0);
            userId = jo.getString("userId");
            userName = jo.getString("userName");
            userAvatarNumberString = jo.getString("userAvatarNumber");
            userCustomAvatarBase64String = jo.getString("customAvatarString");
            userFirstName = jo.getString("userFirstName");
            userLastName = jo.getString("userLastName");
            userPhoneNumber = jo.getString("userPhoneNumber");
            userEmail = jo.getString("userEmail");
            userCityState = jo.getString("userCityState");
            userBio = jo.getString("userBio");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void createHostSessionDataObjects(List<ServerResponse> myChavrutas) {

        myChavrutasArrayList = new ArrayList<>();

        String chavrutaId, hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                startTime, endTime, sefer, location, hostCityState, hostId,
                chavrutaRequest1Id, chavrutaRequest1Avatar, chavrutaRequest1Name,
                chavrutaRequest2Id, chavrutaRequest2Avatar, chavrutaRequest2Name,
                chavrutaRequest3Id, chavrutaRequest3Avatar, chavrutaRequest3Name,
                confirmed;

        for (ServerResponse chavruta : myChavrutas) {
            chavrutaId = chavruta.getChavrutaId();
            hostFirstName = chavruta.getHostFirstName();
            hostLastName = chavruta.getHostLastName();
            hostAvatarNumber = chavruta.getHostAvatarNumber();
            sessionMessage = chavruta.getSessionMessage();
            sessionDate = chavruta.getSessionDate();
            startTime = chavruta.getStartTime();
            endTime = chavruta.getEndTime();
            sefer = chavruta.getSefer();
            location = chavruta.getLocation();
            hostCityState = chavruta.getHostCityState();
            hostId = chavruta.getHostId();
            chavrutaRequest1Id = chavruta.getChavrutaRequest1();
            chavrutaRequest1Avatar = chavruta.getChavrutaRequest1Avatar();
            chavrutaRequest1Name = chavruta.getChavrutaRequest1Name();
            chavrutaRequest2Id = chavruta.getChavrutaRequest2();
            chavrutaRequest2Avatar = chavruta.getChavrutaRequest2Avatar();
            chavrutaRequest2Name = chavruta.getChavrutaRequest2Name();
            chavrutaRequest3Id = chavruta.getChavrutaRequest3();
            chavrutaRequest3Avatar = chavruta.getChavrutaRequest3Avatar();
            chavrutaRequest3Name = chavruta.getChavrutaRequest3Name();
            confirmed = chavruta.getConfirmed();

            //make user data object of UserDataSetter class
            HostSessionData myChavrutaData = new HostSessionData(chavrutaId, hostFirstName,
                    hostLastName, hostAvatarNumber, sessionMessage, sessionDate, startTime, endTime, sefer, location,
                    hostCityState, hostId, chavrutaRequest1Id, chavrutaRequest2Id, chavrutaRequest3Id,
                    chavrutaRequest1Avatar, chavrutaRequest1Name, chavrutaRequest2Avatar, chavrutaRequest2Name,
                    chavrutaRequest3Avatar, chavrutaRequest3Name,
                    confirmed);
            this.myChavrutasArrayList.add(myChavrutaData);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }
}

