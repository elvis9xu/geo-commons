package com.xjd.commons.geo.core;

import com.xjd.commons.geo.model.Point;

public abstract class DirectionAlgorithm {

    public static double[] vector(Point p1, Point p2) {
        double dy = AngleAlgorithm.degree2Radians(p2.getLat() - p1.getLat());
        double dx = AngleAlgorithm.degree2Radians(p2.getLng() - p1.getLng());
        return new double[]{dx, dy};
    }

    public static double radiansBetweenVector(double[] v1, double[] v2) {
        double x1 = v1[0], y1 = v1[1], x2 = v2[0], y2 = v2[1];
        double cos0 = (x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
        return Math.acos(cos0);
    }

    public static double direction(double[] v) {
        double r = radiansBetweenVector(v, new double[]{0, 1}); // 与正北方向的夹角, 值小于180
        if (v[0] > 0) { // 在右侧象向, 方向要大于180
            return 2 * Math.PI - r;
        } else {
            return r;
        }
    }

}
