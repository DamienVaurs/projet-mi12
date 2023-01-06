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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Measurement {
    private Point point;
    private double rssi;

    public Point getPoint() {
        return point;
    }

    public double getRssi() {
        return rssi;
    }

    public Measurement(Point point, double rssi) {
        System.out.println("Constructor Measurement");
        this.point = point;
        this.rssi = rssi;
        System.out.println("Point X : " + this.point.getX());
        System.out.println("Point Y : " + this.point.getY());
        System.out.println("RSSI : " + this.rssi);
    }

    public static Point getPosition(List<Measurement> measurements) {
        // Create a map of RSSI values
        Map<Point, Double> map = new HashMap<>();
        for (Measurement measurement : measurements) {
            Point point = measurement.getPoint();
            double rssi = measurement.getRssi();
            map.put(point, rssi);
        }
        // Find the point with the highest RSSI
        Point bestPoint = new Point(0,0);
        double bestRssi = -100;
        for (Map.Entry<Point, Double> entry : map.entrySet()) {
            Point point = entry.getKey();
            System.out.println("Point getKey : " + point);
            double rssi = entry.getValue();
            System.out.println("Rssi getValue : " + rssi);
            if (rssi > bestRssi) {
                bestPoint = point;
                bestRssi = rssi;
            }
        }
        // Interpolate or extrapolate to find the precise position of the tag
        System.out.println("BestPoint : " + bestPoint.getX());
        Point position = interpolate(map, bestPoint);
        return position;
    }

    private static Point interpolate(Map<Point, Double> map, Point bestPoint) {
        Point p1 = new Point(0,0), p2 = new Point(0,0);
        double r1 = -100, r2 = -100;
        for (Map.Entry<Point, Double> entry : map.entrySet()) {
            Point point = entry.getKey();
            double rssi = entry.getValue();
            if (point.getX() < bestPoint.getX() || (point.getX() == bestPoint.getX() && point.getY() < bestPoint.getY())) {
                if (rssi > r1) {
                    p1 = point;
                    r1 = rssi;
                    System.out.println("Nouveau r1 : " + r1);
                }
            } else if (point.getX() > bestPoint.getX() || (point.getX() == bestPoint.getX() && point.getY() > bestPoint.getY())) {
                if (rssi > r2) {
                    p2 = point;
                    r2 = rssi;
                    System.out.println("Nouveau r2 : " + r2);
                }
            }
        }
        if (p1 == null || p2 == null || r1 == -100 || r2 == -100) {
            System.out.println("Pas d'interpolation");
            return bestPoint;
        } else {
            double x = (bestPoint.getX() * abs(r2) - p2.getX() * r1) / (r2 - r1);
            double y = (bestPoint.getY() * r2 - p2.getY() * r1) / (r2 - r1);
            System.out.println("Interpolate X : " + x);
            return new Point(x, y);
        }
    }
}

