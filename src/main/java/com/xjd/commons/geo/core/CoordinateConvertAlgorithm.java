package com.xjd.commons.geo.core;


import com.xjd.commons.geo.model.Point;

public class CoordinateConvertAlgorithm {
    private static double RANGE_LON_MAX = 137.8347D;
    private static double RANGE_LON_MIN = 72.004D;
    private static double RANGE_LAT_MAX = 55.8271D;
    private static double RANGE_LAT_MIN = 0.8293D;
    private static double jzA = 6378245.0D;
    private static double jzEE = 0.006693421622965943D;

    public CoordinateConvertAlgorithm() {
    }

    public static Point wgs84ToGcj02(double lat, double lng) {
        return gcj02Encrypt(lat, lng);
    }

    public static Point gcj02ToWgs84(double lat, double lng) {
        return gcj02Decrypt(lat, lng);
    }

    public static Point wgs84ToBd09(double lat, double lng) {
        Point gcj02Pt = gcj02Encrypt(lat, lng);
        return bd09Encrypt(gcj02Pt.getLat(), gcj02Pt.getLng());
    }

    public static Point gcj02ToBd09(double lat, double lng) {
        return bd09Encrypt(lat, lng);
    }

    public static Point bd09ToGcj02(double lat, double lng) {
        return bd09Decrypt(lat, lng);
    }

    public static Point bd09ToWgs84(double lat, double lng) {
        Point gcj02 = bd09ToGcj02(lat, lng);
        return gcj02Decrypt(gcj02.getLat(), gcj02.getLng());
    }

    private static double LAT_OFFSET_0(double x, double y) {
        return -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 0.2D * Math.sqrt(Math.abs(x));
    }

    private static double LAT_OFFSET_1(double x, double y) {
        return (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
    }

    private static double LAT_OFFSET_2(double x, double y) {
        return (20.0D * Math.sin(y * 3.141592653589793D) + 40.0D * Math.sin(y / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
    }

    private static double LAT_OFFSET_3(double x, double y) {
        return (160.0D * Math.sin(y / 12.0D * 3.141592653589793D) + 320.0D * Math.sin(y * 3.141592653589793D / 30.0D)) * 2.0D / 3.0D;
    }

    private static double LON_OFFSET_0(double x, double y) {
        return 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * Math.sqrt(Math.abs(x));
    }

    private static double LON_OFFSET_1(double x, double y) {
        return (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
    }

    private static double LON_OFFSET_2(double x, double y) {
        return (20.0D * Math.sin(x * 3.141592653589793D) + 40.0D * Math.sin(x / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
    }

    private static double LON_OFFSET_3(double x, double y) {
        return (150.0D * Math.sin(x / 12.0D * 3.141592653589793D) + 300.0D * Math.sin(x / 30.0D * 3.141592653589793D)) * 2.0D / 3.0D;
    }

    private static double transformLat(double x, double y) {
        double ret = LAT_OFFSET_0(x, y);
        ret += LAT_OFFSET_1(x, y);
        ret += LAT_OFFSET_2(x, y);
        ret += LAT_OFFSET_3(x, y);
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = LON_OFFSET_0(x, y);
        ret += LON_OFFSET_1(x, y);
        ret += LON_OFFSET_2(x, y);
        ret += LON_OFFSET_3(x, y);
        return ret;
    }

    private static boolean outOfChina(double lat, double lon) {
        return lon < RANGE_LON_MIN || lon > RANGE_LON_MAX || lat < RANGE_LAT_MIN || lat > RANGE_LAT_MAX;
    }

    private static Point gcj02Encrypt(double ggLat, double ggLon) {
        Point returnPoint = new Point(ggLon, ggLat);
        double dLat = transformLat(ggLon - 105.0D, ggLat - 35.0D);
        double dLon = transformLon(ggLon - 105.0D, ggLat - 35.0D);
        double radLat = ggLat / 180.0D * 3.141592653589793D;
        double magic = Math.sin(radLat);
        magic = 1.0D - jzEE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0D / (jzA * (1.0D - jzEE) / (magic * sqrtMagic) * 3.141592653589793D);
        dLon = dLon * 180.0D / (jzA / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
        double mgLat = ggLat + dLat;
        double mgLon = ggLon + dLon;
        returnPoint.setLat(mgLat);
        returnPoint.setLng(mgLon);
        return returnPoint;
    }

    private static Point gcj02Decrypt(double gjLat, double gjLon) {
        Point gPt = gcj02Encrypt(gjLat, gjLon);
        double dLon = gPt.getLng() - gjLon;
        double dLat = gPt.getLat() - gjLat;
        Point pt = new Point(gjLon - dLon, gjLat - dLat);
        return pt;
    }

    private static Point bd09Decrypt(double bdLat, double bdLon) {
        double x = bdLon - 0.0065D;
        double y = bdLat - 0.006D;
        double z = Math.sqrt(x * x + y * y) - 2.0E-5D * Math.sin(y * 3.141592653589793D * 3000.0D / 180.0D);
        double theta = Math.atan2(y, x) - 3.0E-6D * Math.cos(x * 3.141592653589793D * 3000.0D / 180.0D);
        Point gcjPt = new Point(z * Math.cos(theta), z * Math.sin(theta));
        return gcjPt;
    }

    private static Point bd09Encrypt(double ggLat, double ggLon) {
        double z = Math.sqrt(ggLon * ggLon + ggLat * ggLat) + 2.0E-5D * Math.sin(ggLat * 3.141592653589793D * 3000.0D / 180.0D);
        double theta = Math.atan2(ggLat, ggLon) + 3.0E-6D * Math.cos(ggLon * 3.141592653589793D * 3000.0D / 180.0D);
        Point bdPt = new Point(z * Math.cos(theta) + 0.0065D, z * Math.sin(theta) + 0.006D);
        return bdPt;
    }
}
