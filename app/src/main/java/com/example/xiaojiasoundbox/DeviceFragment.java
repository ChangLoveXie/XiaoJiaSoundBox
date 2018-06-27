package com.example.xiaojiasoundbox;


import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xiaojiasoundbox.BLE.CastpalBLEConstants;
import com.example.xiaojiasoundbox.BLE.CastpalBLEManager;
import com.example.xiaojiasoundbox.BLE.DeviceControlActivity;
import com.example.xiaojiasoundbox.BLE.LeDeviceListAdapter;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {

    private static final String TAG = "DeviceFragment";
    private static final int REQUEST_SCANDEVICEACTIVITY = 1;
    private static final int REQUEST_DEVICECONTROLACTIVITY = 2;
    private View mView;
    private Button mButton;
    private ListView mListView;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    public DeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_device, container, false);

        mButton = (Button) mView.findViewById(R.id.button_adddevice);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfrimBeforeDetectActivity.class);
                startActivityForResult(intent, REQUEST_SCANDEVICEACTIVITY);
            }
        });

        mLeDeviceListAdapter = new LeDeviceListAdapter(inflater);
        mListView = (ListView) mView.findViewById(R.id.devicelist);
        mListView.setAdapter(mLeDeviceListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = (BluetoothDevice) mLeDeviceListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DeviceControlActivity.class);
                intent.putExtra(CastpalBLEConstants.DEVICE_NAME, device.getName());
                intent.putExtra(CastpalBLEConstants.DEVICE_ADDRESS, device.getAddress());
                startActivityForResult(intent, REQUEST_DEVICECONTROLACTIVITY);
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        for(BluetoothDevice device : CastpalBLEManager.mPairedDeviceList){
            mLeDeviceListAdapter.addDevice(device);
        }
        mLeDeviceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_SCANDEVICEACTIVITY == requestCode){
            if(resultCode == RESULT_OK){
                final String deviceName = data.getStringExtra(CastpalBLEConstants.DEVICE_NAME);
                final String deviceAddress = data.getStringExtra(CastpalBLEConstants.DEVICE_ADDRESS);

                Intent intent = new Intent(getActivity(), DeviceControlActivity.class);
                intent.putExtra(CastpalBLEConstants.DEVICE_NAME, deviceName);
                intent.putExtra(CastpalBLEConstants.DEVICE_ADDRESS, deviceAddress);
                startActivityForResult(intent, REQUEST_DEVICECONTROLACTIVITY);
            }
        }else if(REQUEST_DEVICECONTROLACTIVITY == requestCode){
            if(resultCode == RESULT_OK){

            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
