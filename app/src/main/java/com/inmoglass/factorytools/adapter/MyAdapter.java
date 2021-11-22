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

/**
 * 蓝牙列表适配器
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class MyAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final List<BluetoothDevice> mBlueToothDevicesData;

    public MyAdapter(Context context, List<BluetoothDevice> blueToothDevicesData) {
        mInflater = LayoutInflater.from(context);
        mBlueToothDevicesData = blueToothDevicesData;
    }

    @Override
    public int getCount() {
        return mBlueToothDevicesData.size();
    }

    @Override
    public Object getItem(int position) {
        return mBlueToothDevicesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder();
            holder.nameTv = convertView.findViewById(R.id.tv_name);
            holder.strengthTv = convertView.findViewById(R.id.tv_strength);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice result = mBlueToothDevicesData.get(position);
        holder.nameTv.setText(result.getName());
        return convertView;
    }

    public void add(BluetoothDevice device) {
        if (!isContained(device)) {
            mBlueToothDevicesData.add(device);
            notifyDataSetChanged();
        }
    }

    private boolean isContained(BluetoothDevice device) {
        int length = mBlueToothDevicesData.size();
        for (int i = 0; i < length; i++) {
            if (TextUtils.equals(mBlueToothDevicesData.get(i).getAddress(), device.getAddress())) {
                return true;
            }
        }
        return false;
    }

    private static class ViewHolder {
        TextView nameTv;
        TextView strengthTv;
    }

}