package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import junit.framework.Assert;

import javax.inject.Inject;

/**
 * Created by micha on 8/19/2017.
 */

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 1;
    public static boolean mIsConnected;
    SharedPreferences prefs;
    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    Context context;

    @Inject
    UserDetails userDetailsInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_type_select);
        prefs = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);
        context = this;
        ChavrutaMatch.get(this).getMAComponent().inject(this);
        AccessToken currentAccessToken = AccountKit.getCurrentAccessToken();
        if (currentAccessToken != null) {
            launchMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(
                    AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null) {
                Boolean newUserOnThisDevice = true;
                String lastUsedUserId = prefs.getString(getString(R.string.user_account_id_key), null);
                String currentUserId = loginResult.getAccessToken().getAccountId();
                UserDetails.setmUserId(currentUserId);
                //todo: uncomment below if need to check if new user logging in!

//                if (lastUsedUserId != null &&
//                        lastUsedUserId.equals(currentUserId)) newUserOnThisDevice = false;
/*                if (newUserOnThisDevice) {
                    SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE).edit();
                    editor.putBoolean("new_user_key", false);
                    UserDetails.setmUserId(currentUserId);
                    editor.putString(getString(R.string.user_account_id_key), currentUserId);
                    editor.apply();
                    launchAddBioActivityForNewUser();
                } else {*/
                    launchMainActivity();
//                }
            }
        }else {
            Log.e(LOG_TAG, "request code failed on login");
        }
    }


    //@param type comes from AccountKit and this function implements token request
    private void onLogin(final LoginType loginType) {
        internetCheck(this);
        if (mIsConnected) {
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
        } else {
            Toast.makeText(this, "check internet connection for login", Toast.LENGTH_LONG).show();
        }
    }

    //called when user enters phone#
    public void onPhoneLogin(View view) {
        onLogin(LoginType.PHONE);
    }

    //called when user selects email
    public void onEmailLogin(View view) {
        onLogin(LoginType.EMAIL);
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchAddBioActivityForNewUser() {
        Intent intent = new Intent(this, AddBio.class);
        startActivity(intent);
        finish();
    }
    private void launchMainActivityForPreviousUser() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_data_json_string",ChavrutaMatch.getMyChavrutaJsonString());
        startActivity(intent);
        finish();
    }

    private void internetCheck(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Assert.assertNotNull(cm);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        mIsConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (mIsConnected) Toast.makeText(this, "Internet Connected!", Toast.LENGTH_SHORT).show();
    }

}
