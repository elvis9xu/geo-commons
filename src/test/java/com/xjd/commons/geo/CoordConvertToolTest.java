package com.xjd.commons.geo;


import com.xjd.commons.geo.model.CoordType;
import com.xjd.commons.geo.model.Point;

class CoordConvertToolTest {

    public static void main(String[] args) {
        {
            Point point = CoordConvertTool.convert(new Point(101.961159, 23.625479), CoordType.BD09, CoordType.GCJ02);
            System.out.println(point);
        }
        {
            Point point = CoordConvertTool.convert(new Point(101.746331,23.853524), CoordType.BD09, CoordType.GCJ02);
            System.out.println(point);
        }
    }
}