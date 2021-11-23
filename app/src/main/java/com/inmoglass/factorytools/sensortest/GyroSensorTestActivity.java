package com.inmoglass.factorytools.sensortest;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 陀螺仪测试
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class GyroSensorTestActivity extends AbstractTestActivity {
    private static final String TAG = GyroSensorTestActivity.class.getSimpleName();
    private static final int M_COMPARE_VALUE = 3;

    private SensorManager manager = null;
    private Sensor sensor = null;
    private SensorEventListener listener = null;

    private boolean onetime = true;
    private boolean xPass = false;
    private boolean yPass = false;
    private boolean zPass = false;

    private final float[] data_last = {0, 0, 0};
    private final float[] data_current = {0, 0, 0};

    private TextView mDisplayText;
    private TextView mXSensorText;
    private TextView mYSensorText;
    private TextView mZSensorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.sensor_test_gyro);
        setContentView(R.layout.activity_sensor_gyro);
        mDisplayText = findViewById(R.id.tv_msg_gyroscope);
        mXSensorText = findViewById(R.id.tv_sensor_x);
        mYSensorText = findViewById(R.id.tv_sensor_y);
        mZSensorText = findViewById(R.id.tv_sensor_z);
        displayXYZ(0, 0, 0);
        showPass(0, 0);
        showPass(0, 1);
        showPass(0, 2);
        initSensor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume...");
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause...");
        if (manager != null) {
            manager.unregisterListener(listener);
        }
        super.onPause();
    }

    private void showPass(int colorId, int postionId) {
        if (postionId == SensorManager.DATA_X) {
            if (colorId == 0) {
                mXSensorText.setTextColor(Color.WHITE);
                mXSensorText.setText("X-axis: ");
            } else {
                mXSensorText.setTextColor(Color.GREEN);
                mXSensorText.setText("X-axis: Pass!");
            }
        }
        if (postionId == SensorManager.DATA_Y) {
            if (colorId == 0) {
                mYSensorText.setTextColor(Color.WHITE);
                mYSensorText.setText("Y-axis: ");
            } else {
                mYSensorText.setTextColor(Color.GREEN);
                mYSensorText.setText("Y-axis: Pass!");
            }
        }
        if (postionId == SensorManager.DATA_Z) {
            if (colorId == 0) {
                mZSensorText.setTextColor(Color.WHITE);
                mZSensorText.setText("Z-axis: ");
            } else {
                mZSensorText.setTextColor(Color.GREEN);
                mZSensorText.setText("Z-axis: Pass!");
            }
        }
    }

    private boolean gSensorCheck(float data) {
        return Math.abs(data) > M_COMPARE_VALUE;
    }

    private void displayXYZ(float x, float y, float z) {
        mDisplayText.setText("\nX: " + x + "\nY: " + y + "\nZ: " + z);
    }

    private void initSensor() {
        Log.d(TAG, "Gyroscope test, init...");
        manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        listener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor s, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                Log.d(TAG, "mmi test values: " + x + "," + y + "," + z);
                displayXYZ(x, y, z);
                data_current[0] = x;
                data_current[1] = y;
                data_current[2] = z;
                if (onetime && (x != 0) && (y != 0) && (z != 0)) {
                    onetime = false;
                    data_last[0] = x;
                    data_last[1] = y;
                    data_last[2] = z;
                }
                if ((!xPass) && gSensorCheck(data_current[0]) && (data_current[0] != data_last[0])) {
                    xPass = true;
                    showPass(1, 0);
                }
                if (!yPass && gSensorCheck(data_current[1]) && (data_current[1] != data_last[1])) {
                    yPass = true;
                    showPass(1, 1);
                }
                if (!zPass && gSensorCheck(data_current[2]) && (data_current[2] != data_last[2])) {
                    zPass = true;
                    showPass(1, 2);
                }
                if (xPass && yPass && zPass) {
                    mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
                }
            }
        };
    }
}
