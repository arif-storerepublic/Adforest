package com.scriptsbundle.adforest.helper;

import com.scriptsbundle.adforest.modelsList.PackagesModel;

public interface OnItemClickListenerPackages {
    void onItemClick(PackagesModel item);
    void onItemTouch();
    void onItemSelected(PackagesModel packagesModel,int spinnerPosition);
}
