package com.inmoglass.factorytools.batterytest;

import static android.os.BatteryManager.EXTRA_TEMPERATURE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 电池测试项
 *
 * @author Administrator
 * @date 2021-11-18
 */
public class VoltageActivity extends AbstractTestActivity {
    private static final String TAG = VoltageActivity.class.getSimpleName();
    private TextView mChargingStatusTv;
    private TextView mLeftBatteryTv;
    private TextView mCurrentDianliuTv;
    private TextView mCurrentDianyaTv;
    private TextView mBatteryTemTv;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 充电状态
            boolean isCharging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
            mChargingStatusTv.setText(isCharging ? getString(R.string.battery_test_charging) : getString(R.string.battery_test_no_charging));

            // 电量
            int level = intent.getIntExtra("level", 0);
            int batterySum = intent.getIntExtra("scale", 100);
            int percentBattery = 100 * level / batterySum;
            mLeftBatteryTv.setText(percentBattery + "");

            // 电压
            int voltage = intent.getIntExtra("voltage", 0);
            double dianya = voltage / 1000;
            mCurrentDianyaTv.setText(dianya + "");

            // 电池温度
            int temp = intent.getIntExtra(EXTRA_TEMPERATURE, 0);
            mBatteryTemTv.setText(temp + "");

            if (mIsAutoTest) {
                if (dianya >= 3.8 && dianya <= 4.2) {
                    mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.battery_test_info);
        setContentView(R.layout.activity_voltage);
        initViews();
    }

    private void initViews() {
        mChargingStatusTv = findViewById(R.id.tv_charging_status);
        mLeftBatteryTv = findViewById(R.id.tv_left_battery);
        mCurrentDianliuTv = findViewById(R.id.tv_current_dianliu);
        mCurrentDianyaTv = findViewById(R.id.tv_current_dianya);
        mBatteryTemTv = findViewById(R.id.tv_battery_temperature);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);

        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);

        // BATTERY_PROPERTY_CURRENT_AVERAGE 平均电流 单位：微安
        LogUtils.i("BATTERY_PROPERTY_CURRENT_AVERAGE=" + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE));
        // 瞬时电流 单位：微安
        int current_now = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        mCurrentDianliuTv.setText(current_now / 1000 + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}