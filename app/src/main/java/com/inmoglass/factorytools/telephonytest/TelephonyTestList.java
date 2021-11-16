package com.inmoglass.factorytools.telephonytest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;

public class TelephonyTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.updateTelephonyTestList();
        setTestList(mApplication.getTelephonyTestList());
    }
}
