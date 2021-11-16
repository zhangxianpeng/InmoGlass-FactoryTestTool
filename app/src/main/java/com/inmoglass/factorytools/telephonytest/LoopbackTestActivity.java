package com.inmoglass.factorytools.telephonytest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

public class LoopbackTestActivity extends AbstractTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loopback_test);
    }
}
