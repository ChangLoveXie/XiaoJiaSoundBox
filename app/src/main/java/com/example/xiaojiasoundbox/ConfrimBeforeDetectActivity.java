package com.example.xiaojiasoundbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.xiaojiasoundbox.BLE.CastpalBLEConstants;
import com.example.xiaojiasoundbox.BLE.CastpalBLEManager;
import com.example.xiaojiasoundbox.BLE.ScanDeviceActivity;

public class ConfrimBeforeDetectActivity extends Activity {

    private static final int REQUEST_SCANDEVICEACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrim_before_detect);

        Button button = findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfrimBeforeDetectActivity.this, ScanDeviceActivity.class);
                startActivityForResult(intent, REQUEST_SCANDEVICEACTIVITY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SCANDEVICEACTIVITY)
        {
            if(resultCode == RESULT_OK)
            {
                Intent intent = new Intent();
                intent.putExtra(CastpalBLEConstants.DEVICE_NAME, data.getStringExtra(CastpalBLEConstants.DEVICE_NAME));
                intent.putExtra(CastpalBLEConstants.DEVICE_ADDRESS, data.getStringExtra(CastpalBLEConstants.DEVICE_ADDRESS));
                setResult(RESULT_OK, intent);
            }
            finish();
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
