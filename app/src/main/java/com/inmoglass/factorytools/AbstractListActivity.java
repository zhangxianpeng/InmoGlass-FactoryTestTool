package com.inmoglass.factorytools;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 测试组列表的基类
 */
public abstract class AbstractListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    protected FactoryToolsApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        mApplication = (FactoryToolsApplication) getApplication();
        getListView().setEmptyView(getEmptyView());
        getListView().setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getTag() instanceof TestAdapter.ViewHolder) {
            TestAdapter.ViewHolder holder = (TestAdapter.ViewHolder) view.getTag();
            if (holder.mInfo != null) {
                Intent intent = holder.mInfo.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private View getEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.empty_view, null, false);
        return view;
    }

    /**
     * 设置测试项数组，如果测试数组只有一项，则直接启动该测试，而不再显示测试项列表
     * @param list　测试项数组 com.inmoglass.factorytools.cameratest.CameraTest
     */
    public void setTestList(ArrayList<TestItem> list) {
        if (list.size() == 1) {
            if (list.get(0) != null) {
                try {
                    Intent intent = list.get(0).getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, R.string.not_found_test, Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        } else {
            mApplication = (FactoryToolsApplication) getApplication();
            TestAdapter adapter = new TestAdapter(this, list);
            getListView().setAdapter(adapter);
        }
    }

    class TestAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private Resources mResources;
        private ArrayList<TestItem> mList;

        public TestAdapter(Context context, ArrayList<TestItem> list) {
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResources = context.getResources();
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TestItem info = mList.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder();
                holder.mInfo = info;
                holder.mTv = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTv.setText(info.getTitle());
            int testColor = mResources.getColor(R.color.white);
            if (info.getState() == TestItem.State.FAIL) {
                testColor = mResources.getColor(R.color.red);
            } else if (info.getState() == TestItem.State.PASS) {
                testColor = mResources.getColor(R.color.green);
            }
            holder.mTv.setTextColor(testColor);
            return convertView;
        }

        class ViewHolder {
            TestItem mInfo;
            TextView mTv;
        }
    }
}
