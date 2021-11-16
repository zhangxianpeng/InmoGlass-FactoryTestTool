package com.inmoglass.factorytools.screentest;

import android.os.Bundle;
import android.view.View;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.Log;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.view.LCDTestView;

public class LcdTestActivity extends AbstractTestActivity implements LCDTestView.CallBack {

    private LCDTestView mLcdTestView;

    private int mNextColorDelayed;
    private boolean mIsFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lcd_test);
        setTitle(R.string.lcd_test_title);
        mIsFinish = false;
        mNextColorDelayed = getResources().getInteger(R.integer.lcd_test_next_color_delayed);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLcdTestView = (LCDTestView) findViewById(R.id.lcd_test_view);
        mLcdTestView.setCallBack(this);
        mLcdTestView.setAutoTest(mIsAutoTest);
        mLcdTestView.setOnClickListener(mLcdTestViewClickListener);
        mBottomButtonContainer.setVisibility(View.GONE);
    }

    @Override
    public void onTestFinish(boolean result) {
        Log.d(this, "onTestFinish=>result: " + result);
        mHandler.postDelayed(mShowBottomButtonRunnable, getResources().getInteger(R.integer.lcd_test_show_end_tip_delayed));
        mIsFinish = result;
        mHandler.removeCallbacks(mLcdTestRunable);
        if (mIsAutoTest) {
            if (result) {
                mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
            } else {
                mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsAutoTest) {
            mHandler.postDelayed(mLcdTestRunable, mNextColorDelayed);
        }
    }

    private View.OnClickListener mLcdTestViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLcdTestView.setNextTestColor();
        }
    };

    private Runnable mLcdTestRunable = new Runnable() {
        @Override
        public void run() {
            if (!mIsFinish) {
                mLcdTestView.setNextTestColor();
                mHandler.postDelayed(this, mNextColorDelayed);
            }
        }
    };

    private Runnable mShowBottomButtonRunnable = new Runnable() {
        @Override
        public void run() {
            mBottomButtonContainer.setVisibility(View.VISIBLE);
        }
    };
}

