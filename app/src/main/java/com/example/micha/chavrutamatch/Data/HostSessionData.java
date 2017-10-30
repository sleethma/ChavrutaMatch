package com.example.micha.chavrutamatch.Data;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by micha on 8/14/2017.
 */

//class stores hostsession data for mychavruta and HostSelect activity's population
public class HostSessionData {
    @Nullable
    private String mChavrutaId;
    @Nullable
    private String mHostFirstName;
    @Nullable
    private String mHostLastName;

    private String mHostAvatarNumber;
    @Nullable
    private String mSessionMessage;
    @Nullable
    private String mSessionDate;
    @Nullable
    private String mStartTime;
    @Nullable
    private String mEndTime;
    @Nullable
    private String mSefer;
    @Nullable
    private String mLocation;
    @Nullable
    private String mHostId;
    @Nullable
    private String mchavrutaRequest1;
    @Nullable
    private String mchavrutaRequest2;
    @Nullable
    private String mchavrutaRequest3;

    @Nullable
    private String mChavrutaRequest1Avatar, mChavrutaRequest1Name,mChavrutaRequest2Avatar,mChavrutaRequest2Name,
    mChavrutaRequest3Avatar, mChavrutaRequest3Name;
@Nullable
    private String mConfirmed;

    public Boolean requestOneConfirmed = true;
    public Boolean requestTwoConfirmed = true;
    public Boolean requestThreeConfirmed = true;


    public HostSessionData(String chavrutaId, String hostFirstName, String hostLastName, String hostAvatarNumber, String sessionMessage,
                           String sessionDate, String startTime, String endTime, String sefer,
                           String location, String hostId, String chavrutaRequest1, String chavrutaRequest2,
                           String chavrutaRequest3, String chavrutaRequest1Avatar, String chavrutaRequest1Name,
                           String chavrutaRequest2Avatar, String chavrutaRequest2Name,
                           String chavrutaRequest3Avatar, String chavrutaRequest3Name, String confirmed) {

        setmChavrutaId(chavrutaId);
        setmHostFirstName(hostFirstName);
        setmHostLastName(hostLastName);
        setmHostAvatarNumber(hostAvatarNumber);
        setmSessionMessage(sessionMessage);
        setmSessionDate(sessionDate);
        setmStartTime(startTime);
        setmEndTime(endTime);
        setmSefer(sefer);
        setmLocation(location);
        setmHostId(hostId);
        setMchavrutaRequest1(chavrutaRequest1);
        setMchavrutaRequest2(chavrutaRequest2);
        setMchavrutaRequest3(chavrutaRequest3);
        setmConfirmed(confirmed);
        setmChavrutaRequest1Avatar(chavrutaRequest1Avatar);
        setmChavrutaRequest2Avatar(chavrutaRequest2Avatar);
        setmChavrutaRequest3Avatar(chavrutaRequest3Avatar);
        setmChavrutaRequest1Name(chavrutaRequest1Name);
        setmChavrutaRequest2Name(chavrutaRequest2Name);
        setmChavrutaRequest3Name(chavrutaRequest3Name);
    }

    public String getmChavrutaRequest1Avatar() {
        return mChavrutaRequest1Avatar;
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

    public String getmChavrutaRequest2Avatar() {
        return mChavrutaRequest2Avatar;
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

    public String getmChavrutaRequest3Avatar() {
        return mChavrutaRequest3Avatar;
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

    public String getmHostAvatarNumber(){
        return mHostAvatarNumber;
    }


    public String[] getAllStringHostDataForMyChavruta() {
        return new String[]{mHostFirstName, mHostLastName, mSessionMessage, mSessionDate, mStartTime,
                mEndTime, mSefer, mLocation};
    }

    public void setRequestOneConfirmed(Boolean value){
        requestOneConfirmed = value;
    }
    public void setRequestTwoConfirmed(Boolean value){
        requestTwoConfirmed = value;
    }
    public void setRequestThreeConfirmed(Boolean value){
        requestThreeConfirmed = value;
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

    public void setMchavrutaRequest1(String chavrutaRequest1) {
        mchavrutaRequest1 = chavrutaRequest1;
    }

    public void setMchavrutaRequest2( String mchavrutaRequest2) {
        this.mchavrutaRequest2 = mchavrutaRequest2;
    }

    public void setMchavrutaRequest3( String mchavrutaRequest3) {
        this.mchavrutaRequest3 = mchavrutaRequest3;
    }

    public String getMchavrutaRequest1() {
        return mchavrutaRequest1;
    }

    public void getMchavrutaRequest_1_Data(){

    }


    public String getMchavrutaRequest2() {
        return mchavrutaRequest2;
    }

    public String getMchavrutaRequest3() {
        return mchavrutaRequest3;
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
