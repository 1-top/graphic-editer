package com.chocobi.paint.shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CustomRectangle extends CustomShape {
    public CustomRectangle(Point point) {
        super(PointCount.TWO, point, new Rectangle2D.Double());
        ((Rectangle2D.Double) super.shape).setRect(point.getX(), point.getY(), 0, 0);
    }

    @Override
    public void resize(Point point) {
        ((Rectangle2D.Double) super.shape).setRect(
                Math.min(super.firstPoint.x, point.x),
                Math.min(super.firstPoint.y, point.y),
                Math.abs(super.firstPoint.x - point.x),
                Math.abs(super.firstPoint.y - point.y)
        );
    }
}
