package com.inmoglass.factorytools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.TestItem;

import java.util.List;

/**
 * 测试项目listView适配器
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class TestItemAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final List<TestItem> mTestData;
    private final Context mContext;
    private OnItemClickListener mListener;

    public TestItemAdapter(Context context, List<TestItem> testData) {
        mInflater = LayoutInflater.from(context);
        mTestData = testData;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTestData.size();
    }

    @Override
    public Object getItem(int position) {
        return mTestData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = mInflater.inflate(R.layout.item_test, parent, false);
        holder = new ViewHolder();
        holder.nameTv = convertView.findViewById(R.id.tv_name);
        holder.resultTv = convertView.findViewById(R.id.tv_test_result);
        holder.checkBox = convertView.findViewById(R.id.checkbox);
        holder.containerRl = convertView.findViewById(R.id.rl_container);
        int unknown = mContext.getColor(R.color.white);
        int fail = mContext.getColor(R.color.red);
        int pass = mContext.getColor(R.color.green);
        TestItem item = mTestData.get(position);
        holder.nameTv.setText(item.getTitle());
        if (item.getState() == TestItem.State.UNKNOWN) {
            holder.resultTv.setTextColor(unknown);
        } else if (item.getState() == TestItem.State.FAIL) {
            holder.resultTv.setTextColor(fail);
            holder.resultTv.setText(mContext.getString(R.string.fail));
        } else if (item.getState() == TestItem.State.PASS) {
            holder.resultTv.setTextColor(pass);
            holder.resultTv.setText(mContext.getString(R.string.pass));
        }
        holder.containerRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClick(view, position);
                }
            }
        });
        return convertView;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    private static class ViewHolder {
        TextView nameTv;
        TextView resultTv;
        CheckBox checkBox;
        RelativeLayout containerRl;
    }

}