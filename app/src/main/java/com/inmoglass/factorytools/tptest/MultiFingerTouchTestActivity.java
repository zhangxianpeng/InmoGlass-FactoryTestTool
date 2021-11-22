package com.inmoglass.factorytools.tptest;

import android.os.Bundle;
import android.view.View;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.view.MultiTouchView;

public class MultiFingerTouchTestActivity extends AbstractTestActivity implements MultiTouchView.CallBack {

    private MultiTouchView mMultiTouchView;
    private boolean mIsPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.multi_finger_touch_test_title);
        setContentView(R.layout.layout_multi_finger_touch_test);
        mMultiTouchView = (MultiTouchView) findViewById(R.id.multi_touch_view);
        mMultiTouchView.setCallBack(this);
        mIsPass = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTestCompleted() {
        mIsPass = true;
        if (mIsAutoTest) {
            mHandler.sendEmptyMessage(MSG_PASS);
        } else {
            getActionBar().show();
            mBottomButtonContainer.setVisibility(View.VISIBLE);
        }
    }
}
