package com.example.micha.chavrutamatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.micha.chavrutamatch.Utils.ImgUtils;

import java.io.File;
import java.io.IOException;


/**
 * Created by micha on 10/21/2017.
 */

public class AvatarSelectMasterList extends Activity implements AvatarSelectFragment.OnAvatarClickListener {
    Boolean updateBio = false;
    final int REQUEST_IMAGE_CAPTURE = 111;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
    String LOG_TAG = AvatarSelectMasterList.class.getSimpleName();
    private String mImgUriString, mTempPhotoPath;
    Uri tempImgUri;

    Bundle b;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, AddBio.class);
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
                startCamera();
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

    private void startCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = ImgUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();
                if(Build.VERSION.SDK_INT <24) {
                    tempImgUri = Uri.fromFile(photoFile);
                }else{
                    tempImgUri = FileProvider.getUriForFile(this,
                            FILE_PROVIDER_AUTHORITY,
                            photoFile);
                }
                mImgUriString = tempImgUri.toString();

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
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
                Intent returnToSelectorFrag = new Intent(this, AvatarSelectMasterList.class);
                startActivity(returnToSelectorFrag);
                return;
        }

        //image from user internal data
        if (requestCode != REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uploadedImg = data.getData();
            mImgUriString = uploadedImg.toString();
            intent.putExtra("img_uri_string_key", mImgUriString);

        } else if (resultCode == RESULT_OK ) {
            //image is from camera and successful
            String userImgPathString = mTempPhotoPath;
            intent.putExtra("img_uri_string_key", mImgUriString);
            intent.putExtra("img_file_path_string_key", userImgPathString);
        }else{
            // Otherwise, delete the temporary image file
            if(mTempPhotoPath != null)
            ImgUtils.deleteImageFile(this, mTempPhotoPath);
        }
        intent.putExtra("avatar position", 999);
        intent.putExtra("affirm update bio", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AddBio.class);
        Boolean updateBio = true;
        intent.putExtra("update_bio", updateBio);
        startActivity(intent);
    }

}

