package com.inmoglass.factorytools.batterytest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;

public class BatteryTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.updateBatteryTestList();
        setTestList(mApplication.getBatteryTestList());
    }
}
