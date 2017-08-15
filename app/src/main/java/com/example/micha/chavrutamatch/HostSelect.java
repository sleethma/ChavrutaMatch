package com.example.micha.chavrutamatch;

import android.animation.Animator;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/22/2017.
 */

public class HostSelect extends AppCompatActivity {

    @BindView(R.id.fl_host_pic)
    FrameLayout flHostPic;
    @BindView(R.id.b_host_chavruta)
    ListView allHostsList;
    @BindView(R.id.all_hosts_list_view)


    private String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;


    OpenChavrutaAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_host_listview);
        ButterKnife.bind(this);
        jsonString = getIntent().getExtras().getString("jsonToParseKey");
        //set adapter on listview
        //TODO: check if the resource is necessary
        mAdapter = new OpenChavrutaAdapter(this, R.layout.open_host_list_item);
        allHostsList.setAdapter(mAdapter);

        //parses JSON entry
        parseJSONEntry();
    }

    public void parseJSONEntry(){
        String hostFirstName,hostLastName,sessionMessage,sessionDate,
                startTime, endTime,  sefer , location;
        try {
            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");

            //loop through array and extract objects, adding them individually as setter objects,
            //and adding objects to list adapter.
            int count = 0;
            while(count < jsonArray.length()){
                JSONObject jo = jsonArray.getJSONObject(count);
                hostFirstName = jo.getString("hostFirstName");
                hostLastName = jo.getString("hostLastName");
                sessionMessage = jo.getString("sessionMessage");
                sessionDate = jo.getString("sessionDate");
                startTime = jo.getString("startTime");
                endTime = jo.getString("endTime");
                sefer = jo.getString("sefer");
                location = jo.getString("location");


                //make user data object of UserDataSetter class
                HostSessionData hostClassData = new HostSessionData(hostFirstName,hostLastName,sessionMessage,sessionDate,
                        startTime, endTime,  sefer , location);
                mAdapter.add(hostClassData);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
