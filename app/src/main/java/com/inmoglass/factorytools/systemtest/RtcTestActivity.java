package com.inmoglass.factorytools.systemtest;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RtcTestActivity extends AbstractTestActivity {

    private TextView mSystemTime;

    private SimpleDateFormat mDateFormat;
    private String mCurrentDate;
    private int mDefaultTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rtc_test);
        setTitle(R.string.rtc_test_title);
        mDateFormat = new SimpleDateFormat("hh:mm:ss");
        mSystemTime = (TextView) findViewById(R.id.system_time);
        mDefaultTextColor = mSystemTime.getCurrentTextColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentDate = mDateFormat.format(Calendar.getInstance().getTime());
        mHandler.post(mTimeUpdateRunnable);
        if (mIsAutoTest) {
            if (!TextUtils.isEmpty(mCurrentDate)) {
                mHandler.postDelayed(mAutoTestRunnable, mAutoTestDelayTime);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTimeUpdateRunnable);
        mHandler.removeCallbacks(mAutoTestRunnable);
    }

    private Runnable mTimeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            mCurrentDate = mDateFormat.format(Calendar.getInstance().getTime());
            if (!TextUtils.isEmpty(mCurrentDate)) {
                mSystemTime.setTextColor(RtcTestActivity.this.getColor(R.color.green));
                mSystemTime.setText(mCurrentDate);
            } else {
                mSystemTime.setTextColor(mDefaultTextColor);
                mSystemTime.setText(R.string.unknown);
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    private Runnable mAutoTestRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mTimeUpdateRunnable);
            mHandler.sendEmptyMessage(MSG_PASS);
        }
    };
}
