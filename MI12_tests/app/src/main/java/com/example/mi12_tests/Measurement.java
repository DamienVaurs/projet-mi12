package com.example.mi12_tests;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Measurement {
    private Point point;
    private double rssi;
    public Point getPoint() {
        return point;
    }
    public double getRssi() {
        return rssi;
    }

    public Measurement(Point point, String deviceAddress) {
        System.out.println("CONSTRUCTEUR");
        this.point = point;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled
            return;
        }
        final String address = deviceAddress;
        final List<Double> rssiList = new ArrayList<>();
        ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                if (result.getDevice().getAddress().equals(address)) {
                    rssiList.add((double) result.getRssi());
                }
            }
        };
        BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(scanCallback);
        // Stop scan after 10 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bluetoothLeScanner.stopScan(scanCallback);
        // Calculate the mean RSSI
        double sum = 0;
        for (double r : rssiList) {
            sum += r;
        }
        this.rssi = sum / rssiList.size();
        System.out.println(this.rssi);
    }



}

