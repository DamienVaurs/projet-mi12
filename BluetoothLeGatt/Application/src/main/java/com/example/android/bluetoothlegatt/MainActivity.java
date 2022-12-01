package com.example.android.bluetoothlegatt;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.afficher_infos);
        getActionBar().setTitle(R.string.title_devices);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
            Toast.makeText(this, R.string.turn_on_ble, Toast.LENGTH_SHORT).show();
        }

        //实例化扫描类
        DeviceScanActivity ScanActivity = new DeviceScanActivity();
        ScanActivity.bluetoothAdapter = bluetoothAdapter;
        //实例化Ble类
        ScanActivity.makeBleInstance();
        //启动扫描
        ScanActivity.scanLeDevice(true);
        //
    }
}