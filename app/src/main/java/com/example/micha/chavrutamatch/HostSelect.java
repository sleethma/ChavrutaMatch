package com.example.micha.chavrutamatch;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.ServerConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.micha.chavrutamatch.Data.HostSessionData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/22/2017.
 */

public class HostSelect extends AppCompatActivity {

    @BindView(R.id.fl_host_pic)
    FrameLayout flHostPic;
    // @BindView(R.id.b_host_chavruta) ImageButton hostChavruta;
    @BindView(R.id.all_hosts_list_view)
    RecyclerView allHostsList;
    @BindView(R.id.iv_scroll_open_host)
    ImageView scrollImg;
    ArrayList<HostSessionData> openChavrutaArrayList;
    static Context mContext;


    public String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String userId;


    OpenChavrutaAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_host_listview);
        ButterKnife.bind(this);
        userId = UserDetails.getmUserId();
        mContext = this;

        //constructs the data source
        openChavrutaArrayList = new ArrayList<>();

        //accesses JSON from ServerConnect
        jsonString = getIntent().getExtras().getString("jsonKey");

        //parses JSON entry
        parseJSONEntry();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allHostsList.setLayoutManager(layoutManager);


        //attaches data source to adapter
        mAdapter = new OpenChavrutaAdapter(this, openChavrutaArrayList);

        allHostsList.setAdapter(mAdapter);

        scrollImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCircularRevealAnim(v);
            }
        });
    }

    //@ var chavrutaId = autoInc from db
    public void parseJSONEntry() {
        String chavrutaId;

        String hostFirstName, hostLastName, sessionMessage, sessionDate,
                startTime, endTime, sefer, location, hostId,
                chavrutaRequest1,chavrutaRequest1Avatar, chavrutaRequest1Name,
                chavrutaRequest2, chavrutaRequest2Avatar,chavrutaRequest2Name,
                chavrutaRequest3,chavrutaRequest3Avatar, chavrutaRequest3Name,confirmed;
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
                sessionMessage = jo.getString("sessionMessage");
                sessionDate = jo.getString("sessionDate");
                startTime = jo.getString("startTime");
                endTime = jo.getString("endTime");
                sefer = jo.getString("sefer");
                location = jo.getString("location");
                hostId = jo.getString("host_id");
                chavrutaRequest1 = jo.getString("chavruta_request_1");
                chavrutaRequest1Avatar = jo.getString("chavruta_request_1_avatar");
                chavrutaRequest1Name = jo.getString("chavruta_request_1_name");
                chavrutaRequest2 = jo.getString("chavruta_request_2");
                chavrutaRequest2Avatar = jo.getString("chavruta_request_2_avatar");
                chavrutaRequest2Name = jo.getString("chavruta_request_2_name");
                chavrutaRequest3 = jo.getString("chavruta_request_3");
                chavrutaRequest3Avatar = jo.getString("chavruta_request_3_avatar");
                chavrutaRequest3Name = jo.getString("chavruta_request_1_name");
                confirmed = jo.getString("confirmed");

                    //make user data object of UserDataSetter class
                    HostSessionData hostClassData = new HostSessionData(chavrutaId, hostFirstName, hostLastName, sessionMessage, sessionDate,
                            startTime, endTime, sefer, location, hostId, chavrutaRequest1, chavrutaRequest2, chavrutaRequest3,
                            chavrutaRequest1Avatar, chavrutaRequest1Name,chavrutaRequest2Avatar,chavrutaRequest2Name,
                            chavrutaRequest3Avatar, chavrutaRequest3Name,confirmed);
                    openChavrutaArrayList
                            .add(hostClassData);

                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void makeCircularRevealAnim(View v) {
        int finalRadius = (int) Math.hypot(v.getWidth() / 2, v.getHeight() / 2);

        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, (int) v.getWidth() / 2, (int) v.getHeight() / 2, 0, finalRadius);
        v.setBackgroundColor(Color.GREEN);
        anim.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(this, AddSelect.class);
        startActivity(intent);
    }
}
