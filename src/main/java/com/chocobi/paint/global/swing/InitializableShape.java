package com.chocobi.paint.global.swing;

import com.chocobi.paint.shape.CustomShape;

import java.awt.*;

public interface InitializableShape<T extends CustomShape> {
    T initShape(Point point);
}
