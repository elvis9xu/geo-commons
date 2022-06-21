package com.xjd.commons.geo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    // 经度
    protected double lng;

    // 纬度
    protected double lat;

    @Override
    public String toString() {
        return "[" + getLng() + ", " + getLat() + "]";
    }

    public Point copy() {
        return new Point(getLng(), getLat());
    }
}
