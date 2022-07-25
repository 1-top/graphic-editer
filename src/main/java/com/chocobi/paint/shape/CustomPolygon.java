package com.chocobi.paint.shape;

import java.awt.*;

public class CustomPolygon extends CustomShape {
    public CustomPolygon(Point point) {
        super(PointCount.N, point, new Polygon());
        ((Polygon) super.shape).addPoint(point.x, point.y);
    }

    @Override
    public void resize(Point point) {
        Polygon shape = (Polygon) super.shape;
        shape.xpoints[shape.npoints - 1] = point.x;
        shape.ypoints[shape.npoints - 1] = point.y;
    }

    @Override
    public void addPoint(Point point) {
        ((Polygon) super.shape).addPoint(point.x, point.y);
    }
}
