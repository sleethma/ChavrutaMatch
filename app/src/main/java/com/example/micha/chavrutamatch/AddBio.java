package com.example.micha.chavrutamatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.ServerConnect;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by micha on 8/26/2017.
 */

public class AddBio extends AppCompatActivity {

    private static String LOG_TAG = AddBio.class.getSimpleName();
    String mUserId, mUserEmail, mUserPhoneNumber, mUserName, mUserFirstName, mUserLastName,
            mUserBio, mUserAvatarNumberString;
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
    //null vars to keep from npes
    Boolean nullPhone;
    Boolean nullEmail;
    Boolean nullUserName;
    Boolean nullFirstName;
    Boolean nullLastname;
    Boolean nullAvatar;
    Boolean nullBio;
    Boolean nullId;
    //controls whether or not db update necessary
    Boolean bioDataChanged = false;
    //controls whether activity used to update or to create new account
    Boolean updateBio = false;


//TODO: Add input validation using: https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bio);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) updateBio = getIntent().getExtras().getBoolean("update_bio");
        prefs = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);

        //get info from newUserLogin if exists
        mUserPhoneNumber = prefs.getString(getString(R.string.user_phone_key), null);
        mUserEmail = prefs.getString(getString(R.string.user_email_key), null);
        mUserName = prefs.getString(getString(R.string.user_name_key), null);
        mUserFirstName = prefs.getString(getString(R.string.user_first_name_key), null);
        mUserLastName = prefs.getString(getString(R.string.user_last_name_key), null);
        mUserAvatarNumberString = prefs.getString(getString(R.string.user_avatar_number_key), null);
        mUserBio = prefs.getString(getString(R.string.user_bio_key), null);
        mUserId = prefs.getString(getString(R.string.user_account_id_key), null);
        getmUserId()
        UserPhoneView.setText(mUserPhoneNumber);
        UserEmailView.setText(mUserEmail);
        UserNameView.setText(mUserName);
        UserFirstNameView.setText(mUserFirstName);
        UserLastNameView.setText(mUserLastName);
        //UserAvatarView.setImageResource();
        UserBioView.setText(mUserBio);
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

        //TODO: GRAB USER AVATAR
        String newUserAvatarNumberString = "1";

        //check for changes before queing db
        dataChangeCheck(newUserBio, newUserFirstName, newUserLastName,
                newUserName, newUserPhoneNumber, newUserEmail, newUserAvatarNumberString);

        if (bioDataChanged) {

            UserDetails.setAllUserDataFromAddBio(mUserId, newUserName, newUserAvatarNumberString,
                    newUserFirstName, newUserLastName, newUserPhoneNumber, newUserEmail);
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


            postUserBio();
        } else {
            Toast.makeText(this, "No Changes Made", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //posts saved user bio info to server
    public void postUserBio() {
        String userPost = "user post";
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
        if (mUserBio == null || !mUserBio.equals(newUserBio)) {
            mUserBio = newUserBio;
            bioDataChanged = true;
        }
        if (mUserFirstName == null || !mUserFirstName.equals(newUserFirstName)) {
            mUserFirstName= newUserFirstName;
            bioDataChanged = true;
        }
        if (mUserLastName == null || !mUserLastName.equals(newUserLastName)){
            mUserLastName = newUserLastName;
            bioDataChanged = true;
        }
        if (mUserName == null || !mUserName.equals(newUserName)){
            mUserName = newUserName;
            bioDataChanged = true;
        }
        if (mUserPhoneNumber == null || !mUserPhoneNumber.equals(newUserPhoneNumber)){
            mUserPhoneNumber = newUserPhoneNumber;
            bioDataChanged = true;
        }
        if (mUserEmail == null || !mUserEmail.equals(newUserEmail)){
            mUserEmail = newUserEmail;
            bioDataChanged = true;
        }
        if (mUserAvatarNumberString == null || !mUserAvatarNumberString.equals(newUserAvatarNumberString)){
            mUserName = newUserName;
            bioDataChanged = true;
        }
    }
}
