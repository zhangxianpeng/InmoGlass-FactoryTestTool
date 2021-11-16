package com.inmoglass.factorytools.sensortest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;
import com.inmoglass.factorytools.R;

public class SensorTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.sensor_test_title);
        mApplication.updateSensorTestList();
        setTestList(mApplication.getSensorTestList());
    }
}
