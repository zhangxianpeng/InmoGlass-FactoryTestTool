package com.inmoglass.factorytools.batterytest;

import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.util.CommandExecutionUtil;

/**
 * 待机电流测试项
 *
 * @author Administrator
 * @date 2021-11-18
 */
public class StandByCurrentTestActivity extends AbstractTestActivity {

    private static final String TAG = StandByCurrentTestActivity.class.getSimpleName();
    private TextView mStandByCurrentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.battery_test_info);
        setContentView(R.layout.activity_stand_by_current);
        mStandByCurrentTv = findViewById(R.id.tv_standby_dianliu);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 实测会造成卡顿，不能使用此指令进行休眠
//                String adb = "input keyevent 26";
//                CommandExecutionUtil.CommandResult result = CommandExecutionUtil.execCommand(adb, false);
                mHandler.sendEmptyMessage(10000);
            }
        }, 5000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            if (action == 10000) {
                BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
                // 瞬时电流 单位：微安
                int current_now = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
                mStandByCurrentTv.setText(current_now / 1000 + "");
                // TODO: 2021/11/25 待机电流标准
            }
        }
    };
}