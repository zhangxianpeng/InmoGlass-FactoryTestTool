package com.inmoglass.factorytools.annextest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractListActivity;

public class AnnexTestList extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.updateAnnexTestList();
        setTestList(mApplication.getAnnexTestList());
    }
}
