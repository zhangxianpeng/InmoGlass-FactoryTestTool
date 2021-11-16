package com.inmoglass.factorytools.usbtest;

import android.os.Bundle;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

public class OtgTestActivity extends AbstractTestActivity {

    private static final String TAG = OtgTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.otg_test);
        setContentView(R.layout.activity_otg_test);
    }
}