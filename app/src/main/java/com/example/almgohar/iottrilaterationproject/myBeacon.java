package com.example.almgohar.iottrilaterationproject;

public class myBeacon {
    private double TxPower;
    private double RssI;
    private double xPos;
    private double yPos;
    private double distance;
    private String name;

    public myBeacon(double distance, String name, double x, double y) {
        this.distance = distance;
        this.xPos = x;
        this.yPos = y;
        this.name = name;
    }

    public myBeacon() {

    }

    public void calculateDistance() {
        this.distance = Math.pow(10, TxPower - RssI) / (10 * 2.5);

    }

    public double getTxPower() {
        return TxPower;
    }

    public void setTxPower(double txPower) {
        TxPower = txPower;
    }

    public double getRssI() {
        return RssI;
    }

    public void setRssI(double rssI) {
        RssI = rssI;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
