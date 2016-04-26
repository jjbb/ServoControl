package com.zju.servo;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class Coordinate {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private int scaleNum;

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public int getScaleNum() {
        return scaleNum;
    }

    public void setScaleNum(int scaleNum) {
        this.scaleNum = scaleNum;
    }


    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

}
