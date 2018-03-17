package com.example.micha.chavrutamatch.Data;

import android.support.annotation.Nullable;
import android.util.Base64;

/**
 * Created by micha on 8/14/2017.
 */

//class stores hostsession data for mychavruta and HostSelect activity's population
public class HostSessionData {


    @Nullable
    private String mChavrutaRequest1Avatar, mChavrutaRequest1Name,mChavrutaRequest2Avatar,mChavrutaRequest2Name,
    mChavrutaRequest3Avatar, mChavrutaRequest3Name, mSefer, mLocation, mHostId, mchavrutaRequest1Id, mchavrutaRequest2Id,
            mchavrutaRequest3Id, mChavrutaId, mHostFirstName, mHostLastName, mHostAvatarNumber,
    mSessionMessage, mStartTime, mSessionDate, mHostCityState, mEndTime;

    private byte[] mHostCustomAvatarByteArray;

    @Nullable
    private String mConfirmed;
    public boolean requestOneConfirmed = true;
    public boolean requestTwoConfirmed = true;
    public boolean requestThreeConfirmed = true;




    public HostSessionData(String chavrutaId, String hostFirstName, String hostLastName, String hostAvatarNumber, String sessionMessage,
                           String sessionDate, String startTime, String endTime, String sefer,
                           String location, String hostCityState, String hostId, String chavrutaRequest1Id, String chavrutaRequest2Id,
                           String chavrutaRequest3Id, String chavrutaRequest1Avatar, String chavrutaRequest1Name,
                           String chavrutaRequest2Avatar, String chavrutaRequest2Name,
                           String chavrutaRequest3Avatar, String chavrutaRequest3Name, String confirmed) {

        setmChavrutaId(chavrutaId);
        setmHostFirstName(hostFirstName);
        setmHostLastName(hostLastName);

        //stores custom avatar as byte array for arraylist
        if(hostAvatarNumber.length() > AvatarImgs.avatarImgList.size() &&
                !hostAvatarNumber.equals("999"))
            setmHostCustomAvatarByteArray(hostAvatarNumber);

        setmHostAvatarNumber(hostAvatarNumber);
        setmSessionMessage(sessionMessage);
        setmSessionDate(sessionDate);
        setmStartTime(startTime);
        setmEndTime(endTime);
        setmSefer(sefer);
        setmLocation(location);
        setmHostCityState(hostCityState);
        setmHostId(hostId);
        setMchavrutaRequest1Id(chavrutaRequest1Id);
        setMchavrutaRequest2Id(chavrutaRequest2Id);
        setMchavrutaRequest3Id(chavrutaRequest3Id);
        setmConfirmed(confirmed);
        setmChavrutaRequest1Avatar(chavrutaRequest1Avatar);
        setmChavrutaRequest2Avatar(chavrutaRequest2Avatar);
        setmChavrutaRequest3Avatar(chavrutaRequest3Avatar);
        setmChavrutaRequest1Name(chavrutaRequest1Name);
        setmChavrutaRequest2Name(chavrutaRequest2Name);
        setmChavrutaRequest3Name(chavrutaRequest3Name);
    }

    public void setmHostCustomAvatarByteArray(String byteArrayToDecode){
        mHostCustomAvatarByteArray = Base64.decode(byteArrayToDecode, Base64.DEFAULT);
    }

    public byte[] getmHostCustomAvatarByteArray(){
      return  mHostCustomAvatarByteArray;
    }

    public void setmHostCityState(String mHostCityState) {
        this.mHostCityState = mHostCityState;
    }

    public String getmChavrutaRequestAvatar(int requestNumber) {
        if(requestNumber == 1){
            return mChavrutaRequest1Avatar;
        }else if(requestNumber == 2){
            return mChavrutaRequest2Avatar;
        }else {
            return mChavrutaRequest3Avatar;
        }
    }
    public String getmChavrutaRequest2Avatar() {
        return mChavrutaRequest2Avatar;
    }
    public String getmChavrutaRequest3Avatar() {
        return mChavrutaRequest3Avatar;
    }


    public void setmChavrutaRequest1Avatar(String mChavrutaRequest1Avatar) {
        this.mChavrutaRequest1Avatar = mChavrutaRequest1Avatar;
    }

