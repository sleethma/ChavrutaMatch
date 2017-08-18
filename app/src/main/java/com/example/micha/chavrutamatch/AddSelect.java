package com.example.micha.chavrutamatch;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_select);
        ButterKnife.bind(this);

    }
//TODO fix bug on animate the imagebutton is gone on back pressed
    public void onAddGuestButtonClick(View view) {
        final Context currentContext = getBaseContext();
        addGuestButton.animate()
                .alpha(0f)
                .translationX(-addGuestButton.getWidth())
                .setDuration(getResources().getInteger(
                        android.R.integer.config_shortAnimTime))
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //end action ran after animation


                    }
                });

        String getJSONKey = "getJSONKey";
        ServerConnect getJSONFromServer = new ServerConnect(currentContext);
        getJSONFromServer.execute(getJSONKey);
//        Intent hostSelectIntent = new Intent(AddSelect.this, HostSelect.class);
//        startActivity(hostSelectIntent);

    }

    public void onAddHostButtonClick(View view) {
        //     WORKS   view.animate().alpha(0);
        //   WORKS  view.animate().translationX(600).withLayer();

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
    }

    private void fadeAnimation(View v) {
        v.animate().alpha(0);
    }
}
