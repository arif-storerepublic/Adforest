package com.scriptsbundle.adforest.helper;

import android.view.View;

import com.scriptsbundle.adforest.modelsList.catSubCatlistModel;

public interface CatSubCatOnclicklinstener {
    void onItemClick(catSubCatlistModel item);
    void onItemTouch(catSubCatlistModel item);
    void addToFavClick(View v, String position);

}
