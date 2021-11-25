package com.inmoglass.factorytools.screentest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.util.SystemBrightnessUtil;

/**
 * 光机显示背光测试
 *
 * @author Administrator
 * @date 2021-11-22
 * 测试标准: 从最亮到最暗层级跳变
 */
public class BacklitTestActivity extends AbstractTestActivity {
    private static final String TAG = BacklitTestActivity.class.getSimpleName();
    PowerManager mPowerManager = null;
    TextView mContent;
    private static final int[] COLOR_ARRAY = new int[]{Color.WHITE, Color.BLACK};
    private int mIndex = 0, mCount = 0;
    private static final int TIMES = 5;
    private Handler mUiHandler = new Handler();
    private Runnable mRunnable;

    private static final int MAX_BRIGHTNESS = 255;
    private static final boolean TEST_SCREEN_LIGHT = true;

    private void setScreenLight(Activity context, int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }

    private void startScreenLight() {
        SystemBrightnessUtil.setBrightness(BacklitTestActivity.this, MAX_BRIGHTNESS >> mCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_backlit_test);
        setTitle(R.string.screen_test_lcd_back_light);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mRunnable = new Runnable() {
            public void run() {
                if (TEST_SCREEN_LIGHT) {
                    startScreenLight();
                    mCount++;
                } else {
                    mContent.setBackgroundColor(COLOR_ARRAY[mIndex]);
                    mIndex = 1 - mIndex;
                    mCount++;
                }
                setBackground();
            }
        };

        setBackground();
    }

    private void setBackground() {
        if (mCount > TIMES) {
            if (TEST_SCREEN_LIGHT) {
                setScreenLight(BacklitTestActivity.this, MAX_BRIGHTNESS / 2);
            }
            return;
        }
        mUiHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        SystemBrightnessUtil.setBrightness(BacklitTestActivity.this, 155);
        mUiHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
