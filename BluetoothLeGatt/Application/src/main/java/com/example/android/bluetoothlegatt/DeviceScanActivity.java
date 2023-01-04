package com.example.android.bluetoothlegatt;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DeviceScanActivity extends ListActivity{
    public BluetoothAdapter bluetoothAdapter;
    public boolean mScanning;
    private Handler handler;

    protected int rsiAverage;

    protected boolean block = false;
    private static final long SCAN_PERIOD = 10000;

    private List<Integer> rsiList1 = new ArrayList<>();
    private List<Integer> rsiList2 = new ArrayList<>();
    HashMap<String, List<Integer> > capitalCities = new HashMap<String, List<Integer>>();
    HashMap<String, Integer>[][] tableau = new HashMap[8][14];

    private Ble mBle;

    public void scanLeDevice(final boolean enable){
        block = false;
        rsiList1.clear();
        rsiList2.clear();
        capitalCities.clear();
        if (enable) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    MainActivity.mScanning = false;
                    MainActivity.mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    //rsiAverage = getRsiAverage();
                    System.out.println("RSILIST: " + rsiList1) ;
//                    System.out.println("RSILIST: " + rsiList2) ;
                    System.out.println("C5:32:52:D1:10:02 RSSILIST: " + capitalCities.get("C5:32:52:D1:10:02").toString());
                    rsiAverage = getRsiAverage("C5:32:52:D1:10:02");
                    System.out.println("C5:32:52:D1:10:02 Average: " + rsiAverage);
                    saveRssi("C5:32:52:D1:10:02");



//                    System.out.println("E8:69:A8:6A:24:02 RSSILIST: " + capitalCities.get("E8:69:A8:6A:24:02").toString());
//                    rsiAverage = getRsiAverage("E8:69:A8:6A:24:02");
//                    test.put("E8:69:A8:6A:24:02", rsiAverage);

                    System.out.println("tableau[0][0]: " + tableau[0][0]);
//                    System.out.println(rsiAverage);

                    /*
                    if (rsiAverage<-100){
                        mBle.doBlock();
                    }else if (rsiAverage==0){
//                        System.out.println("=====There is no ble device,please check=====");
                        System.out.println("=====scanLeDevice()=====");
                        scanLeDevice(true);
                    }else {
                        System.out.println("=====scanLeDevice()=====");
                        scanLeDevice(true);
                    }*/
//                    invalidateOptionsMenu();
                }
            },SCAN_PERIOD);
            MainActivity.mScanning = true;
//            mScanning = true;
//            invalidateOptionsMenu();
            bluetoothAdapter.startLeScan(leScanCallback);
        }else {
            MainActivity.mScanning = false;
//            mScanning = false;
//            invalidateOptionsMenu();
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
//        invalidateOptionsMenu();
    }


    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (device.getAddress().equals("C5:32:52:D1:10:02")) {
//                                System.out.println("device name: " + device.getName() + ",   device address: " + device.getAddress() + ", rssi: " + rssi);
//                                rsiList.clear();
                                rsiList1.add(rssi);
                                capitalCities.put("C5:32:52:D1:10:02", rsiList1);
//                                System.out.println("RSILIST1: " + rsiList1);
                            }
//                            if (device.getAddress().equals("E8:69:A8:6A:24:02")){
//                                System.out.println("device name: " + device.getName() + ",   device address: " + device.getAddress() + ", rssi: " + rssi);
//                                rsiList.clear();
//                                rsiList2.add(rssi);
//                                capitalCities.put("E8:69:A8:6A:24:02", rsiList2);
//                                System.out.println("RSILIST2: " + rsiList2) ;
//                            }
                        }
                    });
//                    int average1 = getRsiAverage(rsiList1);
//                    System.out.println("Average = " + average1);
                }
            };
    public void saveRssi(String address) {
        getRsiAverage(address);
        // pour tester HashMap tableau
        HashMap<String, Integer> test = new HashMap<>();
        test.put("C5:32:52:D1:10:02", rsiAverage);
        test.put("balise2", rsiAverage);
        test.put("balise3", rsiAverage);
        test.put("balise4", rsiAverage);
        test.put("balise5", rsiAverage);
        test.put("balise6", rsiAverage);
////      获取坐标
//        Scanner sc = new Scanner(System.in);
//        System.out.print("请输入横坐标x：");
//        int x = sc.nextInt();
//        System.out.print("请输入纵坐标y：");
//        int y = sc.nextInt();
//        System.out.println("横坐标:" + x + ", 纵坐标:" + y);
        tableau[0][0] = test;
    }
    public int getRsiAverage(String address) {
        int sum = 0;
        int num = 1;
        int avrage = 0;
//        System.out.println("Rssi"+ rssi);
        List<Integer> rssi = capitalCities.get(address);
        for (int j = 0; j < rssi.size(); j++) {
            sum = sum + ((Integer)rssi.get(j)).intValue();
        }
        num = rssi.size();
        if(rssi.size() == 0){
            num = 1;
            bluetoothAdapter.stopLeScan(leScanCallback);
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
