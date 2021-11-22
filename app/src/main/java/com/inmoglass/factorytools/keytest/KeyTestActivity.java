package com.inmoglass.factorytools.keytest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 按键测试项
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class KeyTestActivity extends AbstractTestActivity {

    private boolean mIsPass;
    private Button universalBtn;
    private Button otherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.key_test);
        setContentView(R.layout.layout_key_test);
        initViews();
    }

    private void initViews() {
        universalBtn = findViewById(R.id.btn_wanneng);
        otherBtn = findViewById(R.id.btn_other);
        
        universalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2021/11/22 万能按键 
            }
        });
        
        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2021/11/22 其他功能 
            }
        });
    }


}
