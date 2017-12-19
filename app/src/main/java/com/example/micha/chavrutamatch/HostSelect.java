package com.example.micha.chavrutamatch;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.ServerConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Utils.GlideApp;
import com.example.micha.chavrutamatch.Utils.RecyclerViewListDecor;
import com.example.micha.chavrutamatch.Utils.TimeStampConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/22/2017.
 */

public class HostSelect extends AppCompatActivity {

    @BindView(R.id.iv_user_avatar)
    ImageView userPic;
    // @BindView(R.id.b_host_chavruta) ImageButton hostChavruta;
    @BindView(R.id.all_hosts_list_view)
    RecyclerView allHostsList;
    @BindView(R.id.ll_no_chavruta_hosts)
    LinearLayout noHostLayout;
    @BindView(R.id.iv_scroll_open_host)
    ImageView scrollImg;
    ArrayList<HostSessionData> openChavrutaArrayList;
    static Context mContext;
    //true if user non-stock image used for avatar
    final private String USER_IMG_AVATAR = "999";

    public String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String userId;
    //adds spacing b/n listitems
    private final int VERTICAL_LIST_ITEM_SPACE = 40;

    OpenChavrutaAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_host_listview);
        ButterKnife.bind(this);
        userId = UserDetails.getmUserId();
        mContext = this;

        //sets HostImage in title bar
        if (UserDetails.getmUserAvatarNumberString() != null &&
                !UserDetails.getmUserAvatarNumberString().equals(USER_IMG_AVATAR)) {
            userPic.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(UserDetails.getmUserAvatarNumberString())));
        } else {

            try {
                GlideApp
                        .with(mContext)
                        .load(UserDetails.getHostAvatarUri())
                        .placeholder(R.drawable.ic_unknown_user)
                        .centerCrop()
                        .into(userPic);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //constructs the data source
        openChavrutaArrayList = new ArrayList<>();

        //accesses JSON from ServerConnect
        jsonString = getIntent().getExtras().getString("jsonKey");

        //parses JSON entry
        parseJSONEntry();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allHostsList.setLayoutManager(layoutManager);

        //decorates list with spacing
        allHostsList.addItemDecoration(new RecyclerViewListDecor(VERTICAL_LIST_ITEM_SPACE));

        //attaches data source to adapter
        mAdapter = new OpenChavrutaAdapter(this, openChavrutaArrayList);

        allHostsList.setAdapter(mAdapter);

        //todo:functioning ripple effect removed and applied elsewhere
//        scrollImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                makeCircularRevealAnim(v);
//            }
//        });
    }

    //@ var chavrutaId = autoInc from db
    public void parseJSONEntry() {
        String chavrutaId;

        String hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                startTime, endTime, sefer, location, hostCityState, hostId,
                chavrutaRequest1, chavrutaRequest1Avatar, chavrutaRequest1Name,
                chavrutaRequest2, chavrutaRequest2Avatar, chavrutaRequest2Name,
                chavrutaRequest3, chavrutaRequest3Avatar, chavrutaRequest3Name, confirmed;
        try {

            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");
            String currentDateString = new SimpleDateFormat(
                    "yyyy-MM-dd").format(new Date());

            //loop through array and extract objects, adding them individually as setter objects,
            //and adding objects to list adapter.
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                chavrutaId = jo.getString("chavruta_id");
                hostFirstName = jo.getString("hostFirstName");
                hostLastName = jo.getString("hostLastName");
                hostAvatarNumber = jo.getString("hostAvatarNumber");
                sessionMessage = jo.getString("sessionMessage");
                sessionDate = jo.getString("sessionDate");
                startTime = jo.getString("startTime");
                endTime = jo.getString("endTime");
                sefer = jo.getString("sefer");
                location = jo.getString("location");
                hostCityState = jo.getString("hostCityState");
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

                boolean classPassed = false;
                //format session date for outdated comparison and store chavrutaId for deletion
                //todo: replace below block to fit flow
                if(!sessionDate.contains("D")) {
                    //determine a class date is in future
                    classPassed = TimeStampConverter.classDatePassedAndDelete(mContext,
                            currentDateString, sessionDate);
                }else{
                    classPassed = true;
                }
            //only adds to array for adapter if user is not already requesting or hosting class
                if (userId.equals(chavrutaRequest1) || userId.equals(chavrutaRequest2) ||
                        userId.equals(chavrutaRequest3) || userId.equals(hostId)) {
                } else {
                    if(!classPassed) {
                        //make user data object of UserDataSetter class
                        HostSessionData hostClassData = new HostSessionData(chavrutaId, hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                                startTime, endTime, sefer, location, hostCityState, hostId, chavrutaRequest1, chavrutaRequest2, chavrutaRequest3,
                                chavrutaRequest1Avatar, chavrutaRequest1Name, chavrutaRequest2Avatar, chavrutaRequest2Name,
                                chavrutaRequest3Avatar, chavrutaRequest3Name, confirmed);
                        openChavrutaArrayList
                                .add(hostClassData);
                    }else{
                        //if class date passed, delete in db
                            ServerConnect deletePassedClassFromDb = new ServerConnect(mContext);
                            deletePassedClassFromDb.execute("delete chavruta", chavrutaId);
                    }
                }
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //changes view on host classes availiable
        int hostArraySize = openChavrutaArrayList.size();
        if(hostArraySize != 0) {
            allHostsList.setVisibility(View.VISIBLE);
            noHostLayout.setVisibility(View.GONE);
        }else{
            noHostLayout.setVisibility(View.VISIBLE);
            allHostsList.setVisibility(View.GONE);
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
        Intent intent = new Intent(this, AddSelect.class);
        startActivity(intent);
    }

    public void loadNewHost(View v){
        Intent intent = new Intent(this, NewHost.class);
        startActivity(intent);
    }
}
