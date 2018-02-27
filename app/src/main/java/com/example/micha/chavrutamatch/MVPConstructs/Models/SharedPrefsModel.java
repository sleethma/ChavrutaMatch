package com.example.micha.chavrutamatch.MVPConstructs.Models;

import android.content.SharedPreferences;

import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;

import javax.inject.Inject;

/**
 * Created by micha on 2/26/2018.
 */

public class SharedPrefsModel {

    private SharedPreferences sp;

    @Inject
    public SharedPrefsModel(SharedPreferences sp) {
        this.sp = sp;
    }

    public void putIntDataInSP(String key, int intData) {
        sp.edit().putInt(key, intData).apply();
    }

    public int getIntDataFromSP(String key) {
        return sp.getInt(key, -1);
    }

    public void putStringDataInSP(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public String getStringDataFromSP(String key) {
        return sp.getString(key, "null");
    }

    public void putBooleanDataInSP(String key, boolean value){
        sp.edit().putBoolean(key, value).apply();
    }

}
