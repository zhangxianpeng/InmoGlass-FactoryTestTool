package com.inmoglass.factorytools.wirelesstest;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.adapter.MyAdapter;
import com.inmoglass.factorytools.util.BlueToothUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙测试项
 *
 * @author Administrator
 * @date 2021-11-16
 * <p>
 * 测试标准：只要能搜索到蓝牙设备则为通过，反之则失败
 */
public class BluetoothTestActivity extends AbstractTestActivity {
    private static final String TAG = BluetoothTestActivity.class.getSimpleName();
    private ListView bleLv;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bluetooth_test);
        bleLv = findViewById(R.id.lv_ble);
        initAdapter();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myAdapter != null && myAdapter.getCount() > 0) {
                    mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
                }
            }
        }, 5000);
    }

    private List<BluetoothDevice> resultList;

    private void initAdapter() {
        resultList = new ArrayList<>();
        myAdapter = new MyAdapter(this, resultList);
        bleLv.setAdapter(myAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBleInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (singBroadcastReceiver != null) {
            unregisterReceiver(singBroadcastReceiver);
        }
    }

    private BroadcastReceiver singBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (myAdapter != null && device.getName() != null) {
                    myAdapter.add(device);
                }
            }
        }
    };

    /**
     * 获取附近范围的蓝牙信息
     */
    private void getBleInfo() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(singBroadcastReceiver, filter);
        BlueToothUtil mBlueToothUtil = new BlueToothUtil(this);
        mBlueToothUtil.startDiscovery();
    }
}
