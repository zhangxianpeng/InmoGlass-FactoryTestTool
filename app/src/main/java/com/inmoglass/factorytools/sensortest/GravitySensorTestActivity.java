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

import java.util.Timer;
import java.util.TimerTask;

/**
 * 重力传感器测试
 *
 * @author Administrator
 * @date 2021-11-16
 */
public class GravitySensorTestActivity extends AbstractTestActivity {
    private static final String TAG = GravitySensorTestActivity.class.getSimpleName();

    private static final int DATA_X = 0;

    private static final int DATA_Y = 1;

    private static final int DELAY_TIME = 300;

    /**
     * sensor manager object
     */
    private SensorManager manager = null;

    /**
     * sensor object
     */
    private Sensor sensor = null;

    /**
     * sensor listener object
     */
    private SensorEventListener listener = null;

    private Timer mTimer;

    private float[] mValues;
    public Handler mHandler = new Handler();

    private boolean mDxOk = false;
    private boolean mDyOk = false;
    private boolean mDzOk = false;
    private TextView mUpTxt;
    private TextView mDownTxt;
    private TextView mLeftTxt;
    private TextView mRightTxt;
    private boolean mIsShowDialog = true;

    float x;
    float z;
    float y;
    private TextView gSensorNameTv;
    private TextView mXValueTv;
    private TextView mYValueTv;
    private TextView mZValueTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity_sensor_test);
        setTitle(R.string.sensor_test_acceleration_of_gravity);
        initView();
        mUpTxt = (TextView) findViewById(R.id.txt_sensor_arrow_up);
        mDownTxt = (TextView) findViewById(R.id.txt_sensor_arrow_down);
        mLeftTxt = (TextView) findViewById(R.id.txt_sensor_arrow_left);
        mRightTxt = (TextView) findViewById(R.id.txt_sensor_arrow_right);
        showMsg(0, 0, 0);
        initSensor();
        test();
    }

    private void initView() {
        gSensorNameTv = findViewById(R.id.tv_msg_gsensor);
        mXValueTv = findViewById(R.id.tv_sensor_x);
        mYValueTv = findViewById(R.id.tv_sensor_y);
        mZValueTv = findViewById(R.id.tv_sensor_z);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mValues != null) {
                            float x = mValues[DATA_X];
                            float y = mValues[DATA_Y];
                            if (Math.abs(x) < 1) {
                                x = 0;
                            }
                            if (Math.abs(y) < 1) {
                                y = 0;
                            }
                            showArrow(x, y);
                        }
                    }
                });
            }
        }, 0, DELAY_TIME);
    }

    @Override
    protected void onPause() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        manager.unregisterListener(listener);
        super.onPause();
    }

    private void initSensor() {
        listener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor s, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                mValues = event.values;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
                showMsg(x, y, z);
                double dx = Math.abs(9.8 - Math.abs(x));
                double dy = Math.abs(9.8 - Math.abs(y));
                double dz = Math.abs(9.8 - Math.abs(z));
                double ref = 9.8 * 0.08;
                if (!mDxOk)
                    mDxOk = dx < ref;
                if (!mDyOk)
                    mDyOk = dy < ref;
                if (!mDzOk)
                    mDzOk = dz < ref;
                if (mDxOk && mDyOk && mDzOk) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("testa", "run:go ");
                        }
                    }, 10);
                }
            }
        };

        manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void showArrow(float x, float y) {
        int arrowId = 0;
        if (Math.abs(x) <= Math.abs(y)) {
            if (y < 0) {
                // up is low
                arrowId = R.drawable.arrow_up;
                mUpTxt.setBackgroundResource(arrowId);
            } else if (y > 0) {
                // down is low
                arrowId = R.drawable.arrow_down;
                mDownTxt.setBackgroundResource(arrowId);
            } else if (y == 0) {
                // do nothing
            }
        } else {
            if (x < 0) {
                // right is low
                arrowId = R.drawable.arrow_right;
                mRightTxt.setBackgroundResource(arrowId);
            } else {
                // left is low
                arrowId = R.drawable.arrow_left;
                mLeftTxt.setBackgroundResource(arrowId);
            }
            if (arrowId == 0) {
                mUpTxt.setBackgroundDrawable(null);
                mDownTxt.setBackgroundDrawable(null);
                mLeftTxt.setBackgroundDrawable(null);
                mRightTxt.setBackgroundDrawable(null);
            }
            if (mUpTxt.getBackground() != null && mDownTxt.getBackground() != null && mLeftTxt.getBackground() != null && mRightTxt.getBackground() != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
                    }
                }, 500);
            }
        }
    }

    private void showMsg(float x, float y, float z) {
        if (sensor != null) {
            gSensorNameTv.setText(sensor.getName());
            mXValueTv.setText(x + "");
            mYValueTv.setText(y + "");
            mZValueTv.setText(z + "");
        } else {
            gSensorNameTv.setText("");
            mXValueTv.setText("0");
            mYValueTv.setText("0");
            mZValueTv.setText("0");
        }
    }

    private void test() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (x != 0 && y != 0 && y != 0) {
                    mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
                }
            }
        }, 5000);
    }
}
