package com.chocobi.paint.toolbar;

import com.chocobi.paint.panel.DrawingDesktopPane;
import com.chocobi.paint.panel.DrawingPanel;
import com.chocobi.paint.shape.CustomShape;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ColorChooser extends JCheckBox {
    public enum Type {
        LINE, FILL
    }

    private final JPanel colorPanel = new JPanel();
    private final Type type;
    private Color color = Color.BLACK;

    public ColorChooser(String toolTip, Type type) {
        super();
        this.type = type;
        this.colorPanel.setBorder(BorderUIResource.getBlackLineBorderUIResource());
        this.colorPanel.setBackground(this.color);
        this.colorPanel.setMaximumSize(new Dimension(22, 22));
        this.removeCheckBox();
        super.setToolTipText(toolTip);
        super.addActionListener(this::setColor);
        super.add(this.colorPanel);
    }

    private void removeCheckBox() {
        super.setIcon(new ImageIcon());
    }

    public void setColor(Color color) {
        this.color = color;
        this.colorPanel.setBackground(this.color);
    }

    private void setColor(ActionEvent actionEvent) {
        this.setColor(JColorChooser.showDialog(this.getParent().getParent(), null, this.color));
        DrawingPanel drawingPanel = DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel();
        List<CustomShape> selectedShapes = drawingPanel.getSelectedShapesStream().collect(Collectors.toList());
        drawingPanel.savePreviousShapes();
        if (this.type == Type.LINE) selectedShapes.forEach(shape -> shape.setLineColor(this.color));
        else if (this.type == Type.FILL) selectedShapes.forEach(shape -> shape.setFillColor(this.color));
        drawingPanel.repaint();
    }
}
