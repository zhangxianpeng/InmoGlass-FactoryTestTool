package com.inmoglass.factorytools.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inmoglass.factorytools.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<BluetoothDevice> mDatas;

    public MyAdapter(Context context, List<BluetoothDevice> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    //返回数据集的长度
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview, parent, false); //加载布局
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.strengthTv = (TextView) convertView.findViewById(R.id.tv_strength);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice result = mDatas.get(position);
        holder.nameTv.setText(result.getName());
        return convertView;
    }

    public void add(BluetoothDevice device) {
        if (!isContained(device)) {
            mDatas.add(device);
            notifyDataSetChanged();
        }
    }

    private boolean isContained(BluetoothDevice device) {
        int length = mDatas.size();
        for (int i = 0; i < length; i++) {
            if (TextUtils.equals(mDatas.get(i).getAddress(), device.getAddress())) {
                return true;
            }
        }
        return false;
    }

    private class ViewHolder {
        TextView nameTv;
        TextView strengthTv;
    }

}