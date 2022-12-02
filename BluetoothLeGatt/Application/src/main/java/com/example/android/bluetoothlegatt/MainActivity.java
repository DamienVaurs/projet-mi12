package com.example.android.bluetoothlegatt;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    public BluetoothAdapter bluetoothAdapter;
    protected boolean mScanning = false;
    private Handler handler;

//    protected int rsiAverage;

//    protected boolean block = false;
//    private static final long SCAN_PERIOD = 10000;

//    private List<Integer> rsiList = new ArrayList<>();

//    private DeviceScanActivity.Ble mBle;
//    private boolean mScanning;

    private DeviceScanActivity ScanActivity;
    private DeviceScanActivity ScanActivity2;


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
        ScanActivity = new DeviceScanActivity();
        ScanActivity.bluetoothAdapter = bluetoothAdapter;
        //实例化Ble类
        ScanActivity.makeBleInstance();
        //启动扫描
//        ScanActivity.scanLeDevice(true);
        //

        //实例化扫描类
        ScanActivity2 = new DeviceScanActivity();
        ScanActivity2.bluetoothAdapter = bluetoothAdapter;
        //实例化Ble类
        ScanActivity2.makeBleInstance();
        //启动扫描
//        ScanActivity.scanLeDevice(true);
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
//                mLeDeviceListAdapter.clear();
//                scanLeDevice(true);
                ScanActivity.scanLeDevice(true);
                ScanActivity2.scanLeDevice(true);

                break;
            case R.id.menu_stop:
//                scanLeDevice(false);
                ScanActivity.scanLeDevice(false);
                ScanActivity2.scanLeDevice(false);
                break;
        }
        return true;
    }

}