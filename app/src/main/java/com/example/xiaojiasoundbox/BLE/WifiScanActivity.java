package com.example.xiaojiasoundbox.BLE;

import com.example.xiaojiasoundbox.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class WifiScanActivity extends Activity {

    private Spinner mSpinner;
    private EditText mEditText;
    private Button mButton;

    private WifiManager mWifiManager;
    private List<ScanResult> mScanResult;
    private List<String> mSSIDList;
    private ArrayAdapter<String> mScanResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);
        mSpinner = findViewById(R.id.wifi_scan_spinner);
        mEditText = findViewById(R.id.wifi_scan_pwd);
        mButton = findViewById(R.id.wifi_scan_button);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiManager.startScan();
        mScanResult = mWifiManager.getScanResults();

        mSSIDList = new ArrayList<>();
        for(ScanResult scanResult : mScanResult){
            mSSIDList.add(scanResult.SSID);
        }
        mScanResultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSSIDList);
        mScanResultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mScanResultAdapter);

        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String myssid = (String)mSpinner.getSelectedItem();
                String mypwd =  mEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(CastpalBLEConstants.SSID, myssid);
                intent.putExtra(CastpalBLEConstants.PWD, mypwd);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
