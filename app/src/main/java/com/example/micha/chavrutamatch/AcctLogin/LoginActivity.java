package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.micha.chavrutamatch.R;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

/**
 * Created by micha on 8/19/2017.
 */

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_type_select);
    }


    //@param type comes from AccountKit and this function implements token request
    private void onLogin(final LoginType loginType){
        //create intent for the Account Kit activity
         final Intent intent = new Intent(this, AccountKitActivity.class);

        //configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        //if NOT on own server, pass in TOKEN in place of CODE response type
                        AccountKitActivity.ResponseType.CODE);

        final AccountKitConfiguration configuration = configurationBuilder.build();

        //launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    //called when user enters phone#
    public void onPhoneLogin(View view){
        onLogin(LoginType.PHONE);
    }
    //called when user enters email
    public void onEmailLogin(View view){
        onLogin(LoginType.EMAIL);
    }

    private void launchAccountActivity(){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }
}
