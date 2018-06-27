package com.example.xiaojiasoundbox.BLE;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xiaojiasoundbox.HelloActivity;
import com.example.xiaojiasoundbox.R;

import java.util.UUID;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.widget.Toast.LENGTH_SHORT;

public class ScanDeviceActivity extends Activity {

    private final static int REQUEST_PERMISSION_BT = 1;
    private final static int REQUEST_ENABLE_BT = 2;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private ListView mListView;

    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);

        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= 23)
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_BT);
            }

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(getLayoutInflater());
        mListView = findViewById(R.id.scan_device_list);
        mListView.setAdapter(mLeDeviceListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = (BluetoothDevice)mLeDeviceListAdapter.getItem(position);

                mBluetoothAdapter.stopLeScan(mleScanCallback);

                Intent intent = new Intent();
                intent.putExtra(CastpalBLEConstants.DEVICE_NAME, device.getName());
                intent.putExtra(CastpalBLEConstants.DEVICE_ADDRESS, device.getAddress());
                setResult(RESULT_OK, intent);

                finish();
            }
        });

//        mBluetoothAdapter.startLeScan(mleScanCallback);
        UUID[] serviceUuids = {SampleGattAttributes.BLE_UUID_BLE_SERVICE};
        mBluetoothAdapter.startLeScan(serviceUuids, mleScanCallback);
    }

    public BluetoothAdapter.LeScanCallback mleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
