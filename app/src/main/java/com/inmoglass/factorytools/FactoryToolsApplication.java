package com.inmoglass.factorytools;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * （１）在应用创建时，初始化所有测试项。
 * （２）
 */
public class FactoryToolsApplication extends Application {

    private Resources mResources;
    private FactoryToolsDatabase mFactoryToolsDatabase;

    /**
     * 测试列表
     */
    private ArrayList<TestItem> mTestResultList = new ArrayList<TestItem>();
    /**
     * 自动测试项
     */
    private ArrayList<TestItem> mAutoTestList = new ArrayList<TestItem>();
    /**
     * 系统测试项
     */
    private ArrayList<TestItem> mSystemTestList = new ArrayList<TestItem>();
    /**
     * 电池测试项
     */
    private ArrayList<TestItem> mBatteryTestList = new ArrayList<TestItem>();
    /**
     * 屏幕测试项
     */
    private ArrayList<TestItem> mScreenTestList = new ArrayList<TestItem>();
    /**
     * 铃声测试项
     */
    private ArrayList<TestItem> mRingTestList = new ArrayList<TestItem>();
    /**
     * 电话测试项
     */
    private ArrayList<TestItem> mTelephonyTestList = new ArrayList<TestItem>();
    /**
     * 附件测试项
     */
    private ArrayList<TestItem> mAnnexTestList = new ArrayList<TestItem>();
    /**
     * 无线测试项
     */
    private ArrayList<TestItem> mWirelessTestList = new ArrayList<TestItem>();
    /**
     * 传感器测试项
     */
    private ArrayList<TestItem> mSensorTestList = new ArrayList<TestItem>();
    /**
     * 摄像头测试项
     */
    private ArrayList<TestItem> mCameraTestList = new ArrayList<TestItem>();
    /**
     * 按键测试项
     */
    private ArrayList<TestItem> mKeyTestList = new ArrayList<TestItem>();
    /**
     * 麦克风测试项
     */
    private ArrayList<TestItem> mMicroTestList = new ArrayList<TestItem>();
    /**
     * USB测试项
     */
    private ArrayList<TestItem> mUsbTestList = new ArrayList<TestItem>();

    private int mTestPosition = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mResources = getResources();
        mFactoryToolsDatabase = FactoryToolsDatabase.getInstance(this);
        updateValues();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 语言改变时，更新各测试项的测试标题名称
        updateValues();
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 更新所有测试项信息。
     * 首先更新主界面测试列表信息。
     * 其次更新所有测试项信息。
     * 最后更新自动测试项列表信息，因为该信息是从各测试项中直接获取的。
     */
    public void updateValues() {
        updateTestList();
        updateSystemTestList();
        updateBatteryTestList();
        updateScreenTestList();
        updateRingTestList();
        updateSensorTestList();
        updateWirelessTestList();
        updateCameraTestList();
        updateMicroTestList();
        updateKeyTestList();
        updateUsbTestList();
        updateTelephonyTestList();
        updateAnnexTestList();
        updateAutoTestList();
    }

