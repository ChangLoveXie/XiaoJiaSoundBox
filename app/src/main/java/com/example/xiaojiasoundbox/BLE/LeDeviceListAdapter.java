package com.example.xiaojiasoundbox.BLE;

import com.example.xiaojiasoundbox.R;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices;
    private LayoutInflater mLayoutInflater;

    public LeDeviceListAdapter(LayoutInflater inflater) {
        super();
        this.mLeDevices = new ArrayList<>();
        this.mLayoutInflater = inflater;
    }

    public void addDevice(BluetoothDevice device){
        if(!mLeDevices.contains(device)){
            mLeDevices.add(device);
        }
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mLeDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.bledevice_listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = convertView.findViewById(R.id.listitem_addrname);
            viewHolder.deviceAddress = convertView.findViewById(R.id.listitem_address);
            viewHolder.deviceStatus = convertView.findViewById(R.id.listitem_status);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device = mLeDevices.get(position);
        final String deviceName = device.getName();

        if(deviceName != null && deviceName.length() > 0){
            viewHolder.deviceName.setText(deviceName);
        }
        else {
            viewHolder.deviceName.setText(R.string.default_device_name);
        }
        viewHolder.deviceAddress.setText(device.getAddress());
//        viewHolder.deviceStatus.setText(R.string.disconnected);
        return convertView;
    }

    public class ViewHolder{
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceStatus;
    }
}
