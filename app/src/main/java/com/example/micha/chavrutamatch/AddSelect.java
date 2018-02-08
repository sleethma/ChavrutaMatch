package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.ServerConnect;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/9/2017.
 */

public class AddSelect extends AppCompatActivity {

    @BindView(R.id.b_learn_chavruta)
    ImageButton addGuestButton;
    @BindView(R.id.b_host_chavruta)
    ImageButton addHostButton;
    SharedPreferences sp;

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_select);
        ButterKnife.bind(this);
        sp = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);

        //get Context and send to UserDetails for SharedPreferences access
        mContext = AddSelect.this;
        UserDetails.setsApplicationContext(mContext);

    }

    public void onAddGuestButtonClick(View view) {

        if (isPrivacyPolicyChecked()) {
            //sets user city and state for ServerConnect call
            String userCityState = sp.getString(getString(R.string.user_city_state_key), null);
            UserDetails.setUserCityState(userCityState);
            String getJSONKey = "getJSONKey";
            ServerConnect getJSONFromServer = new ServerConnect(mContext);
            getJSONFromServer.execute(getJSONKey);
        } else {
            returnToAddBio();
        }
    }


    public void onAddHostButtonClick(View view) {
        if (isPrivacyPolicyChecked()) {
            addHostButton.animate()
                    .alpha(0f)
                    .translationX(-addHostButton.getWidth())
                    .setDuration(getResources().getInteger(
                            android.R.integer.config_shortAnimTime))
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            Intent newHostIntent = new Intent(AddSelect.this, NewHost.class);
                            startActivity(newHostIntent);
                        }
                    });
        } else {
            returnToAddBio();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addHostButton.clearAnimation();
        addGuestButton.clearAnimation();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        addHostButton.clearAnimation();
        addGuestButton.clearAnimation();
    }


    private void fadeAnimation(View v) {
        v.animate().alpha(0);
    }

    private boolean isPrivacyPolicyChecked() {
        if (sp.getBoolean(getString(R.string.user_priv_policy_consent), false)) {
            return true;
        }else {
            return false;
        }
    }

    private void returnToAddBio() {
        Intent addBioIntent = new Intent(this, AddBio.class);
        addBioIntent.putExtra("update_bio", true);
        startActivity(addBioIntent);
    }
}
