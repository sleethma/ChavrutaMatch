package com.example.micha.chavrutamatch.Data;

import android.support.annotation.Nullable;

/**
 * Created by micha on 8/14/2017.
 */

//class stores hostsession data for mychavruta and HostSelect activity's population
public class HostSessionData {
    @Nullable
    String mHostFirstName,mHostLastName,mSessionMessage,mSessionDate,
            mStartTime, mEndTime,  mSefer , mLocation;

    public HostSessionData(String hostFirstName, String hostLastName,String sessionMessage,
                    String sessionDate, String startTime, String endTime, String sefer ,
                    String location){
        setmHostFirstName(hostFirstName);
        setmHostLastName(hostLastName);
        setmSessionMessage(sessionMessage);
        setmSessionDate(sessionDate);
        setmStartTime(startTime);
        setmEndTime(endTime);
        setmSefer(sefer);
        setmLocation(location);
    }

    public String[] getAllHostDataForMyChavruta(){
        return new String[] {mHostFirstName, mHostLastName, mSessionMessage, mSessionDate, mStartTime,
        mEndTime, mSefer, mLocation};
    }

    public String getmHostFirstName() {
        return mHostFirstName;
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
