package com.example.micha.chavrutamatch;

import android.animation.Animator;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/22/2017.
 */

public class HostSelect extends AppCompatActivity {

    private OpenChavrutaAdapter mAdapter;
@BindView(R.id.fl_host_pic)
    FrameLayout flHostPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_host_listview);
        ButterKnife.bind(this);

        flHostPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                makeCircularRevealAnim(v);
            }
        });


    }
    private void makeCircularRevealAnim(View v){
        int finalRadius = (int) Math.hypot(v.getWidth()/2, v.getHeight()/2);

        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, (int) v.getWidth()/2, (int) v.getHeight()/2, 0, finalRadius);
        v.setBackgroundColor(Color.GREEN);
        anim.start();
    }
}
