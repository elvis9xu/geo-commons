package com.xjd.commons.geo;


import com.xjd.commons.geo.model.Point;

class DirectionToolTest {

    public static void main(String[] args) {
        System.out.println(DirectionTool.direction(new Point(102.54848725, 24.35054961), new Point(102.549422, 24.35054961)));
        System.out.println(DirectionTool.direction(new Point(116.380571,39.913282), new Point(116.381442,39.914261)));
    }
}