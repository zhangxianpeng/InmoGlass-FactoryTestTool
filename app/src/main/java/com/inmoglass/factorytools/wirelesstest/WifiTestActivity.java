package com.inmoglass.factorytools.wirelesstest;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * WIFI测试项
 *
 * @author Administrator
 * @date 2021-11-16
 * 测试标准: 查找到给定的wifiap,并且RSSI>-69，系统自动判断通过
 */
public class WifiTestActivity extends AbstractTestActivity {

    private TextView wifiStatusTv;
    private TextView wifiNameTv;
    private TextView wifiStrengthTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wifi_test);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWifiInfo();
    }

    private void initView() {
        wifiStatusTv = findViewById(R.id.tv_wifi_connected);
        wifiNameTv = findViewById(R.id.tv_wifi_name);
        wifiStrengthTv = findViewById(R.id.tv_wifi_strength);
    }

    private WifiManager mWifiManager;

    private void getWifiInfo() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isAvailable = mWifiManager.isWifiEnabled();
        if (!isAvailable) {
            wifiStatusTv.setText("未连接");
            Toast.makeText(this, "请连接到指定热点后重新测试", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
        }

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        String connectedSsid = wifiInfo.getSSID();
        Log.d("wifi ssid = ", wifiInfo.getSSID());
        if (!TextUtils.isEmpty(connectedSsid) && !connectedSsid.contains("unknown")) {
            wifiStatusTv.setText("已连接");
            wifiNameTv.setText(connectedSsid);
            int rssi = wifiInfo.getRssi();
            wifiStrengthTv.setText("" + rssi);
            if (rssi > -69) {  // 正常标准
                mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
            } else {
                mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
            }
        } else {
            // 没有连接上的wifi
            wifiStatusTv.setText("未连接");
            Toast.makeText(this, "请连接到指定热点后重新测试", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
        }
    }

}
