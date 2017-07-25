package com.example.micha.chavrutamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onAddGuestButtonClick(View view) {
        Snackbar snackbar = Snackbar
                .make(view, "To Open Chavurta Hosts-->", Snackbar.LENGTH_LONG);
        snackbar.show();
        Intent hostSelectIntent = new Intent(this, HostSelect.class);
        startActivity(hostSelectIntent);
    }

    public void onAddHostButtonClick(View view) {
        Intent newHostIntent = new Intent(AddSelect.this, NewHost.class);
        startActivity(newHostIntent);

    }
}
