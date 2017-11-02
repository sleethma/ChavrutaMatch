package com.example.micha.chavrutamatch;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.Utils.ChavrutaUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.micha.chavrutamatch.MainActivity.mContext;


/**
 * Created by micha on 8/26/2017.
 */

public class AddBio extends AppCompatActivity {

    private static String LOG_TAG = AddBio.class.getSimpleName();
    private final int REQUEST_CODE = 100;
    static String mUserId, mUserEmail, mUserPhoneNumber, mUserName, mUserFirstName, mUserLastName,
            mUserBio, mUserAvatarNumberString, jsonString;
    @BindView(R.id.et_user_phone_number)
    EditText UserPhoneView;
    @BindView(R.id.et_user_email)
    EditText UserEmailView;
    @BindView(R.id.user_name)
    EditText UserNameView;
    @BindView(R.id.user_first_name)
    EditText UserFirstNameView;
    @BindView(R.id.user_last_name)
    EditText UserLastNameView;
    @BindView(R.id.et_user_bio)
    EditText UserBioView;
    @BindView(R.id.iv_user_avatar)
    ImageView UserAvatarView;
    SharedPreferences prefs;


    //controls whether or not db update necessary
    Boolean bioDataChanged = false;
    //controls whether activity used to update or to create new account
    Boolean updateBio = false;
    //Holds list view of possible avatars
    List<Integer> mAvatarsList = AvatarImgs.getAllAvatars();


//TODO: Add input validation using: https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bio);
        ButterKnife.bind(this);
        //todo: delete below init
        mUserAvatarNumberString = "0";
        mUserBio = "fake data";

