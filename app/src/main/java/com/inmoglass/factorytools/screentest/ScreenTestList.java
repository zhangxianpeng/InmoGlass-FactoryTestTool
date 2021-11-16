package com.inmoglass.factorytools.screentest;

import android.os.Bundle;
import android.provider.Settings;

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
        setTitle(R.string.screen_test_title);
        mApplication.updateScreenTestList();
        setTestList(mApplication.getScreenTestList());
    }

    private boolean isAirplaneMode(){
        int isAirplaneMode = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) ;
        return (isAirplaneMode == 1);
    }
}
