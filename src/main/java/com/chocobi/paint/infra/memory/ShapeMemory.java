package com.chocobi.paint.infra.memory;

import com.chocobi.paint.panel.DrawingPanel;
import com.chocobi.paint.shape.CustomShape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class ShapeMemory {
    private final DrawingPanel drawingPanel;
    private static List<CustomShape> copyShapes;
    private Stack<List<CustomShape>> previousShapes = new Stack<>();

    public void copy() {
        copyShapes = this.drawingPanel.getSelectedShapesStream().map(SerializationUtils::clone).collect(Collectors.toList());
    }

    public void cut() {
        copyShapes = this.drawingPanel.getSelectedShapesStream().collect(Collectors.toList());
        drawingPanel.getShapes().removeAll(copyShapes);
        copyShapes = copyShapes.stream().map(SerializationUtils::clone).collect(Collectors.toList());
        this.drawingPanel.repaint();
    }

    public void paste() {
        AffineTransform affineTransform = new AffineTransform();
        copyShapes.forEach(shape -> {
            affineTransform.translate(10, 10);
            shape.setShape(affineTransform.createTransformedShape(shape.getShape()));
        });
        this.drawingPanel.savePreviousShapes();
        this.drawingPanel.allShapesSetSelectedFalse();
        this.drawingPanel.getShapes().addAll(copyShapes);
        copyShapes = copyShapes.stream().map(SerializationUtils::clone).collect(Collectors.toList());
        this.drawingPanel.repaint();
    }

    public void undo() {
        this.drawingPanel.setShapes(this.previousShapes.pop());
        this.drawingPanel.repaint();
    }
}
