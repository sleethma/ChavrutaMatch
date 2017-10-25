package com.example.micha.chavrutamatch.Data;

import com.example.micha.chavrutamatch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 10/15/2017.
 */


public class AvatarImgs {

    private static List<Integer> avatarImgList = new ArrayList<Integer>() {
        {
            add(R.drawable.ic_unknown_user);

            add(R.drawable.doggie_avatar_128);

            add(R.drawable.david_star_avatar);

            add(R.drawable.chai_avatar);

            add(R.drawable.israeli_flag_avatar);

            add(R.drawable.shalom_avatar);

            add(R.drawable.love_avatar);

            add(R.drawable.rainbow_heart_avatar);

            add(R.drawable.learn_tefillin_avatar);

            add(R.drawable.luchos_avatar);
        }
    };


    public static List<Integer> getAllAvatars() {
        return avatarImgList;
    }

    public static int getAvatarNumberResId(int number) {
        return avatarImgList.get(number);
    }
}

