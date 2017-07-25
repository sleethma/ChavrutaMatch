package com.example.micha.chavrutamatch;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by micha on 7/22/2017.
 */

public class HostSelect extends AppCompatActivity {

    private OpenChavrutaAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_host_listview);


    }
}
