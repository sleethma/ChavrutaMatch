package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

import javax.inject.Inject;

//import com.example.micha.chavrutamatch.Utils.FontHelper;


/**
 * Created by micha on 8/20/2017.
 */

public class AccountActivity extends AppCompatActivity {

    public UserDetails userDetailsInstance;
    public static SharedPreferences sharedPreferences;
    private Context context;

    @Inject
    public AccountActivity(UserDetails userDetailsInstance, SharedPreferences sharedPreferences) {
        this.userDetailsInstance = userDetailsInstance;
        this.sharedPreferences = sharedPreferences;
        context = this;
    }

    public void setAccountKitAcct() {

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get and set Account Kit ID
                userDetailsInstance.setUserId(account.getId());
                //sets so presenter can access and is aware of
                PhoneNumber phoneNumber = account.getPhoneNumber();

                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    userDetailsInstance.setmUserPhoneNumber(formattedPhoneNumber);
                    //toggle user phone or email login
                    userDetailsInstance.setLoginType("phone");
                } else {
                    // if the email address is available, store it
                    String emailString = account.getEmail();
                    userDetailsInstance.setmUserEmail(emailString);
                    userDetailsInstance.setLoginType("email");
                }
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e(this.getClass().getSimpleName(), "AccountKit could not verify account");
            }
        });
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