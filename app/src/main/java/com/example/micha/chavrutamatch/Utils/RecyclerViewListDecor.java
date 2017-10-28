package com.example.micha.chavrutamatch.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by micha on 10/25/2017.
 */

public class RecyclerViewListDecor extends RecyclerView.ItemDecoration{
    private final int verticalSpaceHeight;

    public RecyclerViewListDecor(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
