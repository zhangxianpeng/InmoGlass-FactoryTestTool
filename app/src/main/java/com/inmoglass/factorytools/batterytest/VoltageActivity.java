package com.inmoglass.factorytools.batterytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

public class VoltageActivity extends AbstractTestActivity {

    private TextView currentBatteryTv;
    private TextView batteryVoltageTv;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int voltage = intent.getIntExtra("voltage", 0);
            double dianya = voltage / 1000;
            int level = intent.getIntExtra("level", 0);
            int batterySum = intent.getIntExtra("scale", 100);
            int percentBattery = 100 * level / batterySum;
            batteryVoltageTv.setText(dianya + "");
            currentBatteryTv.setText(percentBattery + "");
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
        setTitle(R.string.battery_voltage_test_title);
        setContentView(R.layout.activity_voltage);
        initView();
    }

    private void initView() {
        currentBatteryTv = findViewById(R.id.current_battery);
        batteryVoltageTv = findViewById(R.id.battery_voltage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}