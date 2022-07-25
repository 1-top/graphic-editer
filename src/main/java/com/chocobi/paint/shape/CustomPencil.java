package com.chocobi.paint.shape;

import java.awt.*;
import java.awt.geom.Path2D;

public class CustomPencil extends CustomShape {
    public CustomPencil(Point firstPoint) {
        super(PointCount.TWO, firstPoint, new Path2D.Double());
        Path2D.Double shape = (Path2D.Double) super.shape;
        shape.moveTo(firstPoint.getX(), firstPoint.getY());
        shape.lineTo(firstPoint.getX(), firstPoint.getY());
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.drawLineColor(graphics2D);
        super.drawAnchor(graphics2D);
    }

    @Override
    public void resize(Point point) {
        ((Path2D.Double) super.shape).lineTo(point.getX(), point.getY());
    }
}
