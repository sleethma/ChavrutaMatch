package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;
import com.example.micha.chavrutamatch.MainActivity;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;

import com.example.micha.chavrutamatch.R;
//import com.example.micha.chavrutamatch.Utils.FontHelper;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * Created by micha on 8/20/2017.
 */

public class AccountActivity extends AppCompatActivity{

    boolean isLoginSuccessful = false;
    @Inject
    Context context;



    public boolean verifyAccount() {

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                UserDetails.setmUserId(accountKitId);

//                                sp.edit().putString(context.getString(R.string.user_account_id_key), accountKitId).apply();

//                sp.edit().putBoolean("new_user_key", false);
                UserDetails.setNewUserKey(false);

                PhoneNumber phoneNumber = account.getPhoneNumber();


                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    UserDetails.setmUserPhoneNumber(formattedPhoneNumber);
                    //toggle user phone or email login
                    UserDetails.setLoginType("phone");
                } else {
                    // if the email address is available, store it
                    String emailString = account.getEmail();
                    UserDetails.setmUserEmail(emailString);
                    UserDetails.setLoginType("email");
                }
                setLoginSuccessful(true);
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e(this.getClass().getSimpleName(), "AccountKit could not verify account");
//                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
//                    startActivity(intent);
            }
        });

        return getLoginSuccessful();
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

    private void setLoginSuccessful(boolean isSuccessful){
        isLoginSuccessful = isSuccessful;
    }

    private boolean getLoginSuccessful(){
        return isLoginSuccessful;
    }
}