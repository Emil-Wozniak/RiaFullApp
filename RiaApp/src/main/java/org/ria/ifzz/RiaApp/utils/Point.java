package org.ria.ifzz.RiaApp.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Point {

    private double value;
    private boolean flag;

    public Point(double value, boolean flag) {
        this.value = value;
        this.flag = flag;
    }

    double getValue() {
        return value;
    }

    boolean isFlag() {
        return flag;
    }

    public void getPoint() {
        System.out.println("[" + getValue() + " | " + isFlag() + "]");
    }
}