    /**
     * 更新主界面测试列表信息。
     */
    public void updateTestList() {
        mTestResultList.clear();
        ArrayList<FactoryToolsDatabase.ItemState> states = mFactoryToolsDatabase.getAllTestState();
        String[] titles = mResources.getStringArray(R.array.test_item_title);
        String[] classes = mResources.getStringArray(R.array.test_item_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], getTestState(i, states));
                mTestResultList.add(info);
            }
        }
    }

    /**
     * 更新系统测试组测试列表信息。
     */
    public void updateSystemTestList() {
        mSystemTestList.clear();
        String[] titles = mResources.getStringArray(R.array.system_test_title);
        String[] classes = mResources.getStringArray(R.array.system_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mSystemTestList.add(info);
            }
        }
    }

    /**
     * 更新电池测试组测试列表信息。
     */
    public void updateBatteryTestList() {
        mBatteryTestList.clear();
        String[] titles = mResources.getStringArray(R.array.battery_test_title);
        String[] classes = mResources.getStringArray(R.array.battery_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mBatteryTestList.add(info);
            }
        }
    }

    /**
     * 更新屏幕测试组测试列表信息。
     */
    public void updateScreenTestList() {
        mScreenTestList.clear();
        String[] titles = mResources.getStringArray(R.array.screen_test_title);
        String[] classes = mResources.getStringArray(R.array.screen_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mScreenTestList.add(info);
            }
        }
    }

    /**
     * 更新铃声测试组测试列表信息。
     */
    public void updateRingTestList() {
        mRingTestList.clear();
        String[] titles = mResources.getStringArray(R.array.ring_test_title);
        String[] classes = mResources.getStringArray(R.array.ring_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mRingTestList.add(info);
            }
        }
    }

    /**
     * 更新电话测试组测试列表信息。
     */
    public void updateTelephonyTestList() {
        mTelephonyTestList.clear();
        String[] titles = mResources.getStringArray(R.array.telephony_test_title);
        String[] classes = mResources.getStringArray(R.array.telephony_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mTelephonyTestList.add(info);
            }
        }
    }

    /**
     * 更新附件测试组测试列表信息。
     */
    public void updateAnnexTestList() {
        mAnnexTestList.clear();
        String[] titles = mResources.getStringArray(R.array.annex_test_title);
        String[] classes = mResources.getStringArray(R.array.annex_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mAnnexTestList.add(info);
            }
        }
    }

    /**
     * 更新无线测试组测试列表信息。
     */
    public void updateWirelessTestList() {
        mWirelessTestList.clear();
        String[] titles = mResources.getStringArray(R.array.wireless_test_title);
        String[] classes = mResources.getStringArray(R.array.wireless_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mWirelessTestList.add(info);
            }
        }
    }

    /**
     * 更新传感器测试组测试列表信息。
     */
    public void updateSensorTestList() {
        mSensorTestList.clear();
        String[] titles = mResources.getStringArray(R.array.sensor_test_title);
        String[] classes = mResources.getStringArray(R.array.sensor_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mSensorTestList.add(info);
            }
        }
    }

    /**
     * 更新相机测试组测试列表信息。
     */
    public void updateCameraTestList() {
        mCameraTestList.clear();
        String[] titles = mResources.getStringArray(R.array.camera_test_title);
        String[] classes = mResources.getStringArray(R.array.camera_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mCameraTestList.add(info);
            }
        }
    }

    /**
     * 更新按键测试组测试列表信息。
     */
    public void updateKeyTestList() {
        mKeyTestList.clear();
        String[] titles = mResources.getStringArray(R.array.key_test_title);
        String[] classes = mResources.getStringArray(R.array.key_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mKeyTestList.add(info);
            }
        }
    }

    /**
     * 更新麦克风测试组测试列表信息。
     */
    public void updateMicroTestList() {
        mMicroTestList.clear();
        String[] titles = mResources.getStringArray(R.array.micro_test_title);
        String[] classes = mResources.getStringArray(R.array.micro_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mMicroTestList.add(info);
            }
        }
    }

    /**
     * 更新USB接口测试组测试列表信息。
     */
    public void updateUsbTestList() {
        mUsbTestList.clear();
        String[] titles = mResources.getStringArray(R.array.usb_test_title);
        String[] classes = mResources.getStringArray(R.array.usb_test_class);
        int length = (titles.length <= classes.length ? titles.length : classes.length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                TestItem info = new TestItem(titles[i], getPackageName(), classes[i], mFactoryToolsDatabase.getTestState(classes[i]));
                mUsbTestList.add(info);
            }
        }
    }

    /**
     * 更新自动测试列表信息。
     */
    public void updateAutoTestList() {
        mAutoTestList.clear();
        if (mSystemTestList.size() > 0) {
            for (int i = 0; i < mSystemTestList.size(); i++) {
                mAutoTestList.add(mSystemTestList.get(i));
            }
        }

        if (mBatteryTestList.size() > 0) {
            for (int i = 0; i < mBatteryTestList.size(); i++) {
                mAutoTestList.add(mBatteryTestList.get(i));
            }
        }

        if (mScreenTestList.size() > 0) {
            for (int i = 0; i < mScreenTestList.size(); i++) {
                mAutoTestList.add(mScreenTestList.get(i));
            }
        }

        if (mRingTestList.size() > 0) {
            for (int i = 0; i < mRingTestList.size(); i++) {
                mAutoTestList.add(mRingTestList.get(i));
            }
        }

        if (mSensorTestList.size() > 0) {
            for (int i = 0; i < mSensorTestList.size(); i++) {
                mAutoTestList.add(mSensorTestList.get(i));
            }
        }

        if (mWirelessTestList.size() > 0) {
            for (int i = 0; i < mWirelessTestList.size(); i++) {
                mAutoTestList.add(mWirelessTestList.get(i));
            }
        }

        if (mCameraTestList.size() > 0) {
            for (int i = 0; i < mCameraTestList.size(); i++) {
                mAutoTestList.add(mCameraTestList.get(i));
            }
        }

        if (mMicroTestList.size() > 0) {
            for (int i = 0; i < mMicroTestList.size(); i++) {
                mAutoTestList.add(mMicroTestList.get(i));
            }
        }

        if (mKeyTestList.size() > 0) {
            for (int i = 0; i < mKeyTestList.size(); i++) {
                mAutoTestList.add(mKeyTestList.get(i));
            }
        }

        if (mUsbTestList.size() > 0) {
            for (int i = 0; i < mUsbTestList.size(); i++) {
                mAutoTestList.add(mUsbTestList.get(i));
            }
        }

        if (mTelephonyTestList.size() > 0) {
            for (int i = 0; i < mTelephonyTestList.size(); i++) {
                mAutoTestList.add(mTelephonyTestList.get(i));
            }
        }

        if (mAnnexTestList.size() > 0) {
            for (int i = 0; i < mAnnexTestList.size(); i++) {
                mAutoTestList.add(mAnnexTestList.get(i));
            }
        }
        Log.d(this, "updateAutoTestList=>size: " + mAutoTestList.size());
    }

    /**
     * 获取测试组的测试状态信息。
     *
     * @param index  测试组下标，从０开始
     * @param states 　所有测试项的测试状态数组
     * @return 如果测试组中的所有测试项都测试通过，则返回State.PASS；如果测试组中有一项或多项测试不通过，则返回State.FAIL；否则返回State.UNKNOWN。
     */
    public TestItem.State getTestState(int index, ArrayList<FactoryToolsDatabase.ItemState> states) {
        TestItem.State state = TestItem.State.UNKNOWN;
        String[] idNames = mResources.getStringArray(R.array.test_item_state_list);
        if (index < idNames.length) {
            int id = mResources.getIdentifier(idNames[index], "array", getPackageName());
            String[] testClasses = mResources.getStringArray(id);
            int passCount = 0;
            FactoryToolsDatabase.ItemState item = null;
            if (testClasses.length > 0) {
                for (int i = 0; i < testClasses.length; i++) {
                    for (int j = 0; j < states.size(); j++) {
                        item = states.get(j);
                        if (item.mClassName.equals(testClasses[i]))
                            if (item.mState == TestItem.State.FAIL) {
                                state = TestItem.State.FAIL;
                                break;
                            } else if (item.mState == TestItem.State.PASS) {
                                passCount++;
                                break;
                            }
                    }
                }
                if (passCount == testClasses.length) {
                    state = TestItem.State.PASS;
                }
            }
        }
        return state;
    }

    public ArrayList<TestItem> getTestList() {
        return mTestResultList;
    }

    public ArrayList<TestItem> getSystemTestList() {
        return mSystemTestList;
    }

    public ArrayList<TestItem> getBatteryTestList() {
        return mBatteryTestList;
    }

    public ArrayList<TestItem> getScreenTestList() {
        return mScreenTestList;
    }

    public ArrayList<TestItem> getRingTestList() {
        return mRingTestList;
    }

    public ArrayList<TestItem> getTelephonyTestList() {
        return mTelephonyTestList;
    }

    public ArrayList<TestItem> getAnnexTestList() {
        return mAnnexTestList;
    }

    public ArrayList<TestItem> getWirelessTestList() {
        return mWirelessTestList;
    }

    public ArrayList<TestItem> getSensorTestList() {
        return mSensorTestList;
    }

    public ArrayList<TestItem> getCameraTestList() {
        return mCameraTestList;
    }

    public ArrayList<TestItem> getUsbTestList() {
        return mUsbTestList;
    }

    public ArrayList<TestItem> getKeyTestList() {
        return mKeyTestList;
    }

    public ArrayList<TestItem> getMicroTestList() {
        return mMicroTestList;
    }

    public ArrayList<TestItem> getAutoTestList() {
        return mAutoTestList;
    }

    /**
     * 获取测试项的测试状态
     *
     * @param className 测试项的类名
     * @return 如果测试通过，返回State.PASS; 如果测试失败，则返回State.FAIL；否则返回State.UNKNOWN。
     */
    public TestItem.State getTestState(String className) {
        return mFactoryToolsDatabase.getTestState(className);
    }

    /**
     * 设置测试项的测试状态。
     *
     * @param index     测试项所在的下标
     * @param className 　测试项的类名
     * @param state     测试结果
     * @return 设置成功，返回true；否则返回false
     */
    public boolean setTestState(int index, String className, TestItem.State state) {
        long result = mFactoryToolsDatabase.setTestState(index, className, state);
        return result != -1;
    }

    /**
     * 查找测试项的下标
     *
     * @param className 　测试项的类名
     * @return 如果找到测试项，这返回该测试项的下标；否则返回-1
     */
    public int findTestIndex(String className) {
        int index = -1;
        if (mAutoTestList.size() <= 0) {
            updateAutoTestList();
        }
        TestItem info = null;
        for (int i = 0; i < mAutoTestList.size(); i++) {
            info = mAutoTestList.get(i);
            if (info.getClassName().equals(className)) {
                index = i + 1;
                break;
            }
        }
        return index;
    }

    /**
     * 重置自动测试下标
     */
    public void resetAutoTest() {
        mTestPosition = 0;
    }

    /**
     * 删除所有测试数据
     */
    public void clearAllData() {
        mFactoryToolsDatabase.clearAllData();
    }

    /**
     * 启动自动测试。
     */
    public void startAutoTest() {
        if (mAutoTestList.size() > 0) {
            TestItem info = mAutoTestList.get(mTestPosition);
            Intent intent = info.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Utils.EXTRA_AUTO_TEST, true);
            intent.putExtra(Utils.EXTRA_PARENT, this.getClass().getSimpleName());
            Log.d(this, "startAutoTest=>intent: " + intent);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.d(this, "startAutoTest=>error: ", e);
                Toast.makeText(this, R.string.not_found_test, Toast.LENGTH_SHORT).show();
            }
            mTestPosition++;
        }
    }

    /**
     * 自动测试中，测试下一个测试项。
     */
    public void startNextTest() {
        Log.d(this, "startAutoTest=>size: " + mAutoTestList.size() + " testPosition: " + mTestPosition);
        if (mAutoTestList.size() > 0) {
            if (mTestPosition < mAutoTestList.size()) {
                TestItem info = mAutoTestList.get(mTestPosition);
                Intent intent = info.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Utils.EXTRA_AUTO_TEST, true);
                intent.putExtra(Utils.EXTRA_PARENT, this.getClass().getSimpleName());
                Log.d(this, "startAutoTest=>intent: " + intent);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(this, "startAutoTest=>error: ", e);
                    Toast.makeText(this, R.string.not_found_test, Toast.LENGTH_SHORT).show();
                }
                mTestPosition++;
            } else {
                Intent intent = new Intent(this, TestReportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
