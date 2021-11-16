package com.inmoglass.factorytools.sensortest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 指南针测试
 */
public class CompassSensorTestActivity extends AbstractTestActivity {

    private static final String TAG = CompassSensorTestActivity.class.getSimpleName();

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
    TextView tvGSensorName;
    TextView tvGSensorX;
    TextView tvGSensorY;
    TextView tvGSensorZ;

    float x;
    float z;
    float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.compass_test_title);
        setContentView(R.layout.layout_gravity_sensor_test);
        initView();
        mUpTxt = (TextView) findViewById(R.id.txt_sensor_arrow_up);
        mDownTxt = (TextView) findViewById(R.id.txt_sensor_arrow_down);
        mLeftTxt = (TextView) findViewById(R.id.txt_sensor_arrow_left);
        mRightTxt = (TextView) findViewById(R.id.txt_sensor_arrow_right);
        showMsg(0, 0, 0);
        /*SPRD bug 760913:Test can pass/fail must click button*/
        if (isBoardISharkL210c10()) {
//            mPassButton.setVisibility(View.GONE);
        }
        /*@}*/
        initSensor();
        test();
    }

    private void initView() {
        tvGSensorName = findViewById(R.id.txt_msg_gsensor);
        tvGSensorX = findViewById(R.id.txt_msg_gsensor_x);
        tvGSensorY = findViewById(R.id.txt_msg_gsensor_y);
        tvGSensorZ = findViewById(R.id.txt_msg_gsensor_z);
    }

    public static boolean isBoardISharkL210c10() {
        String board = SystemProperties.get("ro.product.board", "unknown");
        Log.d(TAG, "isBoardISharkL210c10 board=" + board);
        if (board != null && board.equals("sp9853i_10c10_vmm")) {
            return true;
        }
        if (board != null && board.equals("sp9832e_10c10_32b")) {
            return true;
        }
        return false;
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
                        Toast.makeText(CompassSensorTestActivity.this, "pass", Toast.LENGTH_SHORT).show();
                        /*SPRD bug 760913:Test can pass/fail must click button*/
                        if (isBoardISharkL210c10()) {
                            Log.d("", "isBoardISharkL210c10 is return!");
//                            mPassButton.setVisibility(View.VISIBLE);
                            return;
                        }
                        /*@}*/
//                        storeResult(true);
                        finish();
                    }
                }, 500);

            }
        }
    }

    private void showMsg(float x, float y, float z) {
        if (sensor != null) {
            tvGSensorName.setText(sensor.getName());
            tvGSensorX.setText(x + "");
            tvGSensorY.setText(y + "");
            tvGSensorZ.setText(z + "");
        } else {
            tvGSensorName.setText("");
            tvGSensorX.setText("0");
            tvGSensorY.setText("0");
            tvGSensorZ.setText("0");
        }
    }

    private void test() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (x != 0 && y != 0 && y != 0) {
                    Toast.makeText(CompassSensorTestActivity.this, "pass", Toast.LENGTH_SHORT).show();
                    storeResult(true);
                } else {
                    Toast.makeText(CompassSensorTestActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    storeResult(false);
                }
                finish();
            }
        }, 5000);
    }
    
    private void storeResult(boolean result) {
        
    }
}
