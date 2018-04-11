package com.example.micha.chavrutamatch.AcctLogin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.DI.Components.ApplicationComponent;
import com.example.micha.chavrutamatch.DI.Components.DaggerMAComponent;
import com.example.micha.chavrutamatch.DI.Components.MAComponent;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
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

public class AccountActivity extends AppCompatActivity{

//    @Inject
    public UserDetails userDetailsInstance;
    private Context context;

    @Inject
    public AccountActivity(UserDetails userDetailsInstance){
        this.userDetailsInstance = userDetailsInstance;
        context = this;


//        ChavrutaMatch.get(this).getMAComponent().inject(this);

//        ApplicationComponent appComponent = ((ChavrutaMatch.get(this)).getApplicationComponent());
//        MAComponent maComponent = DaggerMAComponent.builder()
//                .mAModule(new MAModule(this))
//                .applicationComponent(appComponent)
//                .build();
//        maComponent.inject(this);
    }

    public void setAccountKitAcct() {

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final com.facebook.accountkit.Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
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