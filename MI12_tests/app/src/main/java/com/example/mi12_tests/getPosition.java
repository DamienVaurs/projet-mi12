package com.example.mi12_tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getPosition {

    private List<Measurement> measurements;

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
