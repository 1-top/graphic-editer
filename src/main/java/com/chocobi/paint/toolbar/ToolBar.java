package com.chocobi.paint.toolbar;

import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.global.swing.CustomButtonGroup;
import com.chocobi.paint.global.swing.ShapeJRadioButton;
import com.chocobi.paint.shape.CustomShape;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Getter
public class ToolBar extends JToolBar {
    private CustomButtonGroup buttonGroup = new CustomButtonGroup();
    private final ColorChooser colorChooser = new ColorChooser(Constant.RESOURCE_BUNDLE.getString("tool.background_color"), ColorChooser.Type.FILL);

    public ToolBar() {
        this(JToolBar.CENTER);
    }

    public ToolBar(int orientation) {
        super(orientation);
        super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        for (ToolbarConstant value : ToolbarConstant.values()) {
            ShapeJRadioButton<?> button = new ShapeJRadioButton<>(value.getImageIcon(), value.getInitializableShape());
            button.setToolTipText(value.getToolTip());
            button.setSelectedIcon(value.getSelectedImageIcon());
            button.setActionCommand(value.getToolAttributeType().name());
            button.addActionListener(this::shapeToolActionHandler);
            super.add(button);
            this.buttonGroup.add(button);
        }
        super.add(this.colorChooser);
        this.buttonGroup.getElements().nextElement().doClick();
    }

    public ShapeJRadioButton<?> getSelectedButton() {
        return (ShapeJRadioButton<?>) this.buttonGroup.getSelectedButton();
    }

    public void shapeToolActionHandler(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals(CustomShape.ToolAttributeType.ONLY_LINE.name()))
            ShapeAttributeToolBar.getInstance().setFillPanelVisibleFalse();
        else ShapeAttributeToolBar.getInstance().initFillAttribute(this.colorChooser.getColor());

        if (actionEvent.getActionCommand().equals(CustomShape.ToolAttributeType.IMAGE.name())) {
            Constant.IMAGE_FILE_CHOOSER.showOpenDialog(this);
        }
    }
}
