package com.inmoglass.factorytools.tptest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;
import com.inmoglass.factorytools.R;

public class TpTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.tp_test);
        mApplication.updateTpTestList();
        setTestList(mApplication.getTpTestList());
    }
}
