package com.xjd.commons.geo;

import com.xjd.commons.geo.core.GeoHashAlgorithm;
import com.xjd.commons.geo.model.Point;

public abstract class GeoHashTool {

    /**
     * 求坐标点的GeoHash值
     * @param p
     * @param len geoHash值的位数
     * @return
     */
    public static String geoHash(Point p, int len) {
        return GeoHashAlgorithm.base32Point(p.getLng(), p.getLat(), len);
    }

    /**
     * 获取给定的GeoHash值所对应的Hash块的范围, 返回范围的左下角坐标和右上角坐标
     * @param geoHash
     * @return
     */
    public static Point[] rangeOfHashBlock(String geoHash) {
        double[][] range = GeoHashAlgorithm.getRangeOfHashBlock(geoHash);
        Point[] points = new Point[2];
        points[0] = new Point(range[0][0], range[0][1]);
        points[1] = new Point(range[1][0], range[1][1]);
        return points;
    }

    /**
     * 获取给定的GeoHash值所对应的Hash块范围的中心点
     * @param geoHash
     * @return
     */
    public static Point centerOfHashBlock(String geoHash) {
        Point[] points = rangeOfHashBlock(geoHash);
        return new Point((points[0].getLng() + points[1].getLng()) / 2, (points[0].getLat() + points[1].getLat()) / 2);
    }


    /**
     * 返回给定的hash块周边相邻的hash块的geohash值
     * @param geoHash
     * @param adjacentDirections 相邻的hash块方位, 可选值有 L,LU,U,RU,R,RD,D,LD 多个值使用逗号分隔, 空表示所有邻近点, 顺序如前面所示
     * @return 邻近的Hash块的GeoHash值数组
     */
    public static String[] adjacentHashBlock(String geoHash, String adjacentDirections) {
        if (adjacentDirections == null || "".equals(adjacentDirections.trim())) adjacentDirections = GeoHashAlgorithm.NEAR_ALL;
        return GeoHashAlgorithm.getBase32NearPoint(geoHash, adjacentDirections);
    }
}
