package com.inmoglass.factorytools.systemtest;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

public class SystemVersionTestActivity extends AbstractTestActivity {

    private TextView mDeviceNameTv;
    private TextView mAndroidVersionTv;
    private TextView mSystemVersionTv;
    private TextView mSystemTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.system_test);
        setContentView(R.layout.activity_system_version_test);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String deviceName = android.os.Build.DEVICE;
        String androidVersion = Build.VERSION.RELEASE;
        String systemVersion = Build.DISPLAY;
        String currentTime = TimeUtils.getNowString();

        mDeviceNameTv.setText(TextUtils.isEmpty(deviceName) ? getString(R.string.unknown) : deviceName);
        mAndroidVersionTv.setText(TextUtils.isEmpty(androidVersion) ? getString(R.string.unknown) : androidVersion);
        mSystemVersionTv.setText(TextUtils.isEmpty(systemVersion) ? getString(R.string.unknown) : systemVersion);
        mSystemTimeTv.setText(TextUtils.isEmpty(currentTime) ? getString(R.string.unknown) : currentTime);

        if (mIsAutoTest) {
            if (!TextUtils.isEmpty(deviceName) && !TextUtils.isEmpty(androidVersion) && !TextUtils.isEmpty(systemVersion) && !TextUtils.isEmpty(currentTime)) {
                mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
            } else {
                mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
            }
        }
    }

    private void initViews() {
        mDeviceNameTv = findViewById(R.id.tv_device_name);
        mAndroidVersionTv = findViewById(R.id.tv_android_version);
        mSystemVersionTv = findViewById(R.id.system_version);
        mSystemTimeTv = findViewById(R.id.tv_system_time);
    }
}
