package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.HostSessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.micha.chavrutamatch.Data.HostSessionData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.micha.chavrutamatch.AcctLogin.LoginActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.Utils.ChavrutaTextValidation;
import com.example.micha.chavrutamatch.Utils.RecyclerViewListDecor;
import com.example.micha.chavrutamatch.Utils.TimeStampConverter;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.text.DateFormat.SHORT;

public class MainActivity extends AppCompatActivity {
    //TODO add up nav arrow to each activity
    String LOGTAG = MainActivity.class.getSimpleName();
    @BindView(R.id.iv_no_match_add_match)
    ImageView noMatchView;
    @BindView(R.id.lv_my_chavruta)
    RecyclerView myChavrutaListView;
    @BindView(R.id.iv_host_avatar)
    ImageView userAvatar;
    @BindView(R.id.tv_my_chavruta_label)
    TextView myChavrutaLabel;
    OpenChavrutaAdapter mAdapter;
    static ArrayList<HostSessionData> myChavrutasArrayList;
    static Context mContext;
    private static String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String accountId;

    //adds spacing b/n listitems
    private final int VERTICAL_LIST_ITEM_SPACE = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        //sets up UserDetails
       UserDetails.setUserDetailsFromSP(mContext);
        //sets user avatar. @UserAvatarNumberString = "999" indicates avatar is user photo
        if(UserDetails.getmUserAvatarNumberString() != null &&
                !UserDetails.getmUserAvatarNumberString().equals("999")) {
            userAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                   Integer.parseInt(UserDetails.getmUserAvatarNumberString())));
        }else{
            userAvatar.setImageURI(UserDetails.getHostAvatarUri());
        }

        //check if already logged in
        //get current account and create new anonymous inner class
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                UserDetails.setmUserId(accountKitId);
                //stores user id, email, or phone in SP
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE).edit();
                editor.putString(getString(R.string.user_account_id_key), accountKitId);
                editor.putBoolean("new_user_key", false);
                //todo: delete below
//                editor.putString(getString(R.string.user_avatar_number_key), "0");
                PhoneNumber phoneNumber = account.getPhoneNumber();

                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    UserDetails.setmUserPhoneNumber(formattedPhoneNumber);
                    editor.putString(getString(R.string.user_phone_key), formattedPhoneNumber);

                } else {
                    // if the email address is available, store it
                    String emailString = account.getEmail();
                    UserDetails.setmUserEmail(emailString);
                    editor.putString(getString(R.string.user_email_key), emailString);
                }
                editor.apply();
            }

            @Override
            public void onError(final AccountKitError error) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        //receives intent from ServerConnect to display myChavruta list, else gets myChavruta info from db
        if (getIntent().getExtras().getString("myChavrutaKey") != null) {
            jsonString = getIntent().getExtras().getString("myChavrutaKey");

            myChavrutasArrayList = new ArrayList<>();

            //add and remove views to display myChavrutas
            if (!jsonString.isEmpty()) {
                //parses and adds data in JSON string from MyChavruta Server call
                parseJSONMyChavrutas();
                //todo inorder to resize mychavruta recyclerview
                myChavrutaListView.requestLayout();

                //sets a smaller view if less than
//                if(myChavrutasArrayList.size() <=2) myChavrutaListView.getLayoutParams().height = 900;
                //attaches data source to adapter and displays list
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                myChavrutaListView.setLayoutManager(linearLayoutManager);

                //add ItemDecoration
                myChavrutaListView.addItemDecoration(new RecyclerViewListDecor(VERTICAL_LIST_ITEM_SPACE));

                //todo: uncomment below to optimize UI if works with multiple listitem layout types
//                myChavrutaListView.setHasFixedSize(true);
                mAdapter = new OpenChavrutaAdapter(this, myChavrutasArrayList);
                myChavrutaListView.setAdapter(mAdapter);
                noMatchView.setVisibility(View.GONE);
                myChavrutaLabel.setVisibility(View.VISIBLE);
                myChavrutaListView.setVisibility(View.VISIBLE);
            } else {
                //sets empty array list view
                myChavrutaListView.setVisibility(View.GONE);
                noMatchView.setVisibility(View.VISIBLE);
                myChavrutaLabel.setVisibility(View.GONE);

                //Todo: delete this and reapply to another element more revelant
//                noMatchView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        animateTransition(v);
//                    }
//                });

            }
            //checks to ensure db has data after parsing
            if(myChavrutasArrayList.size() < 1){
                myChavrutaListView.setVisibility(View.GONE);
                noMatchView.setVisibility(View.VISIBLE);
                myChavrutaLabel.setVisibility(View.GONE);
            }

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } else {
            //if db not yet accessed, gets all chavrutas that user has requested
            //@var sp: sets userId to UserDetails for server calls
            SharedPreferences sp = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);
            accountId = sp.getString(getString(R.string.user_account_id_key), null);
            UserDetails.setmUserId(accountId);
            //check user has a stored accountkit id on device and fetch their chavruta data from db
            if (accountId != null) {
                String getMyChavrutasKey = "my chavrutas";
                ServerConnect getMyChavrutas = new ServerConnect(this);
                getMyChavrutas.execute(getMyChavrutasKey);
            } else {
                //device is not logged in
                AccountKit.logOut();
                launchLoginActivity();
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSelect.class);
                startActivity(intent);
            }
        });

        // COMPLETED (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only swipe needed
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                int id = (int) viewHolder.itemView.getTag();
                mAdapter.deleteMyChavrutaArrayItemOnSwipe(id);
            }
        }).attachToRecyclerView(myChavrutaListView);
    }

    //parses JSON string data to form myChavrutas ListView
    public void parseJSONMyChavrutas() {
        String chavrutaId;

        String hostFirstName, hostLastName, hostAvatarNumber, sessionMessage, sessionDate,
                startTime, endTime, sefer, location, hostCityState, hostId,
                chavrutaRequest1, chavrutaRequest1Avatar, chavrutaRequest1Name,
                chavrutaRequest2, chavrutaRequest2Avatar, chavrutaRequest2Name,
                chavrutaRequest3, chavrutaRequest3Avatar, chavrutaRequest3Name,
                confirmed;
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

                //make user data object of UserDataSetter class
                HostSessionData myChavrutaData = new HostSessionData(chavrutaId, hostFirstName,
                        hostLastName, hostAvatarNumber, sessionMessage, sessionDate, startTime, endTime, sefer, location,
                        hostCityState, hostId, chavrutaRequest1, chavrutaRequest2, chavrutaRequest3,
                        chavrutaRequest1Avatar, chavrutaRequest1Name, chavrutaRequest2Avatar, chavrutaRequest2Name,
                        chavrutaRequest3Avatar, chavrutaRequest3Name,
                        confirmed);
                myChavrutasArrayList.add(myChavrutaData);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh_list) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //mAdapter.notifyDataSetChanged();
            return true;
        }
        //My Profile
        if (id == R.id.my_profile) {
            // Access addBio for profile edit
            Intent intent = new Intent(this, AddBio.class);
            Boolean updateBio = true;
            intent.putExtra("update_bio", updateBio);
            startActivity(intent);
            return true;
        }
        //logout
        if (id == R.id.logout) {
            // logout of Account Kit
            AccountKit.logOut();
            launchLoginActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void animateTransition(View view) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        TransitionManager.beginDelayedTransition(root, slide);
        view.setVisibility(View.INVISIBLE);
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    public void loadProfile(View view){
        Intent intent = new Intent(this, AddBio.class);
        Boolean updateBio = true;
        intent.putExtra("update_bio", updateBio);
        startActivity(intent);
    }
}

