package com.inmoglass.factorytools.microtest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;

/**
 * 麦克风测试项
 *
 * @author Administrator
 * @date 2021-11-16
 */
public class MicroTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.updateMicroTestList();
        setTestList(mApplication.getMicroTestList());
    }
}
