package com.xjd.commons.geo;

import com.xjd.commons.geo.core.AngleAlgorithm;
import com.xjd.commons.geo.core.DirectionAlgorithm;
import com.xjd.commons.geo.model.Point;

public abstract class DirectionTool {

    /**
     * 方向值, 正北方为0, 逆时针方向递增
     * @param from
     * @param to
     * @return
     */
    public static double direction(Point from, Point to) {
        return AngleAlgorithm.radians2Degree(DirectionAlgorithm.direction(DirectionAlgorithm.vector(from, to)));
    }

    /**
     * 计算由 向量p11 -> p12 与 向量p21 -> p22 组成的夹角, 夹角范围0~180
     * @param p11
     * @param p12
     * @param p21
     * @param p22
     * @return
     */
    public static double angle(Point p11, Point p12, Point p21, Point p22) {
        double[] v1 = DirectionAlgorithm.vector(p11, p12);
        double[] v2 = DirectionAlgorithm.vector(p21, p22);
        return AngleAlgorithm.radians2Degree(DirectionAlgorithm.radiansBetweenVector(v1, v2));
    }

    /**
     * 计算由 向量p11 -> p12 与 向量p21 -> p22 组成的带符号夹角, 逆时针为正, 顺时针为负, 夹角范围为 -180 ~ 180
     * @param p11
     * @param p12
     * @param p21
     * @param p22
     * @return
     */
    public static double angleSigned(Point p11, Point p12, Point p21, Point p22) {
        double[] v1 = DirectionAlgorithm.vector(p11, p12);
        double[] v2 = DirectionAlgorithm.vector(p21, p22);
        return AngleAlgorithm.radians2Degree(DirectionAlgorithm.radiansSignedBetweenVector(v1, v2));
    }
}
