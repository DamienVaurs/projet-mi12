package com.example.mi12_tests;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class DeviceScanActivity extends ListActivity{
    public BluetoothAdapter bluetoothAdapter;
    public boolean mScanning;
    private Handler handler;

    protected int rsiAverage;

    protected boolean block = false;
    private static final long SCAN_PERIOD = 10000;

    private List<Integer> rsiList1 = new ArrayList<>();
    HashMap<String, List<Integer> > capitalCities = new HashMap<String, List<Integer>>();

    private Ble mBle;

    @SuppressLint("MissingPermission")
    public void scanLeDevice(final boolean enable, String address, Point point) throws InterruptedException {

        BluetoothAdapter.LeScanCallback leScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, final int rssi,
                                         byte[] scanRecord) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (device.getAddress().equals(address)) {
                                    rsiList1.add(rssi);
                                    capitalCities.put(address, rsiList1);
                                }
//                            }
                            }
                        });
                    }
                };

        block = false;
        rsiList1.clear();
        capitalCities.clear();

        if (enable) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    MainActivity.mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    if (rsiList1 == null) {
                        rsiList1.add(-100);
                        capitalCities.put(address, rsiList1);
                    }
                    //System.out.println("RSILIST: " + rsiList1) ;
                    rsiAverage = getRsiAverage(rsiList1);
                    //System.out.println("C5:32:52:D1:10:02 Average: " + rsiAverage);
                }


            },SCAN_PERIOD);
            MainActivity.mScanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        }else {
            MainActivity.mScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
            //System.out.println("ELSE");
        }
    }

    public double getRssiMoyen() {
        return rsiAverage;
    }

    public int getRsiAverage(List<Integer> rssiList) {
        int sum = 0;
        int num = 1;
        int avrage = 0;
        //List<Integer> rssi = capitalCities.get(address);
        List<Integer> rssi = rssiList;
        for (int j = 0; j < Objects.requireNonNull(rssi).size(); j++) {
            sum = sum + rssi.get(j);
        }
        num = rssi.size();
        if(rssi.size() == 0){
            num = 1;
        }
        avrage = sum/num;
        System.out.println("=====getRsiAverage=====");
        System.out.println("avrage: " + avrage);
        if (avrage == 0){
            avrage = -100;
        }
        return avrage;
    }

    public void makeBleInstance(){
        mBle = new Ble();
        mBle.setBleListener(new BleListener(){
            @Override
            public void block() {
            }
        });
    }

    //回调函数接口
    public interface BleListener{
        public void block();
    }

    public static class Ble{
        private BleListener mBleListener;

        public void setBleListener(BleListener mBleListener){
            this.mBleListener = mBleListener;
        }

        public void doBlock(){
            mBleListener.block();
        }
    }


}
