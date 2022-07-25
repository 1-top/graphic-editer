package com.chocobi.paint.shape;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CustomEllipse extends CustomShape {
    public CustomEllipse(Point point) {
        super(PointCount.TWO, point, new Ellipse2D.Double());
    }

    @Override
    public void resize(Point point) {
        ((Ellipse2D.Double) super.shape).setFrame(
                Math.min(super.firstPoint.x, point.x),
                Math.min(super.firstPoint.y, point.y),
                Math.abs(super.firstPoint.x - point.x),
                Math.abs(super.firstPoint.y - point.y)
        );
    }
}
