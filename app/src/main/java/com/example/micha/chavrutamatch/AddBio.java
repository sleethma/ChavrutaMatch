package com.example.micha.chavrutamatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.micha.chavrutamatch.Data.ServerConnect;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 8/26/2017.
 */

public class AddBio extends AppCompatActivity {

    private static String LOG_TAG= AddBio.class.getSimpleName();
    String  mUserId,mUserEmail, mUserPhoneNumber, mUserName, mUserFirstName, mUserLastName,
            mUserBio, mUserAvatarNumberString;
    @BindView(R.id.et_user_phone_number)EditText UserPhoneView;
    @BindView(R.id.et_user_email) EditText UserEmailView;
    @BindView(R.id.user_name) EditText UserNameView;
    @BindView(R.id.user_first_name) EditText UserFirstNameView;
    @BindView(R.id.user_last_name)EditText UserLastNameView;
    @BindView(R.id.et_user_bio) EditText UserBioView;
//TODO: Add input validation using: https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bio);
        ButterKnife.bind(this);
        //get info from newUserLogin if exists
        if(getIntent() != null) {
            Intent intent = getIntent();
            mUserEmail = intent.getStringExtra("userEmail");
            mUserPhoneNumber = intent.getStringExtra("userPhoneNumber");
            mUserId = intent.getStringExtra("userId");
            mUserAvatarNumberString = intent.getStringExtra("userAvatarNumber");
            UserEmailView.setText(mUserEmail);
            UserPhoneView.setText(mUserPhoneNumber);
        }
    }


    //sole purpose of method is to send notification check and remind user to
    // come back and fill out bio later!
    public void skipBio(View view){
        //TODO: send notification to confirm user not setting up bio
        //Stores any biodata entered into SP
        storeUserBioData(view);
    }

    public void storeUserBioData(View view){
    mUserEmail = UserEmailView.getText().toString();
        mUserPhoneNumber = UserPhoneView.getText().toString();
        mUserName = UserNameView.getText().toString();
        mUserFirstName = UserFirstNameView.getText().toString();
        mUserLastName = UserLastNameView.getText().toString();
        mUserBio = UserBioView.getText().toString();
        //TODO: GRAB USER AVATAR
        //mUserAvatarNumberString = 1;

        SharedPreferences.Editor editor =
                getSharedPreferences(getString(R.string.user_data_file),MODE_PRIVATE).edit();
        editor.putString(getString(R.string.user_email), mUserEmail);
        editor.putString(getString(R.string.user_phone_number), mUserPhoneNumber);
        editor.putString(getString(R.string.hint_user_name), mUserName);
        editor.putString(getString(R.string.user_first_name), mUserFirstName);
        editor.putString(getString(R.string.user_last_name), mUserLastName);
        editor.putString(getString(R.string.user_email), mUserEmail);
        editor.putString(getString(R.string.user_avatar_number), mUserAvatarNumberString);
        editor.putString(getString(R.string.user_bio), mUserBio);

        editor.apply();

        postUserBio();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    //posts saved user bio info to server
    public void postUserBio(){
        String newUserPost = "new user post";
        ServerConnect postUserToServer = new ServerConnect(this);
        postUserToServer.execute(newUserPost,mUserId, mUserName, mUserAvatarNumberString, mUserFirstName, mUserLastName,
                mUserPhoneNumber, mUserEmail, mUserBio);
    }
}
