package com.xjd.commons.geo;

import com.google.common.geometry.S2LatLng;
import com.xjd.commons.geo.core.AngleAlgorithm;
import com.xjd.commons.geo.model.GeoConst;
import com.xjd.commons.geo.model.Point;

public abstract class CoordRelationTool {

    /**
     * 求2个坐标点与球心连线的夹角(弧度)
     * @param p1
     * @param p2
     * @return
     */
    public static double angleRadiansBetween(Point p1, Point p2) {
        S2LatLng s1 = S2LatLng.fromDegrees(p1.getLat(), p1.getLng());
        S2LatLng s2 = S2LatLng.fromDegrees(p2.getLat(), p2.getLng());
        return s1.toPoint().angle(s2.toPoint());
    }

    /**
     * 求2个坐标点与球心连线的夹角(角度)
     * @param p1
     * @param p2
     * @return
     */
    public static double angleDegreesBetween(Point p1, Point p2) {
        return AngleAlgorithm.radians2Degree(angleRadiansBetween(p1, p2));
    }

    /**
     * 计算2个坐标点间的球面距离
     * @param p1
     * @param p2
     * @return
     */
    public static double distancesBetween(Point p1, Point p2) {
        return angleRadiansBetween(p1, p2) * GeoConst.EARTH_R_AVG;
    }

}
