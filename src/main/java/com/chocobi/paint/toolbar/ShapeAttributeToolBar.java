package com.chocobi.paint.toolbar;

import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.panel.DrawingDesktopPane;
import com.chocobi.paint.panel.DrawingPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapeAttributeToolBar extends JToolBar {
    private static final ShapeAttributeToolBar INSTANCE = new ShapeAttributeToolBar();

    private final JPanel fillPanel = new JPanel();

    private final JTextField strokeTextField = new JTextField();
    private final JSlider strokeThicknessSlider = new JSlider();

    private final ColorChooser fillColorChooser = new ColorChooser(Constant.RESOURCE_BUNDLE.getString("tool.fill_color"), ColorChooser.Type.FILL);
    private final ColorChooser lineColorChooser = new ColorChooser(Constant.RESOURCE_BUNDLE.getString("tool.line_color"), ColorChooser.Type.LINE);

    private ShapeAttributeToolBar() {
        super.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel fillLabel = new JLabel(Constant.RESOURCE_BUNDLE.getString(Constant.MessageKey.SHAPE_ATTRIBUTE_FILL.getKey()));
        this.fillPanel.add(fillLabel);
        this.fillPanel.add(fillColorChooser);
        super.add(this.fillPanel);
        JLabel lineLabel = new JLabel(Constant.RESOURCE_BUNDLE.getString(Constant.MessageKey.SHAPE_ATTRIBUTE_LINE.getKey()));
        super.add(lineLabel);
        super.add(this.lineColorChooser);

        this.strokeThicknessSlider.setValue(1);
        this.strokeTextField.setColumns(2);
        this.strokeTextField.setText(String.valueOf(this.strokeThicknessSlider.getValue()));
        this.strokeTextField.addActionListener(new StrokeTextFieldChangeHandler());
        this.strokeThicknessSlider.setMinimum(0);
        this.strokeThicknessSlider.setMaximum(100);
        this.strokeThicknessSlider.addChangeListener(new StrokeThicknessSliderChangeHandler());
        super.add(this.strokeTextField);
        super.add(this.strokeThicknessSlider);
    }

    public void setFillPanelVisibleFalse() {
        this.fillPanel.setVisible(false);
    }

    public void initFillAttribute(Color fillColor) {
        this.fillPanel.setVisible(true);
        this.fillColorChooser.setColor(fillColor);
    }

    public void setStrokeThicknessSliderValue(String value) {
        int intValue = Integer.parseInt(value);
        if (intValue > 100) this.strokeThicknessSlider.setValue(100);
        else this.strokeThicknessSlider.setValue(Math.max(intValue, 0));
        this.setStrokeTextFieldText(this.strokeThicknessSlider.getValue());
    }

    public void setStrokeTextFieldText(int value) {
        this.strokeTextField.setText(String.valueOf(value));
        DrawingPanel drawingPanel = DrawingDesktopPane.getInstance().getCurrentInternalFrame().getDrawingPanel();
        drawingPanel.getSelectedShapesStream().forEach(shape -> shape.setStroke(this.strokeThicknessSlider.getValue()));
        drawingPanel.repaint();
    }

    public static ShapeAttributeToolBar getInstance() {
        return INSTANCE;
    }

    public Color getFillColor() {
        return this.fillColorChooser.getColor();
    }

    public Color getLineColor() {
        return this.lineColorChooser.getColor();
    }

    public int getStrokeThicknessValue() {
        return this.strokeThicknessSlider.getValue();
    }

    private class StrokeTextFieldChangeHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setStrokeThicknessSliderValue(((JTextField) actionEvent.getSource()).getText());
        }
    }

    private class StrokeThicknessSliderChangeHandler implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            setStrokeTextFieldText(((JSlider) changeEvent.getSource()).getValue());
        }
    }
}
