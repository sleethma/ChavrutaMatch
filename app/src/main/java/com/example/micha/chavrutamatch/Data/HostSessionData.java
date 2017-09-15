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


    public HostSessionData(String chavrutaId, String hostFirstName, String hostLastName, String sessionMessage,
                           String sessionDate, String startTime, String endTime, String sefer,
                           String location, String hostId, String chavrutaRequest1, String chavrutaRequest2,
                           String chavrutaRequest3) {

        setmChavrutaId(chavrutaId);
        setmHostFirstName(hostFirstName);
        setmHostLastName(hostLastName);
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
    }

    public String[] getAllStringHostDataForMyChavruta() {
        return new String[]{mHostFirstName, mHostLastName, mSessionMessage, mSessionDate, mStartTime,
                mEndTime, mSefer, mLocation};
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

    public void setMchavrutaRequest2(@Nullable String mchavrutaRequest2) {
        this.mchavrutaRequest2 = mchavrutaRequest2;
    }

    public void setMchavrutaRequest3(@Nullable String mchavrutaRequest3) {
        this.mchavrutaRequest3 = mchavrutaRequest3;
    }

    public String getMchavrutaRequest1() {
        return mchavrutaRequest1;
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
