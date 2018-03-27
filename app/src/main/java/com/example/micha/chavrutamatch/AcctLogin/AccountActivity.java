package com.example.micha.chavrutamatch.AcctLogin;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;

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

public class AccountActivity extends AppCompatActivity{

    public void setAccountKitAcct() {

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                UserDetails.setCurrentUserIdForThisSession(accountKitId);
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
//                setLoginSuccessful(true);
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e(this.getClass().getSimpleName(), "AccountKit could not verify account");
            }
        });

//        return getLoginSuccessful();
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

//    private void setLoginSuccessful(boolean isSuccessful){
//        isLoginSuccessful = isSuccessful;
//    }

//    private boolean getLoginSuccessful(){
//        return isLoginSuccessful;
//    }
}