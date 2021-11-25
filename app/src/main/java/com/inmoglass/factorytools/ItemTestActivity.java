package com.inmoglass.factorytools;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.inmoglass.factorytools.adapter.TestItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 单板测试列表
 */
public class ItemTestActivity extends Activity {
    private static final String TAG = ItemTestActivity.class.getSimpleName();
    private Resources mResources;
    private FactoryToolsApplication mApplication;
    private ListView testListView;
    private List<TestItem> resultList;
    private TestItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle(R.string.item_test);
        setContentView(R.layout.activity_item_test);
        testListView = findViewById(R.id.lv_test);
        initAdapter();
        initValues();
    }

    private void initAdapter() {
        resultList = new ArrayList<>();
        adapter = new TestItemAdapter(this, resultList);
        testListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TestItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TestItem info = resultList.get(position);
                Log.d(ItemTestActivity.this, "onClick(test)=>class: " + info.getClassName());
                Intent intent = info.getIntent();
                intent.putExtra(Utils.EXTRA_AUTO_TEST, false);
                intent.putExtra(Utils.EXTRA_PARENT, ItemTestActivity.this.getClass().getSimpleName());
                if (intent != null) {
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(this, "(mTestClickListener)onClick=>start activity fail. intent: " + intent + " error: ", e);
                        Toast.makeText(ItemTestActivity.this, R.string.not_found_test, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        mApplication.updateValues();
        updateItemContainer();
    }

    private void initValues() {
        mResources = getResources();
        mApplication = (FactoryToolsApplication) getApplication();
    }

    private boolean allPass = false;

    private void updateItemContainer() {
        // 首先清除GirdLayout中的View，防止重复添加测试组View
        resultList.clear();
        // 获取测试列表信息
        ArrayList<TestItem> testList = mApplication.getTestList();
        for (TestItem item : testList) {
            if (item.getState() != TestItem.State.PASS) {
                setTitle(R.string.item_test);
                break;
            } else {
                setTitle(R.string.item_test_all_pass);
            }
        }
        resultList.addAll(testList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
//        int padding = mResources.getDimensionPixelSize(R.dimen.activity_vertical_margin);
//        int height = mResources.getDimensionPixelSize(R.dimen.item_height);
//        Drawable leftBackground = mResources.getDrawable(R.drawable.ic_left_item_background);
//        Drawable rightBackground = mResources.getDrawable(R.drawable.ic_right_item_background);
//        int unknown = mResources.getColor(R.color.white);
//        int fail = mResources.getColor(R.color.red);
//        int pass = mResources.getColor(R.color.green);
//        int maxWidth = mResources.getDisplayMetrics().widthPixels - (2 * padding);
//        Log.d(this, "updateItemContainer=>size: " + testList.size() + ", maxWidth: " + maxWidth);
//        if (testList.size() > 0) {
//            for (int i = 0; i < testList.size(); i++) {
//                // 获取测试组信息
//                TestItem info = testList.get(i);
//                // 创建测试组View
//                TextView itemTest = new TextView(this);
//                itemTest.setText(info.getTitle());
//                itemTest.setTypeface(itemTest.getTypeface(), Typeface.BOLD);
//                itemTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, mResources.getInteger(R.integer.item_test_text_size));
//                itemTest.setGravity(Gravity.CENTER);
//                // 通过测试组状态设置测试组View的颜色
//                if (info.getState() == TestItem.State.UNKNOWN) {
//                    itemTest.setTextColor(unknown);
//                } else if (info.getState() == TestItem.State.FAIL) {
//                    itemTest.setTextColor(fail);
//                } else if (info.getState() == TestItem.State.PASS) {
//                    itemTest.setTextColor(pass);
//                }
//                // 设置测试组View的背景图片
//                if (((i + 1) % 2) != 0) {
//                    itemTest.setBackground(leftBackground);
//                } else {
//                    itemTest.setBackground(rightBackground);
//                }
//                // 将测试组信息添加到View中，以便在点击View时获取测试组信息
//                itemTest.setTag(info);
//                itemTest.setOnClickListener(mTestClickListener);
//                // 设置布局属性
//                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.setGravity(Gravity.CENTER);
//                params.width = maxWidth / 2;
//                params.height = height;
//                // 将测试组View添加到GridLayout中
//                mItemTestContainer.addView(itemTest, i, params);
//            }
//        }
    }
}