package com.example.micha.chavrutamatch;

import android.app.FragmentManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by micha on 10/21/2017.
 */

public class AvatarSelectMasterList extends Activity implements AvatarSelectFragment.OnAvatarClickListener{

    Boolean updateBio = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gv_avatar_frag);

        if(getIntent().getExtras().getBoolean("update_bio")){
            updateBio = getIntent().getExtras().getBoolean("update_bio");
        }
    }

    @Override
    public void onAvatarClick(int position) {
        Toast.makeText(this, "GridView Context" + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AddBio.class);
        Bundle b = new Bundle();
        b.putBoolean("affirm update bio", updateBio);
        b.putInt("avatar position", position);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
