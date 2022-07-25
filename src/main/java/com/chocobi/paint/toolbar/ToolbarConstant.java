package com.chocobi.paint.toolbar;

import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.global.swing.InitializableShape;
import com.chocobi.paint.shape.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;

@Getter
@RequiredArgsConstructor
public enum ToolbarConstant {
    SINGLE_SELECTION(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/single_selection.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/single_selection_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.single_selection"),
            CustomShape.ToolAttributeType.SINGLE_SELECTION,
            null
    ),
    MULTI_SELECTION(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/multi_selection.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/multi_selection_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.multi_selection"),
            CustomShape.ToolAttributeType.MULTI_SELECTION,
            null
    ),
    LINE(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/line.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/line_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.line"),
            CustomShape.ToolAttributeType.ONLY_LINE,
            CustomLine::new
    ),
    RECTANGLE(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/rectangle.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/rectangle_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.rectangle"),
            CustomShape.ToolAttributeType.LINE_AND_FILL,
            CustomRectangle::new
    ),
    ELLIPSE(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/ellipse.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/ellipse_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.ellipse"),
            CustomShape.ToolAttributeType.LINE_AND_FILL,
            CustomEllipse::new
    ),
    TRIANGLE(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/triangle.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/triangle_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.triangle"),
            CustomShape.ToolAttributeType.LINE_AND_FILL,
            CustomTriangle::new
    ),
    POLYGON(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/polygon.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/polygon_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.polygon"),
            CustomShape.ToolAttributeType.LINE_AND_FILL,
            CustomPolygon::new
    ),
    POLYLINE(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/polyline.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/polyline_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.polyline"),
            CustomShape.ToolAttributeType.ONLY_LINE,
            CustomPolyline::new
    ),
    IMAGE(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/image.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/image_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.image"),
            CustomShape.ToolAttributeType.IMAGE,
            ImageShape::new
    ),
    PEN(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/pencil.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/pencil_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.pencil"),
            CustomShape.ToolAttributeType.ONLY_LINE,
            CustomPencil::new
    ),
    ERASER(
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/eraser.png")),
            new ImageIcon(ClassLoader.getSystemClassLoader().getResource("assets/icon/eraser_selected.png")),
            Constant.RESOURCE_BUNDLE.getString("tool.eraser"),
            CustomShape.ToolAttributeType.ERASER,
            Eraser::new
    );

    private final ImageIcon imageIcon;
    private final ImageIcon selectedImageIcon;
    private final String toolTip;
    private final CustomShape.ToolAttributeType toolAttributeType;
    private final InitializableShape<?> initializableShape;
}
