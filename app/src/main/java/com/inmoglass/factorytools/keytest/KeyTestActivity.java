package com.inmoglass.factorytools.keytest;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 按键测试项
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class KeyTestActivity extends AbstractTestActivity {

    private TextView resultTv;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.key_test);
        setContentView(R.layout.activity_key_test);
        initViews();
    }

    private void initViews() {
        resultTv = findViewById(R.id.tv_result);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 26) {
            result = "您点击了电源键";
        } else if (keyCode == 289) {
            result = "您点击了万能键";
        }
        resultTv.setText(result);
        if (result.equals("")) {
            mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
        }
        return super.onKeyDown(keyCode, event);
    }
}
