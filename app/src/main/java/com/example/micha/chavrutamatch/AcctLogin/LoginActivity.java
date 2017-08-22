package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.micha.chavrutamatch.R;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
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
        //check for an existing access token
        AccessToken currentAccessToken = AccountKit.getCurrentAccessToken();
        if(currentAccessToken != null){
            //iff previously logged in, proceed to the account activity
            launchAccountActivity();
        }
    }

    //override to control AccountActivity.class launch
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check to makesure launched from our acctivity
        if(requestCode == APP_REQUEST_CODE){
            //extract loginResult from intent
            AccountKitLoginResult loginResult = data.getParcelableExtra(
                    AccountKitLoginResult.RESULT_KEY);
            //handle error login as well as login success
            if(loginResult.getError() != null){
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            }else if (loginResult.getAccessToken() != null){
                //on successful login, proceed to the account activity
                launchAccountActivity();
            }
        }
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
                        AccountKitActivity.ResponseType.TOKEN);

        final AccountKitConfiguration configuration = configurationBuilder.build();

        //launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        //startActivityForResult allows tracking of the login via onActivityResult method (overrided above)
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
