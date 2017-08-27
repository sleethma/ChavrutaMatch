package com.example.micha.chavrutamatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.LoginActivity;
import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //TODO add up nav arrow to each activity
    @BindView(R.id.iv_no_match_add_match)
    ImageView noMatchView;
    public static final String USER_DATA_FILE = "user_data";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        noMatchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateTransition(v);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSelect.class);
                startActivity(intent);
            }
        });
        //activate user details class
        final UserDetails userDetails = new UserDetails();


        //check if already logged in
        //get current account and create new anonymous inner class
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                userDetails.setmUserId(accountKitId);
                //stores user id, email, or phone in SP
                SharedPreferences.Editor editor = getSharedPreferences(USER_DATA_FILE, MODE_PRIVATE).edit();
                editor.putString(getString(R.string.user_account_id_key), accountKitId);
                editor.putBoolean("new_user_key", false);

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    userDetails.setmUserPhoneNumber(formattedPhoneNumber);
                    editor.putString(getString(R.string.user_phone_key), formattedPhoneNumber);

                } else {
                    // if the email address is available, store it
                    String emailString = account.getEmail();
                    userDetails.setmUserEmail(emailString);
                    editor.putString(getString(R.string.user_email_key), emailString);
                }
                editor.apply();
            }

            @Override
            public void onError(final AccountKitError error) {
                //display error
//                String toastMessage = error.getErrorType().getMessage();
//                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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
        if (id == R.id.action_settings) {
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

    //Account Activity from FB login
    public void onLogout(View view) {
        // logout of Account Kit
        AccountKit.logOut();
        launchLoginActivity();
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
}

