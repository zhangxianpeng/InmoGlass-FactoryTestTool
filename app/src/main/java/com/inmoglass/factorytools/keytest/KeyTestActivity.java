package com.inmoglass.factorytools.keytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 按键测试项
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class KeyTestActivity extends AbstractTestActivity {
    private static final String TAG = KeyTestActivity.class.getSimpleName();
    private TextView wannengResultTv;
    private TextView lockKeyResultTv;
    private String result = "";

    private boolean isLockKeyEnabled = false;
    private boolean isWannnegKeyEnabled = false;

    BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.key_test);
        setContentView(R.layout.activity_key_test);
        initViews();

        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_SCREEN_OFF);
        batteryFilter.addAction(Intent.ACTION_SCREEN_ON);
        batteryFilter.addAction(Intent.ACTION_USER_PRESENT);
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, batteryFilter);
    }

    class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i(TAG,"您点击了电源键,SCREEN_OFF");
                lockKeyResultTv.setText("您点击了电源键,SCREEN_OFF");
                isLockKeyEnabled = true;
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.i(TAG,"您点击了电源键,SCREEN_ON");
                lockKeyResultTv.setText("您点击了电源键,SCREEN_ON");
                isLockKeyEnabled = true;
            } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
                Log.i(TAG, "onReceive: 3");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    private void initViews() {
        wannengResultTv = findViewById(R.id.tv_wanneng_key_result);
        lockKeyResultTv = findViewById(R.id.tv_lock_key_result);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 289) {
            wannengResultTv.setText("您点击了万能键");
            isWannnegKeyEnabled = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setTestResult() {
        if (isLockKeyEnabled && isWannnegKeyEnabled) {
            mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
        }
    }

}
