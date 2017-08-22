package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.micha.chavrutamatch.MainActivity;
import com.facebook.accountkit.AccountKit;

import com.example.micha.chavrutamatch.R;
//import com.example.micha.chavrutamatch.Utils.FontHelper;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Locale;


/**
 * Created by micha on 8/20/2017.
 */

public class AccountActivity extends AppCompatActivity {


    EditText userName;
    EditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Deleted because font won't load in AS FontHelper.setCustomTypeface(findViewById(R.id.view_root));

         userName= (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);

        //get current account and create new anonymous inner class
        AccountKit.getCurrentAccount(new AccountKitCallback<com.facebook.accountkit.Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                userName.setText(accountKitId);

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    password.setText(formattedPhoneNumber);
                    //infoLabel.setText(R.string.password_label);
                }
                else {
                    // if the email address is available, display it
                    String emailString = account.getEmail();
                    password.setText(emailString);
                    //infoLabel.setText(R.string.email_label);
                }

            }

            @Override
            public void onError(final AccountKitError error){
                //display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });

    }

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
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

}