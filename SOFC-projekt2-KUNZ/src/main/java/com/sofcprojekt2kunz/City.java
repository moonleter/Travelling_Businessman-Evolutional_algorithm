package com.sofcprojekt2kunz;

public class City {
    private final double x;
    private final double y;

    public City(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(City other) {
        double dx = x - other.getX();
        double dy = y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
