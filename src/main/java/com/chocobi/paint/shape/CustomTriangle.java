package com.chocobi.paint.shape;

import java.awt.*;

public class CustomTriangle extends CustomShape {
    private final int[] xpoints = new int[3];
    private final int[] ypoints = new int[3];

    public CustomTriangle(Point point) {
        super(PointCount.TWO, point, new Polygon());
        Polygon shape = (Polygon) super.shape;
        shape.xpoints = this.xpoints;
        shape.ypoints = this.ypoints;
        shape.npoints = 3;
    }

    @Override
    public void resize(Point point) {
        int width = point.x - super.firstPoint.x;
        Polygon shape = (Polygon) super.shape;
        shape.xpoints[0] = super.firstPoint.x;
        shape.ypoints[0] = point.y;
        shape.xpoints[1] = super.firstPoint.x + width / 2;
        shape.ypoints[1] = super.firstPoint.y;
        shape.xpoints[2] = super.firstPoint.x + width;
        shape.ypoints[2] = point.y;
    }
}
