package com.inmoglass.factorytools.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 重力加速度测试项
 *
 * @author Administrator
 * @date 2021-11-16
 */
public class GravitySensorTestActivity extends AbstractTestActivity {
    private static final String TAG = GravitySensorTestActivity.class.getSimpleName();

    float x;
    float z;
    float y;

    private TextView mXValueTv;
    private TextView mYValueTv;
    private TextView mZValueTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_sensor_test);
        setTitle(R.string.sensor_test_acceleration_of_gravity);
        initView();
    }

    private void initView() {
        mXValueTv = findViewById(R.id.tv_sensor_x);
        mYValueTv = findViewById(R.id.tv_sensor_y);
        mZValueTv = findViewById(R.id.tv_sensor_z);
    }

    SensorManager manager;
    SensorEventListener listener;

    @Override
    protected void onResume() {
        super.onResume();
        manager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                LogUtils.i(TAG, x + "");
                LogUtils.i(TAG, y + "");
                LogUtils.i(TAG, z + "");

                mXValueTv.setText(x + "");
                mYValueTv.setText(y + "");
                mZValueTv.setText(z + "");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }
}
