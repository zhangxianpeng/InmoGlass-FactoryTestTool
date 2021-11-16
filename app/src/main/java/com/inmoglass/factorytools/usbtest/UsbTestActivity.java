package com.inmoglass.factorytools.usbtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

public class UsbTestActivity extends AbstractTestActivity {

    // TODO: 2021/11/16 PASS FAIL的回调事件
    private static final String TAG = UsbTestActivity.class.getSimpleName();

    BroadcastReceiver mUsbStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive = " + intent.getAction());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        filter.addAction("android.hardware.usb.action.USB_STATE");

        registerReceiver(mUsbStateChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mUsbStateChangeReceiver!=null) {
            unregisterReceiver(mUsbStateChangeReceiver);
        }
    }
}