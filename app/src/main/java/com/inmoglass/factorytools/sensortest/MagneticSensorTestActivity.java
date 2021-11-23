package com.inmoglass.factorytools.sensortest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 磁力传感器测试
 *
 * @author Administrator
 * @date 2021-11-18
 */
public class MagneticSensorTestActivity extends AbstractTestActivity {
    private static final String TAG = MagneticSensorTestActivity.class.getSimpleName();
    private static final int M_COMPAR_VALUE = 20;
    private float[] data_last = {0, 0, 0};
    private float[] data_current = {0, 0, 0};
    private SensorManager mSManager = null;
    private Sensor mSensor = null;
    private SensorEventListener mListener = null;
    private Context mContext;
    private TextView mWarnText;
    private TextView mDisplayText;
    private boolean onetime = true;
    private boolean xPass = false;
    private boolean yPass = false;
    private boolean zPass = false;
    private boolean mSuccessFlag = false;
    private boolean mFailFlag = false;

    private static final long TEST_FAIL_TIMEOUT = 5000l;
    private Handler myHandler = new Handler();
    private Runnable mFailRunnable = new Runnable() {
        public void run() {
            if (xPass || yPass || zPass) {
                Log.d(TAG, "mFailRunnable already success xPass=" + xPass + ",yPass=" + yPass + ",zPass=" + zPass);
                return;
            }
            Log.d(TAG, "mFailRunnable test fail finish!");
            mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_sensor_test);
        mDisplayText = findViewById(R.id.tv_magnetic_xyz);
        displayXYZ(0, 0, 0);
        initSensor();
        myHandler.postDelayed(mFailRunnable, TEST_FAIL_TIMEOUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(mFailRunnable);
    }

    private void displayXYZ(float x, float y, float z) {
        mDisplayText.setText("\nX: " + x + "\nY: " + y + "\nZ: " + z);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        if (mSManager != null) {
            mSManager.unregisterListener(mListener);
        }
        super.onPause();
    }

    private void initSensor() {
        mSManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor s, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                Log.d(TAG, "mmi test values: " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
                myHandler.removeCallbacks(mFailRunnable);
                displayXYZ(event.values[0], event.values[1], event.values[2]);
                data_current[0] = event.values[0];
                data_current[1] = event.values[1];
                data_current[2] = event.values[2];
                if (onetime && (data_current[0] != 0) && (data_current[1] != 0) && (data_current[2] != 0)) {
                    onetime = false;
                    data_last[0] = data_current[0];
                    data_last[1] = data_current[1];
                    data_last[2] = data_current[2];
                }
                if (Math.abs(data_current[0] - data_last[0]) > M_COMPAR_VALUE) {
                    xPass = true;
                }
                if (Math.abs(data_current[1] - data_last[1]) > M_COMPAR_VALUE) {
                    yPass = true;
                }
                if (Math.abs(data_current[1] - data_last[1]) > M_COMPAR_VALUE) {
                    zPass = true;
                }
                if (xPass && yPass && zPass) {
                    mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
                }
            }
        };
    }
}
