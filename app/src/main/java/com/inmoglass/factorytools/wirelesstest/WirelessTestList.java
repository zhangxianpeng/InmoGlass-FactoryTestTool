package com.inmoglass.factorytools.wirelesstest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;

public class WirelessTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.updateWirelessTestList();
        setTestList(mApplication.getWirelessTestList());
    }
}
