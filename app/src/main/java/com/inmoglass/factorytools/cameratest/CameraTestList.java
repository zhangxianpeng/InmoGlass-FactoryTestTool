package com.inmoglass.factorytools.cameratest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;

public class CameraTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.updateCameraTestList();
        setTestList(mApplication.getCameraTestList());
    }
}
