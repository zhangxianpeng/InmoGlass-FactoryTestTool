package com.inmoglass.factorytools.wirelesstest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;
import com.inmoglass.factorytools.R;

public class WirelessTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.wireless_test);
        mApplication.updateWirelessTestList();
        setTestList(mApplication.getWirelessTestList());
    }
}
