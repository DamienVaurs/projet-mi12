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
    //private List<Integer> rsiList2 = new ArrayList<>();
    HashMap<String, List<Integer> > capitalCities = new HashMap<String, List<Integer>>();
    HashMap<String, Integer>[][] tableau = new HashMap[8][14];

    private Ble mBle;

    @SuppressLint("MissingPermission")
    public double scanLeDevice(final boolean enable, String address, Point point) throws InterruptedException {

        BluetoothAdapter.LeScanCallback leScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, final int rssi,
                                         byte[] scanRecord) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //System.out.println("ENTREE");
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
        //rsiList2.clear();
        capitalCities.clear();
        if (enable) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
//                    MainActivity.mScanning = false;
                    MainActivity.mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    System.out.println("RSILIST: " + rsiList1) ;
//                    System.out.println("RSILIST: " + rsiList2) ;
                    System.out.println("C5:32:52:D1:10:02 RSSILIST: " + capitalCities.get("C5:32:52:D1:10:02").toString());
                    rsiAverage = getRsiAverage(address);
                    System.out.println("C5:32:52:D1:10:02 Average: " + rsiAverage);
                    saveRssi("C5:32:52:D1:10:02");

                    System.out.println("tableau[0][0]: " + tableau[0][0]);

                    //Measurement measurement = new Measurement(point, rsiAverage);
                    //measurements.add(measurement);



                }


            },SCAN_PERIOD);
            MainActivity.mScanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
            //Thread.sleep(11000);
            return Double.parseDouble(String.valueOf(rsiAverage));
        }else {
            MainActivity.mScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
            System.out.println("ELSE");
            return 0;
        }
    }



    public void saveRssi(String address) {
        getRsiAverage(address);
        // pour tester HashMap tableau
        HashMap<String, Integer> test = new HashMap<>();
        test.put("C5:32:52:D1:10:02", rsiAverage);
        tableau[0][0] = test;
    }

    public double getRssiMoyen() {
        return rsiAverage;
    }

    public int getRsiAverage(String address) {
        int sum = 0;
        int num = 1;
        int avrage = 0;
        List<Integer> rssi = capitalCities.get(address);
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
        return avrage;
    }

    public void makeBleInstance(){
        mBle = new Ble();
        mBle.setBleListener(new BleListener(){
            @Override
            public void block() {
                System.out.println("a car is in the Parking space!");
                System.out.println("Now turn on the camera to shoot!");
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
