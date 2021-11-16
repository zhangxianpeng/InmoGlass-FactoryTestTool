package com.inmoglass.factorytools.ringtest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;
import com.inmoglass.factorytools.R;

public class RingTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.ring_test_title);
        mApplication.updateRingTestList();
        setTestList(mApplication.getRingTestList());
    }
}
