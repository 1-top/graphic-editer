package com.chocobi.paint.shape;

import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.awt.*;
import java.awt.geom.Path2D;
import java.lang.reflect.Field;

public class CustomPolyline extends CustomShape {
    private transient final Field doubleCoordsField;
    private transient final Field numCoordsField;

    public CustomPolyline(Point point) {
        super(PointCount.N, point, new Path2D.Double());
        ((Path2D.Double) super.shape).moveTo(point.getX(), point.getY());

        this.doubleCoordsField = FieldUtils.getField(Path2D.Double.class, "doubleCoords", true);
        this.numCoordsField = FieldUtils.getField(Path2D.class, "numCoords", true);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.drawLineColor(graphics2D);
        super.drawAnchor(graphics2D);
    }

    @SneakyThrows
    @Override
    public void resize(Point point) {
        double[] doubleCoords = (double[]) doubleCoordsField.get(super.shape);
        int numCoords = (int) numCoordsField.get(super.shape);
        doubleCoords[numCoords - 2] = point.x;
        doubleCoords[numCoords - 1] = point.y;
    }

    @Override
    public void addPoint(Point point) {
        ((Path2D.Double) super.shape).lineTo(point.getX(), point.getY());
    }
}
