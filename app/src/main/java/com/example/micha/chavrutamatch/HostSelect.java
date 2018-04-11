package com.example.micha.chavrutamatch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.Utils.GlideApp;
import com.example.micha.chavrutamatch.Utils.RecyclerViewListDecor;
import com.example.micha.chavrutamatch.Utils.TimeStampConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/22/2017.
 */

public class HostSelect extends AppCompatActivity implements OpenHostAdapter.ListItemClickListener {

    @BindView(R.id.iv_awaiting_host_avatar)
    ImageView userPic;
    // @BindView(R.id.b_host_chavruta) ImageButton hostChavruta;
    @BindView(R.id.all_hosts_list_view)
    RecyclerView allHostsList;
    @BindView(R.id.ll_no_chavruta_hosts)
    LinearLayout noHostLayout;
    @BindView(R.id.iv_scroll_open_host)
    ImageView scrollImg;
    ArrayList<HostSessionData> openHostArrayList;
    static Context mContext;
    //true if user non-stock image used for avatar
    final private String USER_IMG_AVATAR = "999";
    final private String NO_DATE = "Date?";

    public String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String userId;
    //adds spacing b/n listitems
    private final int VERTICAL_LIST_ITEM_SPACE = 40;
    private Context context;
    OpenHostAdapter mAdapter;

    @Inject
    public UserDetails userDetailsInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_host_listview);
        ButterKnife.bind(this);
        userId = userDetailsInstance.getmUserId();
        context = this;
        (ChavrutaMatch.get(this).getApplicationComponent()).inject(this);

        //sets HostImage in title bar
        if (userDetailsInstance.getmUserAvatarNumberString() != null &&
                !userDetailsInstance.getmUserAvatarNumberString().equals(USER_IMG_AVATAR)) {
            userPic.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(userDetailsInstance.getmUserAvatarNumberString())));
        } else {

            try {
                GlideApp
                        .with(mContext)
                        .load(userDetailsInstance.getHostAvatarUri())
                        .placeholder(R.drawable.ic_unknown_user)
                        .circleCrop()
                        .into(userPic);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //constructs the data source
        openHostArrayList = new ArrayList<>();

        //accesses JSON from ServerConnect
        if(getIntent().getExtras().getString("jsonKey") != null){
            jsonString = ChavrutaMatch.getOpenHostsJsonString();
            //parses JSON entry
            parseJSONEntry();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allHostsList.setLayoutManager(layoutManager);

        //decorates list with spacing
        allHostsList.addItemDecoration(new RecyclerViewListDecor(VERTICAL_LIST_ITEM_SPACE));

        //attaches data source to adapter
        mAdapter = new OpenHostAdapter(this, openHostArrayList, this, userDetailsInstance);
        allHostsList.setHasFixedSize(true);

        allHostsList.setAdapter(mAdapter);
    }

    //@ var chavrutaId = autoInc from db
    private void parseJSONEntry() {
        String chavrutaId;

        String hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                startTime, endTime, sefer, location, hostCityState, hostId,
                chavrutaRequest1, chavrutaRequest1Avatar, chavrutaRequest1Name,
                chavrutaRequest2, chavrutaRequest2Avatar, chavrutaRequest2Name,
                chavrutaRequest3,  chavrutaRequest3Avatar, chavrutaRequest3Name, confirmed;
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

                boolean classDatePassed = false;
                //format session date for outdated comparison and store chavrutaId for deletion
                //todo: refactor below block to fit flow
                if (!sessionDate.contains(NO_DATE)) {
                    //determine a class date is in future
                    classDatePassed = TimeStampConverter.classDatePassedAndDelete(mContext,
                            currentDateString, sessionDate);
                } else {
                    classDatePassed = true;
                }
                //only adds to array for adapter if user is not already requesting or hosting class
                if (userId.equals(chavrutaRequest1) || userId.equals(chavrutaRequest2) ||
                        userId.equals(chavrutaRequest3) || userId.equals(hostId)) {
                } else {
                    if (!classDatePassed) {
                        //make user data object of UserDataSetter class
                        HostSessionData hostClassData = new HostSessionData(chavrutaId, hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                                startTime, endTime, sefer, location, hostCityState, hostId, chavrutaRequest1, chavrutaRequest2, chavrutaRequest3,
                                chavrutaRequest1Avatar, chavrutaRequest1Name, chavrutaRequest2Avatar, chavrutaRequest2Name,
                                chavrutaRequest3Avatar, chavrutaRequest3Name, confirmed);
                        openHostArrayList
                                .add(hostClassData);
                    } else {
                        //if class date passed, delete in db
                        ServerConnect deletePassedClassFromDb = new ServerConnect(mContext, userDetailsInstance);
                        deletePassedClassFromDb.execute("delete chavruta", chavrutaId);
                    }
                }
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //changes view on host classes availiable
        int hostArraySize = openHostArrayList.size();
        if (hostArraySize != 0) {
            allHostsList.setVisibility(View.VISIBLE);
            noHostLayout.setVisibility(View.GONE);
        } else {
            noHostLayout.setVisibility(View.VISIBLE);
            allHostsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AddSelect.class);
        startActivity(intent);
    }

    public void loadNewHost(View v) {
        Intent intent = new Intent(this, NewHost.class);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View cardView) {
        flip(cardView, cardView);
    }

    public void flip(final View currentSide, final View flipToSide) {
        final int FLIP_DURATION = 150;
        final boolean isCardFront = currentSide.findViewById(R.id.cardFront)
                .getVisibility() == View.VISIBLE;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            if (isCardFront) {
                currentSide.findViewById(R.id.cardFront).setVisibility(View.VISIBLE);
                currentSide.findViewById(R.id.cardBack).setVisibility(View.GONE);
                flipToSide.findViewById(R.id.cardBack).setVisibility(View.VISIBLE);
                flipToSide.findViewById(R.id.cardFront).setVisibility(View.GONE);
            } else {
                currentSide.findViewById(R.id.cardFront).setVisibility(View.GONE);
                currentSide.findViewById(R.id.cardBack).setVisibility(View.VISIBLE);
                flipToSide.findViewById(R.id.cardBack).setVisibility(View.GONE);
                flipToSide.findViewById(R.id.cardFront).setVisibility(View.VISIBLE);
            }

            AnimatorSet set = new AnimatorSet();
            set.playSequentially(
                    ObjectAnimator.ofFloat(currentSide, "rotationY", 90).setDuration(FLIP_DURATION),
                    ObjectAnimator.ofInt(currentSide, "visibility", View.GONE).setDuration(0),
                    ObjectAnimator.ofFloat(flipToSide, "rotationY", -90).setDuration(0),
                    ObjectAnimator.ofInt(flipToSide, "visibility", View.VISIBLE).setDuration(0),
                    ObjectAnimator.ofFloat(flipToSide, "rotationY", 0).setDuration(FLIP_DURATION));
            set.start();
        } else {
            currentSide
                    .animate().rotationY(90).setDuration(FLIP_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (isCardFront) {
                                flipToSide.findViewById(R.id.cardBack).setVisibility(View.VISIBLE);
                                flipToSide.findViewById(R.id.cardFront).setVisibility(View.GONE);
                            } else {
                                flipToSide.findViewById(R.id.cardBack).setVisibility(View.GONE);
                                flipToSide.findViewById(R.id.cardFront).setVisibility(View.VISIBLE);
                            }
                            flipToSide.setRotationY(-90);
                            flipToSide.animate().rotationY(0).setDuration(FLIP_DURATION).setListener(null);
                        }
                    });
        }
    }
}
