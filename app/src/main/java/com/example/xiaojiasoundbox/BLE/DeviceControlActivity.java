package com.example.xiaojiasoundbox.BLE;

import com.example.xiaojiasoundbox.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceControlActivity extends Activity {

    private final String TAG = "DeviceControlActivity";
    private final int REQUEST_WIFISCANACTIVITY = 1;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;

    private ProgressBar mProgressBar;
    private TextView mTextViewConnecting;
    private TextView mTextViewDevicename;
    private Button mButtonSetWifi;
    private Button mButtonSetVolume;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder)service).getService();
            if(!mBluetoothLeService.initialize()){
                Log.e(TAG, "Unable to initialize BluetoothService");
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)){
                Toast.makeText(DeviceControlActivity.this, "connected", Toast.LENGTH_SHORT).show();
                updateUI(true);
            }else if(BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)){
                updateUI(false);
            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){

            }else if(BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        mDeviceName = getIntent().getStringExtra(CastpalBLEConstants.DEVICE_NAME);
        mDeviceAddress = getIntent().getStringExtra(CastpalBLEConstants.DEVICE_ADDRESS);

        mProgressBar = findViewById(R.id.device_control_progressBar);
        mTextViewConnecting = findViewById(R.id.device_control_connecting);
        mTextViewDevicename = findViewById(R.id.device_control_devicename);
        if(mDeviceName != null){
            mTextViewDevicename.setText(mDeviceName);
        }else{
            mTextViewDevicename.setText(R.string.default_device_name);
        }
        mButtonSetWifi = findViewById(R.id.device_control_setwifi);
        mButtonSetVolume = findViewById(R.id.device_control_setvolumn);
        updateUI(false);
        mButtonSetWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceControlActivity.this, WifiScanActivity.class);
                startActivityForResult(intent, REQUEST_WIFISCANACTIVITY);
            }
        });

        Intent intentGATTService = new Intent(this, BluetoothLeService.class);
        bindService(intentGATTService, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBroadcastReceiver, makeGattUpdateIntentFilter());
        if(mBluetoothLeService != null){
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "connect result " + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void updateUI(boolean contected){
        mProgressBar = findViewById(R.id.device_control_progressBar);
        mTextViewConnecting = findViewById(R.id.device_control_connecting);
        mTextViewDevicename = findViewById(R.id.device_control_devicename);
        mButtonSetWifi = findViewById(R.id.device_control_setwifi);
        mButtonSetVolume = findViewById(R.id.device_control_setvolumn);

        if(contected == true){
            mProgressBar.setVisibility(View.GONE);
            mTextViewConnecting.setVisibility(View.GONE);
            mTextViewDevicename.setVisibility(View.VISIBLE);
            mButtonSetWifi.setVisibility(View.VISIBLE);
            mButtonSetVolume.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.VISIBLE);
            mTextViewConnecting.setVisibility(View.VISIBLE);
            mTextViewDevicename.setVisibility(View.GONE);
            mButtonSetWifi.setVisibility(View.GONE);
            mButtonSetVolume.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_WIFISCANACTIVITY == requestCode){
            if(RESULT_OK == resultCode){
                final String ssid = data.getStringExtra(CastpalBLEConstants.SSID);
                final String pwd = data.getStringExtra(CastpalBLEConstants.PWD);
                byte[] ch = new byte[85];
                ch[0] = 0x01;
                char[] str = "amlogicblewifisetup".toCharArray();

                for (int i = 0; i < 19; i++) {
                    ch[i + 1] = (byte) str[i];
                }

                for (int i = 0; i < 32; i++) {
                    if (i < ssid.length()) {
                        ch[i + 1 + 19] = (byte) ssid.charAt(i);
                    } else
                        ch[i + 1 + 19] = 0;
                }
                for (int i = 0; i < 32; i++) {
                    if (i < pwd.length()) {
                        ch[i + 1 + 19 + 32] = (byte) pwd.charAt(i);
                    } else
                        ch[i + 1 + 19 + 32] = 0;
                }
                ch[19 + 32 + 32 + 1] = 0x04;
                mBluetoothLeService.writeCharacteristic(SampleGattAttributes.BLE_UUID_BLE_SERVICE, SampleGattAttributes.BLE_UUID_BLE_SERVICE_WIFI_CHAR,ch );
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
