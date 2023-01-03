package com.example.mi12;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class getRssi {
    public int getRssi(String deviceAddress) {
        int rssi = 0;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            return rssi;
        }
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled
            return rssi;
        }
        Context context = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permission is not granted
            return rssi;
        }
        final String address = deviceAddress;
        BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (device.getAddress().equals(address)) {
                    // Device found, rssi has the RSSI value
                    rssi = rssi;
                }
            }
        };
        bluetoothAdapter.startLeScan(leScanCallback);
        // Stop scan after 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bluetoothAdapter.stopLeScan(leScanCallback);
        return rssi;
    }

    public Point getPosition(List<Measurement> measurements) {
        // Create a map of RSSI values
        Map<Point, Double> map = new HashMap<>();
        for (Measurement measurement : measurements) {
            Point point = measurement.getPoint();
            double rssi = measurement.getRssi();
            map.put(point, rssi);
        }
        // Find the point with the highest RSSI
        Point bestPoint = null;
        double bestRssi = Double.MIN_VALUE;
        for (Map.Entry<Point, Double> entry : map.entrySet()) {
            Point point = entry.getKey();
            double rssi = entry.getValue();
            if (rssi > bestRssi) {
                bestPoint = point;
                bestRssi = rssi;
            }
        }
        // Interpolate or extrapolate to find the precise position of the tag
        Point position = interpolate(map, bestPoint);
        return position;
    }

    private Point interpolate(Map<Point, Double> map, Point bestPoint) {
        // Find the surrounding points with the highest RSSI
        Point p1 = null, p2 = null;
        double r1 = Double.MIN_VALUE, r2 = Double.MIN_VALUE;
        for (Map.Entry<Point, Double> entry : map.entrySet()) {
            Point point = entry.getKey();
            double rssi = entry.getValue();
            if (point.getX() < bestPoint.getX() || (point.getX() == bestPoint.getX() && point.getY() < bestPoint.getY())) {
                if (rssi > r1) {
                    p1 = point;
                    r1 = rssi;
                }
            } else if (point.getX() > bestPoint.getX() || (point.getX() == bestPoint.getX() && point.getY() > bestPoint.getY())) {
                if (rssi > r2) {
                    p2 = point;
                    r2 = rssi;
                }
            }
        }
        // Interpolate the position of the tag
        if (p1 == null || p2 == null || r1 == Double.MIN_VALUE || r2 == Double.MIN_VALUE) {
            // Cannot interpolate, return the best point
            return bestPoint;
        } else {
            double x = (bestPoint.getX() * r2 - p2.getX() * r1) / (r2 - r1);
            double y = (bestPoint.getY() * r2 - p2.getY() * r1) / (r2 - r1);
            return new Point(x, y);
        }
    }

}