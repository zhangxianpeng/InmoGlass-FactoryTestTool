package com.inmoglass.factorytools;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import com.inmoglass.factorytools.adapter.TestItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestReportActivity extends Activity {

    private TestItemAdapter testItemAdapter;
    private ListView listView;
    private List<TestItem> results;
    private FactoryToolsApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.test_report);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_test_report);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        listView = findViewById(R.id.listview);
        initAdapter();
    }

    private void initAdapter() {
        results = new ArrayList<>();
        testItemAdapter = new TestItemAdapter(this, results);
        listView.setAdapter(testItemAdapter);
    }

    private void initValues() {
        mApplication = (FactoryToolsApplication) getApplication();
        mApplication.updateValues();
        results.clear();
        ArrayList<TestItem> testList = mApplication.getTestList();
        testList.remove(testList.size() - 1);
        results.addAll(testList);
        testItemAdapter.notifyDataSetChanged();
    }
}
