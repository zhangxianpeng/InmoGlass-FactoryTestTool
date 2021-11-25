package com.inmoglass.factorytools.systemtest;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 系统测试
 * 包括：设备名称、安卓版本、系统版本、固件版本、系统时间、flashId
 *
 * @author Administrator
 * @date 2021-11-25
 */
public class SystemVersionTestActivity extends AbstractTestActivity {

    private TextView mDeviceNameTv;
    private TextView mAndroidVersionTv;
    private TextView mSystemVersionTv;
    private TextView mSystemTimeTv;
    private TextView mFlashIdTv;

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
        String flashId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mDeviceNameTv.setText(TextUtils.isEmpty(deviceName) ? getString(R.string.unknown) : deviceName);
        mAndroidVersionTv.setText(TextUtils.isEmpty(androidVersion) ? getString(R.string.unknown) : androidVersion);
        mSystemVersionTv.setText(TextUtils.isEmpty(systemVersion) ? getString(R.string.unknown) : systemVersion);
        mSystemTimeTv.setText(TextUtils.isEmpty(currentTime) ? getString(R.string.unknown) : currentTime);
        mFlashIdTv.setText(TextUtils.isEmpty(flashId) ? getString(R.string.unknown) : flashId);
        if (mIsAutoTest) {
            if (!TextUtils.isEmpty(deviceName) && !TextUtils.isEmpty(androidVersion)
                    && !TextUtils.isEmpty(systemVersion) && !TextUtils.isEmpty(currentTime) && !TextUtils.isEmpty(flashId)) {
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
        mFlashIdTv = findViewById(R.id.tv_flash_id_name);
    }
}
