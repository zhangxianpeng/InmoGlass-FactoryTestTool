package com.inmoglass.factorytools.usbtest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;
import com.inmoglass.factorytools.R;

public class USBTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.usb_test_title);
        mApplication.updateUsbTestList();
        setTestList(mApplication.getUsbTestList());
    }
}
