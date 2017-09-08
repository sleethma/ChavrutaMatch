//package com.example.micha.chavrutamatch;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.Preference;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.ListView;
//
//import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
//
//import java.sql.Timestamp;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//import static android.R.attr.delay;
//import static java.lang.Thread.sleep;
//
///**
// * Created by micha on 9/1/2017.
// */
//
//
//public class MyChavrutas {
//
//
//    //date requested
//    private long mRequestDate;
//    //date confirmed by both chavrutas
//    private long mdateCreated;
//    //the AutoINCR value in ChavrutaHosts server Db
//    private int classId;
//    //all userData associated with class
//    private String[] mUserDataArray;
//    private SharedPreferences mPreferences;
//    private String[] mAllHostSessionData;
//
//
//    MyChavrutas(long currentTime, String[] allHostSessionData) {
//        mdateCreated = currentTime;
//        mAllHostSessionData = allHostSessionData;
//        mUserDataArray = UserDetails.getUserDataForChavruta();
//    }
//
//}
