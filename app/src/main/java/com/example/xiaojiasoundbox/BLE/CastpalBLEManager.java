package com.example.xiaojiasoundbox.BLE;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class CastpalBLEManager {
    static public ArrayList<BluetoothDevice> mPairedDeviceList = new ArrayList<>();

    public CastpalBLEManager(){

    }

    static public void addPairedDevice(BluetoothDevice device){
        if(!mPairedDeviceList.contains(device)){
            mPairedDeviceList.add(device);
        }
    }

    static public int getPairedDeviceCount(){
        return mPairedDeviceList.size();
    }

    static public BluetoothDevice getPairedDevice(int position){
        return mPairedDeviceList.get(position);
    }
}
