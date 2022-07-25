package com.chocobi.paint.shape;

import java.awt.*;
import java.awt.geom.Line2D;

public class CustomLine extends CustomShape {
    public CustomLine(Point point) {
        super(PointCount.TWO, point, new Line2D.Double());
    }

    @Override
    public void resize(Point point) {
        ((Line2D.Double) super.shape).setLine(super.firstPoint.getX(), super.firstPoint.getY(), point.getX(), point.getY());
    }
}
