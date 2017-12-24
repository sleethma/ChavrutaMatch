package com.example.micha.chavrutamatch.Data;

import com.example.micha.chavrutamatch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 10/15/2017.
 */


public class AvatarImgs {

    public static List<Integer> avatarImgList = new ArrayList<Integer>() {
        {
            add(R.drawable.ic_unknown_user);

            add(R.drawable.david_star_avatar);

            add(R.drawable.chai_avatar);

            add(R.drawable.israeli_flag_avatar);

            add(R.drawable.shalom_avatar);

            add(R.drawable.love_avatar);

            add(R.drawable.rainbow_heart_avatar);

            add(R.drawable.learn_tefillin_avatar);

            add(R.drawable.luchos_avatar);

            add(R.drawable.doggie_avatar_128);
        }
    };

    private static List<Integer> avatarImgListFramed = new ArrayList<Integer>() {
        {
            add(R.drawable.photo_upload_128_frame);

            add(R.drawable.upload_from_camera_128_frame);

            add(R.drawable.ic_unknown_user_128_frame);

            add(R.drawable.david_star_avatar_128_frame);

            add(R.drawable.chai_avatar_128_frame);

            add(R.drawable.israeli_flag_avatar_128_frame);

            add(R.drawable.shalom_avatar_128_frame);

            add(R.drawable.love_avatar_128_frame);

            add(R.drawable.rainbow_heart_avatar_128_frame);

            add(R.drawable.learn_tefillin_avatar_128_frame);

            add(R.drawable.luchos_avatar_128_frame);

            add(R.drawable.doggie_avatar_128_frame);
        }
    };



    public static List<Integer> getAllFramedAvatars() {
        return avatarImgListFramed;
    }

    public static List<Integer> getAllAvatars() {
        return avatarImgList;
    }

    public static int getAvatarNumberResId(int number) {
        return avatarImgList.get(number);
    }
}

