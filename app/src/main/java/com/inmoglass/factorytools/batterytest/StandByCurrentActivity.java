package com.inmoglass.factorytools.batterytest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;

public class StandByCurrentActivity extends AbstractTestActivity {

    private TextView standbyCurrentTv;
    private boolean mIsStart = true;
    private Toast mToast;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            showToast(getCurrent());
            if (mIsStart) {
                // 因为Toast.LENGTH_SHORT的默认值是2000
                mHandler.sendEmptyMessageDelayed(0, 1900);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_by_current);
        standbyCurrentTv = findViewById(R.id.standby_current);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 获取当前电流
     */
    private String getCurrent() {
        String result = "null";
        try {
            Class systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getDeclaredMethod("get", String.class);
            String platName = (String) get.invoke(null, "ro.hardware");
            if (platName.startsWith("mt") || platName.startsWith("MT")) {
                String filePath = "/sys/class/power_supply/battery/device/FG_Battery_CurrentConsumption";
                // MTK平台该值不区分充放电，都为负数，要想实现充放电电流增加广播监听充电状态即可
                result = "当前电流为：" + Math.round(getMeanCurrentVal(filePath, 5, 0) / 10.0f) + "mA";
                result += ", 电压为：" + readFile("/sys/class/power_supply/battery/batt_vol", 0) + "mV";
            } else if (platName.startsWith("qcom")) {
                String filePath = "/sys/class/power_supply/battery/current_now";
                int current = Math.round(getMeanCurrentVal(filePath, 5, 0) / 10.0f);
                int voltage = readFile("/sys/class/power_supply/battery/voltage_now", 0) / 1000;
                // 高通平台该值小于0时电池处于放电状态，大于0时处于充电状态
                if (current < 0) {
                    result = "充电电流为：" + (-current) + "mA, 电压为：" + voltage + "mV";
                } else {
                    result = "放电电流为：" + current + "mA, 电压为：" + voltage + "mV";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取平均电流值
     * 获取 filePath 文件 totalCount 次数的平均值，每次采样间隔 intervalMs 时间
     */
    private float getMeanCurrentVal(String filePath, int totalCount, int intervalMs) {
        float meanVal = 0.0f;
        if (totalCount <= 0) {
            return 0.0f;
        }
        for (int i = 0; i < totalCount; i++) {
            try {
                float f = Float.valueOf(readFile(filePath, 0));
                meanVal += f / totalCount;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (intervalMs <= 0) {
                continue;
            }
            try {
                Thread.sleep(intervalMs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meanVal;
    }

    private int readFile(String path, int defaultValue) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    path));
            int i = Integer.parseInt(bufferedReader.readLine(), 10);
            bufferedReader.close();
            return i;
        } catch (Exception localException) {
        }
        return defaultValue;
    }

    private void showToast(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(StandByCurrentActivity.this, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}