package com.inmoglass.factorytools.screentest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;
import com.inmoglass.factorytools.R;

public class ScreenTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.screen_test);
        mApplication.updateScreenTestList();
        setTestList(mApplication.getScreenTestList());
    }
}
