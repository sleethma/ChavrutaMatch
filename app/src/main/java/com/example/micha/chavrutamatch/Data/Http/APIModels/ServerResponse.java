
package com.example.micha.chavrutamatch.Data.Http.APIModels;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    @SerializedName("chavruta_id")
    @Expose
    private String chavrutaId;
    @SerializedName("hostFirstName")
    @Expose
    private String hostFirstName;
    @SerializedName("hostLastName")
    @Expose
    private String hostLastName;
    @SerializedName("hostAvatarNumber")
    @Expose
    private String hostAvatarNumber;
    @SerializedName("sessionMessage")
    @Expose
    private String sessionMessage;
    @SerializedName("sessionDate")
    @Expose
    private String sessionDate;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("sefer")
    @Expose
    private String sefer;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("hostCityState")
    @Expose
    private String hostCityState;
    @SerializedName("host_id")
    @Expose
    private String hostId;
    @SerializedName("chavruta_request_1")
    @Expose
    private String chavrutaRequest1;
    @SerializedName("chavruta_request_1_avatar")
    @Expose
    private String chavrutaRequest1Avatar;
    @SerializedName("chavruta_request_1_name")
    @Expose
    private String chavrutaRequest1Name;
    @SerializedName("chavruta_request_2")
    @Expose
    private String chavrutaRequest2;
    @SerializedName("chavruta_request_2_avatar")
    @Expose
    private String chavrutaRequest2Avatar;
    @SerializedName("chavruta_request_2_name")
    @Expose
    private String chavrutaRequest2Name;
    @SerializedName("chavruta_request_3")
    @Expose
    private String chavrutaRequest3;
    @SerializedName("chavruta_request_3_avatar")
    @Expose
    private String chavrutaRequest3Avatar;
    @SerializedName("chavruta_request_3_name")
    @Expose
    private String chavrutaRequest3Name;
    @SerializedName("confirmed")
    @Expose
    private String confirmed;

    private String mChavrutaRequest1Avatar, mChavrutaRequest2Avatar, mChavrutaRequest3Avatar;
    public boolean requestOneConfirmed = true;
    public boolean requestTwoConfirmed = true;
    public boolean requestThreeConfirmed = true;
    private byte[] mHostCustomAvatarByteArray;


    public String getChavrutaId() {
        return chavrutaId;
    }

    public void setChavrutaId(String chavrutaId) {
        this.chavrutaId = chavrutaId;
    }

    public String getHostFirstName() {
        return hostFirstName;
    }

    public void setHostFirstName(String hostFirstName) {
        this.hostFirstName = hostFirstName;
    }

    public String getHostLastName() {
        return hostLastName;
    }

    public void setHostLastName(String hostLastName) {
        this.hostLastName = hostLastName;
    }

    public String getHostAvatarNumber() {
        return hostAvatarNumber;
    }

    public void setHostAvatarNumber(String hostAvatarNumber) {
        this.hostAvatarNumber = hostAvatarNumber;
    }

    public String getSessionMessage() {
        return sessionMessage;
    }

    public void setSessionMessage(String sessionMessage) {
        this.sessionMessage = sessionMessage;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSefer() {
        return sefer;
    }

    public void setSefer(String sefer) {
        this.sefer = sefer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostCityState() {
        return hostCityState;
    }

    public void setHostCityState(String hostCityState) {
        this.hostCityState = hostCityState;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getChavrutaRequest1() {
        return chavrutaRequest1;
    }

    public void setChavrutaRequest1(String chavrutaRequest1) {
        this.chavrutaRequest1 = chavrutaRequest1;
    }

    public String getChavrutaRequest1Avatar() {
        return chavrutaRequest1Avatar;
    }

    public void setChavrutaRequest1Avatar(String chavrutaRequest1Avatar) {
        this.chavrutaRequest1Avatar = chavrutaRequest1Avatar;
    }

    public String getChavrutaRequest1Name() {
        return chavrutaRequest1Name;
    }

    public void setChavrutaRequest1Name(String chavrutaRequest1Name) {
        this.chavrutaRequest1Name = chavrutaRequest1Name;
    }

    public String getChavrutaRequest2() {
        return chavrutaRequest2;
    }

    public void setChavrutaRequest2(String chavrutaRequest2) {
        this.chavrutaRequest2 = chavrutaRequest2;
    }

    public String getChavrutaRequest2Avatar() {
        return chavrutaRequest2Avatar;
    }

    public void setChavrutaRequest2Avatar(String chavrutaRequest2Avatar) {
        this.chavrutaRequest2Avatar = chavrutaRequest2Avatar;
    }

    public String getChavrutaRequest2Name() {
        return chavrutaRequest2Name;
    }

    public void setChavrutaRequest2Name(String chavrutaRequest2Name) {
        this.chavrutaRequest2Name = chavrutaRequest2Name;
    }

    public String getChavrutaRequest3() {
        return chavrutaRequest3;
    }

    public void setChavrutaRequest3(String chavrutaRequest3) {
        this.chavrutaRequest3 = chavrutaRequest3;
    }

    public String getChavrutaRequest3Avatar() {
        return chavrutaRequest3Avatar;
    }

    public void setChavrutaRequest3Avatar(String chavrutaRequest3Avatar) {
        this.chavrutaRequest3Avatar = chavrutaRequest3Avatar;
    }

    public String getChavrutaRequest3Name() {
        return chavrutaRequest3Name;
    }

    public void setChavrutaRequest3Name(String chavrutaRequest3Name) {
        this.chavrutaRequest3Name = chavrutaRequest3Name;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public void setRequestConfirmed(Boolean value, int requestNumber) {
        if (requestNumber == 1) {
            requestOneConfirmed = value;
        } else if (requestNumber == 2) {
            requestTwoConfirmed = value;
        } else {
            requestThreeConfirmed = value;
        }
    }

    public String getmChavrutaRequestAvatar(int requestNumber) {
        if (requestNumber == 1) {
            return mChavrutaRequest1Avatar;
        } else if (requestNumber == 2) {
            return mChavrutaRequest2Avatar;
        } else {
            return mChavrutaRequest3Avatar;
        }
    }

    public byte[] getHostCustomAvatarByteArray() {
        mHostCustomAvatarByteArray = Base64.decode(getHostAvatarNumber(), Base64.DEFAULT);
        return mHostCustomAvatarByteArray;
    }

    public void setmHostCustomAvatarByteArray(String byteArrayToDecode) {
        mHostCustomAvatarByteArray = Base64.decode(byteArrayToDecode, Base64.DEFAULT);
    }

    public static byte[] getByteArrayFromString(String stringToDecode){
        return Base64.decode(stringToDecode, Base64.DEFAULT);
    }

}
