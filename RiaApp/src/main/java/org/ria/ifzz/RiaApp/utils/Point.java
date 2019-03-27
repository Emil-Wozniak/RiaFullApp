package org.ria.ifzz.RiaApp.utils;

public class Point {

    private double value;
    private boolean flag;

    public Point(double value, boolean flag) {
        this.value = value;
        this.flag = flag;
    }

    public double getValue() {
        return value;
    }

    public boolean isFlag() {
        return flag;
    }

    public void getPoint() {
        System.out.println("[" + getValue() + " | " + isFlag() + "]");
    }
}
