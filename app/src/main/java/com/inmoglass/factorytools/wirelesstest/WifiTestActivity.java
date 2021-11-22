package com.inmoglass.factorytools.wirelesstest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

import java.util.List;

/**
 * WIFI测试项
 *
 * @author Administrator
 * @date 2021-11-16
 * 测试标准: 查找到给定的wifiap,并且RSSI>-69，系统自动判断通过
 */
public class WifiTestActivity extends AbstractTestActivity {
    private static final String TAG = WifiTestActivity.class.getSimpleName();
    private TextView wifiStatusTv;
    private TextView wifiNameTv;
    private TextView wifiStrengthTv;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wireless_test_wifi);
        setContentView(R.layout.activity_wifi_test);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(WifiTestActivity.this, "请先打开wifi开关", Toast.LENGTH_SHORT).show();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiver, filter);
        connectWifi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.i(TAG, "WIFI_STATE_DISABLED");
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        Log.i(TAG, "WIFI_STATE_DISABLING");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        Log.i(TAG, "WIFI_STATE_ENABLED");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        Log.i(TAG, "WIFI_STATE_ENABLING");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        Log.i(TAG, "WIFI_STATE_UNKNOWN");
                        break;
                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.i(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {
                    Log.i(TAG, "wifi DISCONNECTED");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {
                    Log.i(TAG, "wifi CONNECTED");
                    getWifiInfo();
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                    Log.i(TAG, "wifi CONNECTING");
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.i(TAG, "SCAN_RESULTS_AVAILABLE_ACTION");
            }
        }
    };

    private void connectWifi() {
        String wifiName = "inmoglass";
        String wifiPwd = "20210108";
        String ssid = "\"" + wifiName + "\"";
        String psd = "\"" + wifiPwd + "\"";

        //2、配置wifi信息
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        conf.preSharedKey = psd;

        // 3、链接wifi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        wifiManager.addNetwork(conf);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        if (list.isEmpty()) {
            LogUtils.e(TAG, "Empty list returned");
            return;
        }
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals(ssid)) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }

    private void initView() {
        wifiStatusTv = findViewById(R.id.tv_wifi_connected);
        wifiNameTv = findViewById(R.id.tv_wifi_name);
        wifiStrengthTv = findViewById(R.id.tv_wifi_strength);
    }

    private WifiManager mWifiManager;

    private void getWifiInfo() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        String connectedSsid = wifiInfo.getSSID();
        Log.d("wifi ssid = ", wifiInfo.getSSID());
        if (connectedSsid.contains("unknown")) {
            return;
        }
        if (!TextUtils.isEmpty(connectedSsid)) {
            wifiStatusTv.setText("已连接");
            wifiNameTv.setText(connectedSsid);
            int rssi = wifiInfo.getRssi();
            LogUtils.i(TAG, "connect wifiName = " + connectedSsid + ",Rssi = " + rssi);
            wifiStrengthTv.setText("" + rssi);
            if (rssi > -69) {  // 正常标准
                mHandler.sendEmptyMessageDelayed(MSG_PASS, mAutoTestDelayTime);
            } else {
                mHandler.sendEmptyMessageDelayed(MSG_FAIL, mAutoTestDelayTime);
            }
        }
    }
}
