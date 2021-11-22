package com.inmoglass.factorytools.screentest;

import android.os.Bundle;
import android.view.View;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.Log;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.view.LCDTestView;

/**
 * 光机显示测试
 *
 * @author Administrator
 * @date 2021-11-19
 * 测试标准： 纯色黑色、白色、绿色、蓝色、红色画面自动滚动，每个纯色画面显示3S判断测试。光机显示无漏光现象屏幕画面完整，无倾斜及断点画面判断通过
 */
public class LcdTestActivity extends AbstractTestActivity implements LCDTestView.CallBack {

    private LCDTestView mLcdTestView;
    private int mNextColorDelayed;
    private boolean mIsFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lcd_test);
        setTitle(R.string.screen_test_lcd);
        mIsFinish = false;
        mNextColorDelayed = getResources().getInteger(R.integer.lcd_test_next_color_delayed);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLcdTestView = findViewById(R.id.lcd_test_view);
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
        mHandler.postDelayed(mLcdTestRunable, mNextColorDelayed);
    }

    private View.OnClickListener mLcdTestViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLcdTestView.setNextTestColor();
        }
    };

    private final Runnable mLcdTestRunable = new Runnable() {
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

