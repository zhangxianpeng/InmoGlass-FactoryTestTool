package com.inmoglass.factorytools.sensortest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 陀螺仪
 */
public class GyroSensorTestActivity extends AbstractTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.gyro_sensor_test_title);
        setContentView(R.layout.layout_proximity_sensor_test);
    }
}
