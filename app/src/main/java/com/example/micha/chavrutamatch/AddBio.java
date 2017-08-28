package com.example.micha.chavrutamatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 8/26/2017.
 */

public class AddBio extends AppCompatActivity {
    private static String LOG_TAG= AddBio.class.getSimpleName();
    String mUserEmail, mUserPhoneNumber;
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
    String userEmail = UserEmailView.getText().toString();
        String userPhoneNumber = UserPhoneView.getText().toString();
        String userName = UserNameView.getText().toString();
        String userFirstName = UserFirstNameView.getText().toString();
        String userLastName = UserLastNameView.getText().toString();
        String userBio = UserBioView.getText().toString();

        SharedPreferences.Editor editor =
                getSharedPreferences(getString(R.string.user_data_file),MODE_PRIVATE).edit();
        editor.putString(getString(R.string.user_email), userEmail);
        editor.putString(getString(R.string.user_phone_number), userPhoneNumber);
        editor.putString(getString(R.string.hint_user_name), userName);
        editor.putString(getString(R.string.user_first_name), userFirstName);
        editor.putString(getString(R.string.user_last_name), userLastName);
        editor.putString(getString(R.string.user_email), userEmail);
        editor.putString(getString(R.string.user_bio), userBio);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}
