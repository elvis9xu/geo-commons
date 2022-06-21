package com.xjd.commons.geo.core;

public abstract class AngleAlgorithm {

    public static double degree2Radians(double degree) {
        return degree * (Math.PI / 180);
    }

    public static double radians2Degree(double radians) {
        return radians * (180 / Math.PI);
    }
}
