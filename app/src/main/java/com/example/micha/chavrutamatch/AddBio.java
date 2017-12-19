package com.example.micha.chavrutamatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;


import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.Utils.ChavrutaUtils;
import com.example.micha.chavrutamatch.Utils.GlideApp;
import com.example.micha.chavrutamatch.Utils.ImgUtils;
import com.example.micha.chavrutamatch.Utils.TestImgClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by micha on 8/26/2017.
 */

public class AddBio extends AppCompatActivity {

    private final static String LOG_TAG = AddBio.class.getSimpleName();
    private final static String CUSTOM_AVATAR_NUMBER_STRING = "999";
    private final static int CUSTOM_AVATAR_NUMBER_INT = 999;
    private final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 0;

    private final int REQUEST_CODE = 100;
    static String mUserId, mUserEmail, mUserPhoneNumber, mUserName, mUserFirstName, mUserLastName,
            mUserBio, mUserAvatarNumberString, mUserCityState, jsonString;
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
    @BindView(R.id.ac_city_state)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.sv_add_bio)
    ScrollView scrollView;

    //controls whether or not db update necessary
    Boolean bioDataChanged = false;
    //controls whether activity used to update or to create new account
    Boolean updateBio = false;
    // checks if user img file sent from @AvatarSelectMasterList
    Uri mNewProfImgUri = null;
    //Holds list view of possible avatars
    List<Integer> mAvatarsList = AvatarImgs.getAllAvatars();
    String mCustomUserAvatarUriString;
    String mCustomUserAvatarBase64String;

    //TODO: Add input validation using: https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bio);
        ButterKnife.bind(this);
        mUserAvatarNumberString = "0";

        prefs = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);
        //incoming intents
        if (getIntent().getExtras() != null) {
            Intent incomingIntent = getIntent();

            // check if data coming from avatar select frag & sets user selected avatar in addBio Activitiy
            if (incomingIntent.getBooleanExtra("affirm update bio", false)) {
                Bundle bundle = incomingIntent.getExtras();
                updateBio = bundle.getBoolean("affirm update bio");
                int userAvatarSelected = bundle.getInt("avatar position", -1);

                if (userAvatarSelected == CUSTOM_AVATAR_NUMBER_INT) {
                    requestExternalStoragePermission();
                    mCustomUserAvatarUriString = bundle.getString("img_uri_string_key");
                    mNewProfImgUri = Uri.parse(mCustomUserAvatarUriString);

                    if (mNewProfImgUri != null) {

                        GlideApp
                                .with(this)
                                .load(mNewProfImgUri)
                                .into(UserAvatarView);
                    }
                } else {
                    UserAvatarView.setImageResource(mAvatarsList.get(userAvatarSelected));
                }
                mUserAvatarNumberString = "" + userAvatarSelected;
                populateUserDataFromSP("pick chooser return");
            }
            //intent sent from user selecting update bio w/o user editing avatar
            if (incomingIntent.getExtras().getBoolean("update_bio")) {
                updateBio = incomingIntent.getExtras().getBoolean("update_bio");
                populateUserDataFromSP("no new custom avatar selected");
            }
            //if first login on a new device, db call is returned with user acct data
            else if (incomingIntent.getExtras().getString("user_data_json_string") != null) {
                jsonString = incomingIntent.getExtras().getString("user_data_json_string");
                parseUserDetailsFromDB(jsonString);
            }
        } else {
            //if userFirstName in SP == null then user has not used current device,
            // then check db for user details, else load from Shared Preferences
            if (prefs.getString(getString(R.string.user_first_name_key), null) != null) {
                populateUserDataFromSP("no new custom avatar selected");
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
        //set auto-complete for closest US city
        ChavrutaUtils cu = new ChavrutaUtils();
        String jsonFileString = cu.getJsonFileFromResource(this);
        List<String> cityList = cu.parseCityName(jsonFileString);

// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityList);
        autoCompleteTextView.setAdapter(adapter);
    }

    //stores user Biodata in db and sp
    public void storeUserBioDataInDb(View view) {
        String newUserEmail = UserEmailView.getText().toString();
        String newUserPhoneNumber = UserPhoneView.getText().toString();
        String newUserName = UserNameView.getText().toString();
        String newUserFirstName = UserFirstNameView.getText().toString();
        String newUserLastName = UserLastNameView.getText().toString();
        String newUserBio = UserBioView.getText().toString();
        String newUserAvatarNumberString = mUserAvatarNumberString;
        //mCustomUserAvatarString != null means is newly set
        String newCustomUserAvatarString = mCustomUserAvatarUriString;
        String newUserCityState = autoCompleteTextView.getText().toString();
        Uri newProfImgUser = mNewProfImgUri;

        //check for changes before queing db
        dataChangeCheck(newUserBio, newUserFirstName, newUserLastName,
                newUserName, newUserPhoneNumber, newUserEmail, newUserAvatarNumberString, newProfImgUser, newUserCityState);

        if (bioDataChanged) {
            UserDetails.setAllUserDataFromAddBio(mUserId, newUserName, newUserAvatarNumberString,
                    newUserFirstName, newUserLastName, newUserPhoneNumber, newUserEmail, newUserCityState,
                    newCustomUserAvatarString);
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

    //checks to see if user changed data before saving in SP and DB
    public void dataChangeCheck(String newUserBio, String newUserFirstName, String newUserLastName,
                                String newUserName, String newUserPhoneNumber, String newUserEmail,
                                String newUserAvatarNumberString, Uri newProfImgUri, String newUserCityState) {
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
        if (mUserAvatarNumberString == null || !UserDetails.getmUserAvatarNumberString().equals(newUserAvatarNumberString)) {
            mUserAvatarNumberString = newUserAvatarNumberString;
            bioDataChanged = true;
        }

        //todo: must check rotation differently if API< 24, check with emulators!
        if (mCustomUserAvatarUriString != null && newUserAvatarNumberString.equals(CUSTOM_AVATAR_NUMBER_STRING)) {
            bioDataChanged = true;
            //check to see if image needs rotating b4 saving to db
            int rotation = 0;
            try {
                rotation =  ImgUtils.rotateImgNeededCk(this, newProfImgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), newProfImgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //if image is not upright
            if(rotation != 0) {
                bitmap = ImgUtils.rotateImg(bitmap, rotation);
                mCustomUserAvatarBase64String = ImgUtils.bitmapToCompressedBase64String(this, bitmap);
            }else {
                mCustomUserAvatarBase64String = ImgUtils.uriToCompressedBase64String(this, newProfImgUri);
            }
        } else {
            mCustomUserAvatarBase64String = "none";
        }

        if (mUserCityState == null || !mUserCityState.equals(newUserCityState)) {
            mUserCityState = newUserCityState;
            bioDataChanged = true;
        }
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
                mUserPhoneNumber, mUserEmail, mUserBio, mUserCityState, userPostType, mCustomUserAvatarBase64String);
    }

    //populates activity data from SP based on if started from media chooser or not
    public void populateUserDataFromSP(String activityOnCreateType) {
        //get info from newUserLogin if exists on device
        mUserPhoneNumber = prefs.getString(getString(R.string.user_phone_key), null);
        mUserEmail = prefs.getString(getString(R.string.user_email_key), null);
        mUserName = prefs.getString(getString(R.string.user_name_key), null);
        mUserFirstName = prefs.getString(getString(R.string.user_first_name_key), null);
        mUserLastName = prefs.getString(getString(R.string.user_last_name_key), null);
        mUserBio = prefs.getString(getString(R.string.user_bio_key), null);
        mUserId = prefs.getString(getString(R.string.user_account_id_key), null);
        mUserCityState = prefs.getString(getString(R.string.user_city_state), null);
        String userAvatarNumberString = UserDetails.getmUserAvatarNumberString();
        // if user selected a new custom avatar, don't populate from SP
        if (activityOnCreateType.equals("no new custom avatar selected")) {
            mCustomUserAvatarUriString = prefs.getString(
                    getString(R.string.custom_user_avatar_string_uri_key), null);
            mUserAvatarNumberString = prefs.getString(getString(R.string.user_avatar_number_key), userAvatarNumberString);
        }
        populateEditTextData(activityOnCreateType);
        //get user id if not in SP
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
            mUserCityState = jo.getString("userCityState");
            populateEditTextData("no new custom avatar selected");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void populateEditTextData(String activityOnCreateType) {
        UserPhoneView.setText(mUserPhoneNumber);
        UserEmailView.setText(mUserEmail);
        UserNameView.setText(mUserName);
        UserFirstNameView.setText(mUserFirstName);
        UserLastNameView.setText(mUserLastName);
        //set avatar image if not just chosen
        if (activityOnCreateType.equals("no new custom avatar selected")) {
            String userAvatarNumberString = UserDetails.getmUserAvatarNumberString();
            Uri userAvatarUri = UserDetails.getHostAvatarUri();

            mUserAvatarNumberString = userAvatarNumberString;
            mNewProfImgUri = userAvatarUri;

            //custom avatar was previously chosen
            if (mUserAvatarNumberString.equals(CUSTOM_AVATAR_NUMBER_STRING)) {
                GlideApp
                        .with(this)
                        .load(mNewProfImgUri)
                        .into(UserAvatarView);

                //template avatar previously chosen
            } else {
                UserAvatarView.setImageResource(mAvatarsList.get(Integer.parseInt(mUserAvatarNumberString)));
            }
        }
        UserBioView.setText(mUserBio);
        autoCompleteTextView.setText(mUserCityState);
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
        editor.putString(getString(R.string.user_city_state_key), mUserCityState);
        editor.putString(getString(R.string.custom_user_avatar_string_uri_key), mCustomUserAvatarUriString);
        editor.putString(getString(R.string.user_avatar_base_64_key), mCustomUserAvatarBase64String);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //todo: close inputstream
    private byte[] convertProfUriToByteArray(Uri mNewProfImgUri) {

        InputStream iStream = null;
        try {
            iStream = getContentResolver().openInputStream(mNewProfImgUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] inputData = new byte[0];
        try {
            inputData = ImgUtils.getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            // Request for read permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                Snackbar.make(scrollView, "Read permission was granted.",
                        Snackbar.LENGTH_SHORT)
                        .show();

            } else {
                // Permission request was denied.
                Snackbar.make(scrollView, "Read file permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestExternalStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(scrollView, "Read permissions requested to get avatar from your image files.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(AddBio.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }).show();

        } else {
            Snackbar.make(scrollView,
                    "Permission is not available. Requesting read storage permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
}
