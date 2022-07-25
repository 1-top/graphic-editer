package com.chocobi.paint.global.swing;

import com.chocobi.paint.shape.CustomShape;

import javax.swing.*;
import java.awt.*;

public class ShapeJRadioButton<T extends CustomShape> extends JRadioButton implements InitializableShape<T> {
    private final InitializableShape<T> initializableShape;

    public ShapeJRadioButton(Icon icon, InitializableShape<T> initializableShape) {
        super(icon);
        this.initializableShape = initializableShape;
    }

    public ShapeJRadioButton(String title, InitializableShape<T> initializableShape) {
        super(title);
        this.initializableShape = initializableShape;
    }

    @Override
    public T initShape(Point point) {
        return this.initializableShape.initShape(point);
    }
}
