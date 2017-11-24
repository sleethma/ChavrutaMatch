package com.example.micha.chavrutamatch;

import android.app.FragmentManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by micha on 10/21/2017.
 */

public class AvatarSelectMasterList extends Activity implements AvatarSelectFragment.OnAvatarClickListener {
    Boolean updateBio = false;
    String LOG_TAG = AvatarSelectMasterList.class.getSimpleName();
    private String mImgUriString;
    Bundle b;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, AddBio.class);
//        intent.setClass(this, AddBio.class);
        setContentView(R.layout.gv_avatar_frag);
        b = new Bundle();
        if (getIntent().getExtras().getBoolean("update_bio")) {
            updateBio = getIntent().getExtras().getBoolean("update_bio");
            b.putBoolean("affirm update bio", updateBio);

        }
    }

    @Override
    public void onAvatarClick(int position) {
        Toast.makeText(this, "positionClicked= " + position, Toast.LENGTH_SHORT).show();

        //adjusts position to account difference b/n @addAllAvatars() and @addAllAvatarsFramed() ArrayLists
        switch (position) {
            case 0:
                uploadImgFile();
                break;
            case 1:
                b.putInt("avatar position", 2);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                break;
            default:
                b.putInt("avatar position", position - 2);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                break;
        }

    }


    private void uploadImgFile() {
        Intent imgIntent;

        if (Build.VERSION.SDK_INT < 19) {
            imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            imgIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            imgIntent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        imgIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imgIntent, "Select Picture"), 1);
    }

    /**
     * manage the returned path after selecting the picture
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // action cancelled
            Log.e(LOG_TAG, "onActivityResult() was canceled");
        }
        if (resultCode == RESULT_OK) {
            Uri uploadedImg = data.getData();
            mImgUriString = uploadedImg.toString();
            Log.i(LOG_TAG, "file path from OnActivityResult imgUri: " + mImgUriString);
            intent.putExtra("img_uri_string_key", mImgUriString);
            intent.putExtra("avatar position", 999);
            intent.putExtra("affirm update bio", true);
            startActivity(intent);
            finish();

        }
    }
}
