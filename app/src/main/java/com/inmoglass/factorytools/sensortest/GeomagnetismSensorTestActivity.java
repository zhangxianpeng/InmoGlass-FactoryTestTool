package com.inmoglass.factorytools.sensortest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 地磁
 */
public class GeomagnetismSensorTestActivity extends AbstractTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.geomagnetism_sensor_test_title);
        setContentView(R.layout.layout_ambient_light_test);
    }
}
