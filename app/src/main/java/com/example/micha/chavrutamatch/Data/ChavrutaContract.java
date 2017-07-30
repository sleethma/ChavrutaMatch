package com.example.micha.chavrutamatch.Data;

import android.provider.BaseColumns;

/**
 * Created by micha on 7/14/2017.
 */

public class ChavrutaContract {

    public class ChavrutaHostEntry implements BaseColumns {

        //Column Headings for Chavrusa Host Session Creation And Match Confirmation
        public static final String TABLE_NAME = "chavrutaHosts"; //string
        public static final String HOST_FIRST_NAME = "hostFirstName"; //string
        public static final String HOST_LAST_NAME = "hostLastName"; //string
        public static final String HOST_USER_NAME = "hostUserName"; //string
        public static final String SESSION_MESSAGE = "sessionMessage"; //string
        public static final String SESSION_DATE = "sessionDate"; //string
        public static final String START_TIME = "startTime";
        public static final String END_TIME = "endTime";
        public static final String SEFER = "sefer"; //string
        public static final String LOCATION = "chavrutaHosts"; //string
        public static final String GUEST_FIRST_NAME = "guestFirstName"; //string
        public static final String GUEST_LAST_NAME = "guestLastName"; //string
        public static final String SESSION_CONFIRMED_BOOL = "sessionConfirmedBool";
        public static final String SESSION_FREQUENCY = "sessionFrequency"; //int
        public static final String SESSION_CREATION_DATE = "timestamp"; //long
        public static final String SESSION_ID = "sessionId"; //string
        public static final String HOST_AVATAR = "hostAvatar"; //string
        public static final String REQUESTING_GUEST_PROFILE = "hostSessionConnectId"; //array



    }

    public class UserProfileEntry implements BaseColumns{
        public static final String TABLE_NAME = "chavrutaUsers"; //string
        public static final String USER_FIRST_NAME = "userFirstName"; //string
        public static final String USER_LAST_NAME = "userLastName"; //string
        public static final String USER_NAME = "userName"; //string
        public static final String USER_PASSWORD = "userPassword"; //string
        public static final String BIO = "bio"; //string
        public static final String SEFARIM = "sefarim"; //string
        public static final String PROFILE_PIC = "profilePic"; //int
        public static final String UNIQUE_ID = "uniqueId"; //string
        public static final String GUEST_SESSION_CONNECT_ID = "guestSessionConnectId"; //string
        public static final String NEW_USER_CREATION_DATE = "newUserCreationDate"; //long
    }
}
