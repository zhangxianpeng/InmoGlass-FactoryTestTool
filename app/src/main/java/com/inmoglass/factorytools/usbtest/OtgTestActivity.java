package com.inmoglass.factorytools.usbtest;

import static android.content.Intent.ACTION_MEDIA_MOUNTED;
import static android.content.Intent.ACTION_MEDIA_UNMOUNTED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

public class OtgTestActivity extends AbstractTestActivity {

    private static final String TAG = OtgTestActivity.class.getSimpleName();
    private TextView resultTv;

    public static final String ACTION_USB_DEVICE_ATTACHED ="android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DEVICE_DETACHED ="android.hardware.usb.action.USB_DEVICE_DETACHED";
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.otg_test);
        setContentView(R.layout.activity_otg_test);
        resultTv = findViewById(R.id.result_tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_MEDIA_MOUNTED)) {
                resultTv.setText("OTG设备已挂载");
            } else if (action.equals(ACTION_MEDIA_UNMOUNTED)) {
                resultTv.setText("OTG设备已卸载");
            }
        }
    };
}