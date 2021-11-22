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
import android.widget.Toast;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.util.CommonUtils;

/**
 * 陀螺仪传感器测试
 */
public class GyroSensorTestActivity extends AbstractTestActivity {
    private static final String TAG = GyroSensorTestActivity.class.getSimpleName();
    private static final int M_COMPAR_VALUE = 3;

    private SensorManager manager = null;
    private Sensor sensor = null;
    private SensorEventListener listener = null;
    private Context mContext;

    private boolean onetime = true;
    private boolean xPass = false;
    private boolean yPass = false;
    private boolean zPass = false;

    private float[] data_last = {0, 0, 0};
    private float[] data_current = {0, 0, 0};

    private TextView mDisplayText;
    private TextView mXsensorText;
    private TextView mYsensorText;
    private TextView mZsensorText;

    private boolean mPassed = false;
    private boolean mSuccessFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.sensor_test_gyro);
        setContentView(R.layout.activity_sensor_gyro);
        mContext = this;
        mDisplayText = (TextView) findViewById(R.id.txt_msg_gyroscope);
        mXsensorText = (TextView) findViewById(R.id.txt_sensor_x);
        mYsensorText = (TextView) findViewById(R.id.txt_sensor_y);
        mZsensorText = (TextView) findViewById(R.id.txt_sensor_z);
        diplayXYZ(0, 0, 0);
        showPass(0, 0);
        showPass(0, 1);
        showPass(0, 2);
        /*SPRD bug 760913:Test can pass/fail must click button*/
        if (CommonUtils.isBoardISharkL210c10()) {
//            mPassButton.setVisibility(View.GONE);
        }
        /*@}*/
        initSensor();
    }

    private void diplayXYZ(float x, float y, float z) {
        mDisplayText.setText("\nX: " + x + "\nY: " + y + "\nZ: " + z);
    }

    private void showPass(int colorId, int postionId) {
        if (postionId == SensorManager.DATA_X) {
            if (colorId == 0) {
                mXsensorText.setTextColor(Color.WHITE);
                mXsensorText.setText("X-axis: ");
            } else {
                mXsensorText.setTextColor(Color.GREEN);
                mXsensorText.setText("X-axis: Pass!");
            }
        }
        if (postionId == SensorManager.DATA_Y) {
            if (colorId == 0) {
                mYsensorText.setTextColor(Color.WHITE);
                mYsensorText.setText("Y-axis: ");
            } else {
                mYsensorText.setTextColor(Color.GREEN);
                mYsensorText.setText("Y-axis: Pass!");
            }
        }
        if (postionId == SensorManager.DATA_Z) {
            if (colorId == 0) {
                mZsensorText.setTextColor(Color.WHITE);
                mZsensorText.setText("Z-axis: ");
            } else {
                mZsensorText.setTextColor(Color.GREEN);
                mZsensorText.setText("Z-axis: Pass!");
            }
        }
    }

    private void initSensor() {
        Log.d(TAG, "Gyroscope test, init...");
        /*SPRD bug 835673:Maybe cause ANR*/
        mPassed = false;
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
                diplayXYZ(x, y, z);
                data_current[0] = x;
                data_current[1] = y;
                data_current[2] = z;
                if (onetime && (x != 0) && (y != 0) && (z != 0)) {
                    onetime = false;
                    data_last[0] = x;
                    data_last[1] = y;
                    data_last[2] = z;
                }
                if ((!xPass) && gsensorCheck(data_current[0])
                        && (data_current[0] != data_last[0])) {
                    xPass = true;
                    showPass(1, 0);
                }
                if (!yPass && gsensorCheck(data_current[1])
                        && (data_current[1] != data_last[1])) {
                    yPass = true;
                    showPass(1, 1);
                }
                if (!zPass && gsensorCheck(data_current[2])
                        && (data_current[2] != data_last[2])) {
                    zPass = true;
                    showPass(1, 2);
                }
                if (xPass && yPass && zPass) {
                    /*SPRD bug 760913:Test can pass/fail must click button*/
                    if (CommonUtils.isBoardISharkL210c10()) {
                        Log.d(TAG, "isBoardISharkL210c10 is return!");
                        if (!mSuccessFlag) {
//                            mPassButton.setVisibility(View.VISIBLE);
                            Toast.makeText(mContext, "pass", Toast.LENGTH_SHORT).show();
                        }
                        mSuccessFlag = true;
                        return;
                    }
                    if (!mPassed) {
                        mPassed = true;
                        Toast.makeText(mContext, "pass", Toast.LENGTH_SHORT).show();
                    }
//                    storeRusult(true);
                    if (!isFinishing() && !isDestroyed()) {
                        finish();
                    }
                }
            }
        };
    }

    private boolean gsensorCheck(float data) {
        if (Math.abs(data) > M_COMPAR_VALUE) {
            return true;
        }
        return false;
    }

}
