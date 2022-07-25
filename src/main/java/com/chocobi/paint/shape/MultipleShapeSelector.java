package com.chocobi.paint.shape;

import java.awt.*;
import java.util.List;

public class MultipleShapeSelector extends CustomRectangle {
    public MultipleShapeSelector() {
        super(new Point());
        super.fillColor = new Color(0, 0, 0, 0.3f);
    }

    public void initPoint(Point point) {
        super.firstPoint = point;
    }

    public void setContainsShapesSelectedTrue(List<CustomShape> shapes) {
        shapes.stream().filter(shape -> shape.contains(super.shape.getBounds2D())).forEach(shape -> shape.setSelected(true));
    }

    @Override
    public void setSelected(boolean selected) {
        super.selected = false;
    }
}