    public String getmChavrutaRequest1Name() {
        return mChavrutaRequest1Name;
    }

    public void setmChavrutaRequest1Name(String mChavrutaRequest1Name) {
        this.mChavrutaRequest1Name = mChavrutaRequest1Name;
    }


    public void setmChavrutaRequest2Avatar(String mChavrutaRequest2Avatar) {
        this.mChavrutaRequest2Avatar = mChavrutaRequest2Avatar;
    }

    public String getmChavrutaRequest2Name() {
        return mChavrutaRequest2Name;
    }

    public void setmChavrutaRequest2Name(String mChavrutaRequest2Name) {
        this.mChavrutaRequest2Name = mChavrutaRequest2Name;
    }


    public void setmChavrutaRequest3Avatar(String mChavrutaRequest3Avatar) {
        this.mChavrutaRequest3Avatar = mChavrutaRequest3Avatar;
    }

    public String getmChavrutaRequest3Name() {
        return mChavrutaRequest3Name;
    }

    public void setmChavrutaRequest3Name(String mChavrutaRequest3Name) {
        this.mChavrutaRequest3Name = mChavrutaRequest3Name;
    }

    public void setmHostAvatarNumber(String mHostAvatarNumber){
        this.mHostAvatarNumber = mHostAvatarNumber;
    }

    public static byte[] getByteArrayFromString(String stringToDecode){
        return Base64.decode(stringToDecode, Base64.DEFAULT);
    }

    public String getmHostAvatarNumber(){
        return mHostAvatarNumber;
    }


    public String[] getAllStringHostDataForMyChavruta() {
        return new String[]{mHostFirstName, mHostLastName, mSessionMessage, mSessionDate, mStartTime,
                mEndTime, mSefer, mLocation};
    }

    public void setRequestConfirmed(Boolean value, int requestNumber){
        if(requestNumber ==1){
            requestOneConfirmed = value;
        }else if(requestNumber == 2){
            requestTwoConfirmed = value;
        }else{
            requestThreeConfirmed = value;
        }
    }
    public String getmConfirmed() {
        return mConfirmed;
    }

    public void setmConfirmed(String confirmed) {
        mConfirmed = confirmed;
    }

    public void setmChavrutaId(String chavrutaId) {
        mChavrutaId = chavrutaId;
    }


    public String getmHostFirstName() {
        return mHostFirstName;
    }

    public String getmChavrutaId() {
        return mChavrutaId;
    }

    public void setmHostId(String hostId) {
        mHostId = hostId;
    }
    public String getmHostId(){
        return mHostId;
    }

    public void setMchavrutaRequest1Id(String chavrutaRequest1) {
        mchavrutaRequest1Id = chavrutaRequest1;
    }

    public void setMchavrutaRequest2Id(String mchavrutaRequest2Id) {
        this.mchavrutaRequest2Id = mchavrutaRequest2Id;
    }

    public void setMchavrutaRequest3Id(String mchavrutaRequest3Id) {
        this.mchavrutaRequest3Id = mchavrutaRequest3Id;
    }

    public String getMchavrutaRequest1Id() {
        return mchavrutaRequest1Id;
    }


    public String getMchavrutaRequest2Id() {
        return mchavrutaRequest2Id;
    }

    public String getMchavrutaRequest3Id() {
        return mchavrutaRequest3Id;
    }


    public String getmHostLastName() {
        return mHostLastName;
    }

    public String getmSessionMessage() {
        return mSessionMessage;
    }

    public String getmSessionDate() {
        return mSessionDate;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public String getmSefer() {
        return mSefer;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmHostFirstName(String mHostFirstName) {
        this.mHostFirstName = mHostFirstName;
    }

    public void setmHostLastName(String mHostLastName) {
        this.mHostLastName = mHostLastName;
    }

    public void setmSessionMessage(String mSessionMessage) {
        this.mSessionMessage = mSessionMessage;
    }

    public void setmSessionDate(String mSessionDate) {
        this.mSessionDate = mSessionDate;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmSefer(String mSefer) {
        this.mSefer = mSefer;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
