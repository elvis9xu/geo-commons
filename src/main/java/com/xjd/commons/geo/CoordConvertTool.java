package com.xjd.commons.geo;

import com.xjd.commons.geo.core.CoordinateConvertAlgorithm;
import com.xjd.commons.geo.model.CoordType;
import com.xjd.commons.geo.model.Point;

public abstract class CoordConvertTool {

    /**
     * 转换坐标
     * @param point
     * @param from
     * @param to
     * @return
     */
    public static Point convert(Point point, CoordType from, CoordType to) {
        if (from == to) return point.copy();

        if (from == CoordType.WGS84) {
            if (to == CoordType.GCJ02) return CoordinateConvertAlgorithm.wgs84ToGcj02(point.getLat(), point.getLng());
            if (to == CoordType.BD09) return CoordinateConvertAlgorithm.wgs84ToBd09(point.getLat(), point.getLng());
        }

        if (from == CoordType.GCJ02) {
            if (to == CoordType.WGS84) return CoordinateConvertAlgorithm.gcj02ToWgs84(point.getLat(), point.getLng());
            if (to == CoordType.BD09) return CoordinateConvertAlgorithm.gcj02ToBd09(point.getLat(), point.getLng());
        }

        if (from == CoordType.BD09) {
            if (to == CoordType.WGS84) return CoordinateConvertAlgorithm.bd09ToWgs84(point.getLat(), point.getLng());
            if (to == CoordType.GCJ02) return CoordinateConvertAlgorithm.bd09ToGcj02(point.getLat(), point.getLng());
        }

        throw new RuntimeException("impossible error");
    }
}
