package com.example.mi12_tests;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
        BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (device.getAddress().equals(address)) {
                    rssiList.add((double) rssi);
                }
            }
        };
        bluetoothAdapter.startLeScan(leScanCallback);
        // Stop scan after 10 seconds
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bluetoothAdapter.stopLeScan(leScanCallback);
        // Calculate the mean RSSI
        double sum = 0;
        for (double r : rssiList) {
            sum += r;
        }
        this.rssi = sum / rssiList.size();
    }



}