        prefs = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);
        //incoming intents
        if (getIntent().getExtras() != null) {
            Intent incomingIntent = getIntent();

            // check if data coming from avatar select frag & sets user selected avatar in addBio Activitiy

            if (incomingIntent.getBooleanExtra("affirm update bio", false)){
                Bundle bundle = incomingIntent.getExtras();
                updateBio = bundle.getBoolean("affirm update bio");
                int userAvatarSelected = bundle.getInt("avatar position", -1);
                UserDetails.setmUserAvatarNumberString("" + userAvatarSelected);
                UserAvatarView.setImageResource(mAvatarsList.get(userAvatarSelected));
                populateUserDataFromSP();
            }
            //intent sent from user selecting update bio w/o user editing avatar
            if (incomingIntent.getExtras().getBoolean("update_bio")) {
                updateBio = incomingIntent.getExtras().getBoolean("update_bio");
                populateUserDataFromSP();
            }
            //if first login on a new device, db call is returned with user acct data
            else if (incomingIntent.getExtras().getString("user_data_json_string") != null) {
                jsonString = incomingIntent.getExtras().getString("user_data_json_string");
                parseUserDetailsFromDB(jsonString);
            }
        } else {

            //if userFirstName in SP = null then user has not used current device,
            // then check db for user details, else load from Shared Preferences
            if (prefs.getString(getString(R.string.user_first_name_key), null) != null) {
                populateUserDataFromSP();
            } else {
                getUserBioDatafromDb();
            }
        }
        //auto moves edittext when softkeyboard called
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        UserAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AvatarSelectMasterList.class);
                intent.putExtra("update_bio", updateBio);
                startActivity(intent);
            }
        });
        //todo: delete me!
        ChavrutaUtils cu = new ChavrutaUtils();
        String jsonFileString = cu.getJsonFileFromResource(this);
        //list of top 1000 city names
        List<String> testList = cu.parseCityName(jsonFileString);
    }


    //sole purpose of method is to send notification check and remind user to
    // come back and fill out bio later!

    public void skipBio(View view) {
        //TODO: send notification to confirm user not setting up bio
        //Stores any biodata entered into SP
        storeUserBioDataInDb(view);
    }

    //stores user Biodata in db and sp
    public void storeUserBioDataInDb(View view) {
        String newUserEmail = UserEmailView.getText().toString();
        String newUserPhoneNumber = UserPhoneView.getText().toString();
        String newUserName = UserNameView.getText().toString();
        String newUserFirstName = UserFirstNameView.getText().toString();
        String newUserLastName = UserLastNameView.getText().toString();
        String newUserBio = UserBioView.getText().toString();
        String newUserAvatarNumberString = UserDetails.getmUserAvatarNumberString();

        //check for changes before queing db
        dataChangeCheck(newUserBio, newUserFirstName, newUserLastName,
                newUserName, newUserPhoneNumber, newUserEmail, newUserAvatarNumberString);

        if (bioDataChanged) {

            UserDetails.setAllUserDataFromAddBio(mUserId, newUserName, newUserAvatarNumberString,
                    newUserFirstName, newUserLastName, newUserPhoneNumber, newUserEmail);
            saveAddBioDataToSP();

            postUserBio();
        } else {
            Toast.makeText(this, "No Changes Made", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //gets user bio info from server
    public void getUserBioDatafromDb() {
        // check to see if have an account in db
        mUserId = UserDetails.getmUserId();
        ServerConnect getUserDetailsFromDb = new ServerConnect(this);
        getUserDetailsFromDb.execute("get UserDetails", mUserId);
    }

    //posts saved user bio info to server
    public void postUserBio() {
        final String userPost = "user post";
        String userPostType;
        if (updateBio) {
            userPostType = "update user post";
        } else {
            userPostType = "new user post";
        }

        ServerConnect postUserToServer = new ServerConnect(this);
        postUserToServer.execute(userPost, mUserId, mUserName, mUserAvatarNumberString, mUserFirstName, mUserLastName,
                mUserPhoneNumber, mUserEmail, mUserBio, userPostType);
    }

    //checks to see if user changed data before saving in SP and DB
    public void dataChangeCheck(String newUserBio, String newUserFirstName, String newUserLastName,
                                String newUserName, String newUserPhoneNumber, String newUserEmail,
                                String newUserAvatarNumberString) {
        bioDataChanged = false;
        if (mUserBio == null || !mUserBio.equals(newUserBio)) {
            mUserBio = newUserBio;
            bioDataChanged = true;
        }
        if (mUserFirstName == null || !mUserFirstName.equals(newUserFirstName)) {
            mUserFirstName = newUserFirstName;
            bioDataChanged = true;
        }
        if (mUserLastName == null || !mUserLastName.equals(newUserLastName)) {
            mUserLastName = newUserLastName;
            bioDataChanged = true;
        }
        if (mUserName == null || !mUserName.equals(newUserName)) {
            mUserName = newUserName;
            bioDataChanged = true;
        }
        if (mUserPhoneNumber == null || !mUserPhoneNumber.equals(newUserPhoneNumber)) {
            mUserPhoneNumber = newUserPhoneNumber;
            bioDataChanged = true;
        }
        if (mUserEmail == null || !mUserEmail.equals(newUserEmail)) {
            mUserEmail = newUserEmail;
            bioDataChanged = true;
        }
        if (mUserAvatarNumberString == null || !mUserAvatarNumberString.equals(newUserAvatarNumberString)) {
            mUserAvatarNumberString = newUserAvatarNumberString;
            bioDataChanged = true;
        }
    }

    public void setAddBioDataToUserDetails() {
        //gets activity globals from static UserDetail class updated in method above
        mUserPhoneNumber = UserDetails.getmUserPhoneNumber();
        mUserEmail = UserDetails.getmUserEmail();
        mUserName = UserDetails.getmUserName();
        mUserFirstName = UserDetails.getmUserFirstName();
        mUserLastName = UserDetails.getmUserLastName();
        mUserAvatarNumberString = UserDetails.getmUserAvatarNumberString();
        mUserBio = UserDetails.getmUserBio();
        mUserId = prefs.getString(getString(R.string.user_account_id_key), null);
        //todo: delete below if functional w/o
        if (mUserId == null) mUserId = UserDetails.getmUserId();
    }

    //populates activity data from SP
    public void populateUserDataFromSP() {
        //get info from newUserLogin if exists on device
        mUserPhoneNumber = prefs.getString(getString(R.string.user_phone_key), null);
        mUserEmail = prefs.getString(getString(R.string.user_email_key), null);
        mUserName = prefs.getString(getString(R.string.user_name_key), null);
        mUserFirstName = prefs.getString(getString(R.string.user_first_name_key), null);
        mUserLastName = prefs.getString(getString(R.string.user_last_name_key), null);
        mUserAvatarNumberString = prefs.getString(getString(R.string.user_avatar_number_key), "0");
        mUserBio = prefs.getString(getString(R.string.user_bio_key), null);
        mUserId = prefs.getString(getString(R.string.user_account_id_key), null);
        populateEditTextData();
        //todo: delete below if functional w/o
        if (mUserId == null) mUserId = UserDetails.getmUserId();
    }

    //sets user details from db call
    public void parseUserDetailsFromDB(String jsonString) {
        JSONObject jsonObject;
        JSONArray jsonArray;


        try {
            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");

            //loop through array and extract objects, adding them individually as setter objects,
            //and adding objects to list adapter.
            JSONObject jo = jsonArray.getJSONObject(0);
            mUserId = jo.getString("userId");
            mUserName = jo.getString("userName");
            mUserAvatarNumberString = jo.getString("userAvatarNumber");
            mUserFirstName = jo.getString("userFirstName");
            mUserLastName = jo.getString("userLastName");
            mUserPhoneNumber = jo.getString("userPhoneNumber");
            mUserEmail = jo.getString("userEmail");
            mUserBio = jo.getString("userBio");
            populateEditTextData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void populateEditTextData() {
        UserPhoneView.setText(mUserPhoneNumber);
        UserEmailView.setText(mUserEmail);
        UserNameView.setText(mUserName);
        UserFirstNameView.setText(mUserFirstName);
        UserLastNameView.setText(mUserLastName);
        UserAvatarView.setImageResource(mAvatarsList.get(Integer.parseInt(UserDetails.getmUserAvatarNumberString())));
        UserBioView.setText(mUserBio);
    }

    public void saveAddBioDataToSP() {
        SharedPreferences.Editor editor =
                getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE).edit();
        editor.putString(getString(R.string.user_email_key), mUserEmail);
        editor.putString(getString(R.string.user_phone_key), mUserPhoneNumber);
        editor.putString(getString(R.string.user_name_key), mUserName);
        editor.putString(getString(R.string.user_first_name_key), mUserFirstName);
        editor.putString(getString(R.string.user_last_name_key), mUserLastName);
        editor.putString(getString(R.string.user_email_key), mUserEmail);
        editor.putString(getString(R.string.user_avatar_number_key), mUserAvatarNumberString);
        editor.putString(getString(R.string.user_bio_key), mUserBio);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
