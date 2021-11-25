package com.inmoglass.factorytools.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.inmoglass.factorytools.Log;
import com.inmoglass.factorytools.R;

public class LCDTestView extends View {

    private static final int MSG_HIDE_START_TIP = 100;
    private static final int MSG_SHOW_END_TIP = 101;

    private CallBack mCallBack;
    private Paint mPaint;

    private String mStartTip;
    private String mEndTip;
    private int[] mTestColors;
    private int mPosition;
    private int mHideStartTipDelayed;
    private int mShowEndTipDelayed;
    private boolean mIsAutoTest;
    private boolean mShowStartTip;
    private boolean mShowEndTip;

    public LCDTestView(Context context) {
        this(context, null);
    }

    public LCDTestView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LCDTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = getResources();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xff000000);
        mPaint.setTextSize(28);
        mStartTip = context.getString(R.string.screen_test_lcd_test_start);
        mEndTip = context.getString(R.string.screen_test_lcd_test_end);
        mTestColors = res.getIntArray(R.array.lcd_test_colors);
        mHideStartTipDelayed = res.getInteger(R.integer.lcd_test_hide_start_tip_delayed);
        mShowEndTipDelayed = res.getInteger(R.integer.lcd_test_show_end_tip_delayed);
        mPosition = 0;
        mIsAutoTest = false;
        mShowStartTip = true;
        mShowEndTip = false;
        setBackgroundColor(mTestColors[mPosition]);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_START_TIP, mHideStartTipDelayed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsAutoTest) {
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            float height = fm.descent - fm.ascent;
            if (mPosition == 0) {
                if (mShowStartTip) {
                    float width = mPaint.measureText(mStartTip);
                    canvas.drawText(mStartTip, (getWidth() - width) / 2, (getHeight() - height) / 2, mPaint);
                }
            } else if ((mPosition + 1) == mTestColors.length) {
                if (mShowEndTip) {
                    float width = mPaint.measureText(mEndTip);
                    canvas.drawText(mEndTip, (getWidth() - width) / 2, (getHeight() - height) / 2, mPaint);
                }
            }
        }
    }

    public void setNextTestColor() {
        Log.d(this, "setNextTestColor=>position: " + mPosition + " length: " + mTestColors.length);
        if (mPosition + 1 < mTestColors.length) {
            mPosition += 1;
            setBackgroundColor(mTestColors[mPosition]);
            postInvalidate();
            if (mPosition + 1 == mTestColors.length) {
                mHandler.sendEmptyMessageDelayed(MSG_SHOW_END_TIP, mShowEndTipDelayed);
                if (mCallBack != null) {
                    mCallBack.onTestFinish(true);
                }
            }
        }
    }

    public void setAutoTest(boolean autoTest) {
        mIsAutoTest = autoTest;
    }

    public void setCallBack(CallBack callback) {
        mCallBack = callback;
    }

    public interface CallBack {
        void onTestFinish(boolean result);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.d(this, "handleMessage=>what: " + msg.what);
            switch (msg.what) {
                case MSG_HIDE_START_TIP:
                    mShowStartTip = false;
                    break;

                case MSG_SHOW_END_TIP:
                    mShowEndTip = true;
                    break;
            }
            postInvalidate();
        }
    };

}