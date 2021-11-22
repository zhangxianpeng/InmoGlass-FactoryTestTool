package com.inmoglass.factorytools.screentest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 光机显示背光测试
 *
 * @author Administrator
 * @date 2021-11-22
 * 
 */
public class BacklitTestActivity extends AbstractTestActivity {

    private static final int MSG_SWITCH_COLOR = 100;
    private View mBacklitTestView;

    private int[] mColors;
    private int mSwitchColorDelayed;
    private int mSwitchTimes;
    private int mTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_backlit_test);
        setTitle(R.string.screen_test_lcd_back_light);
        mColors = getResources().getIntArray(R.array.backlit_test_colors);
        mSwitchColorDelayed = getResources().getInteger(R.integer.backlit_test_switch_color_delayed);
        mSwitchTimes = getResources().getInteger(R.integer.backlit_test_switch_times);
        mTimes = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBacklitTestView = findViewById(R.id.backlit_test);
        mBacklitTestView.setBackgroundColor(mColors[0]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwitchHandler.sendEmptyMessageDelayed(MSG_SWITCH_COLOR, mSwitchColorDelayed);
    }

    private Handler mSwitchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SWITCH_COLOR:
                    if (mTimes < mSwitchTimes) {
                        mTimes++;
                        mBacklitTestView.setBackgroundColor(mColors[mTimes % mColors.length]);
                        mSwitchHandler.sendEmptyMessageDelayed(MSG_SWITCH_COLOR, mSwitchColorDelayed);
                    } else {
                        mBacklitTestView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    break;
            }
        }
    };
}
