package com.example.micha.chavrutamatch;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.micha.chavrutamatch.Data.AvatarImgs;

import java.util.List;

/**
 * Created by micha on 10/21/2017.
 */

public class AvatarSelectFragment extends Fragment {
    AvatarSelectAdapter mAdapter;
    List<Integer> mImageIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.avatar_select_frag, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gv_avatar_list);

        mAdapter = new AvatarSelectAdapter(getContext(), AvatarImgs.getAllAvatars());

//        mAdapter = new AvatarSelectAdapter(getContext(), AvatarImgs.getAllAvatars());

        gridView.setAdapter(mAdapter);



        return rootView;
    }

    public void setAvatarImageIds(List<Integer> avatarImageIds){
        mImageIds = avatarImageIds;
    }

}
