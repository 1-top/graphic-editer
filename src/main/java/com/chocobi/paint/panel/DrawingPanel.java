package com.chocobi.paint.panel;

import com.chocobi.paint.global.constant.Constant;
import com.chocobi.paint.global.constant.CursorConstant;
import com.chocobi.paint.infra.memory.ShapeMemory;
import com.chocobi.paint.shape.*;
import com.chocobi.paint.toolbar.ShapeAttributeToolBar;
import com.chocobi.paint.toolbar.ToolBar;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.SerializationUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
public class DrawingPanel extends JPanel implements Printable {
    private boolean updated;
    private CustomShape currentShape;
    private CustomShape onShape;
    private MultipleShapeSelector multipleShapeSelector = new MultipleShapeSelector();
    private DrawingState currentDrawingState = DrawingState.IDLE;
    private List<CustomShape> shapes = new ArrayList<>();

    private Anchor.AnchorType selectedAnchorType;

    private Image doubleBufferedImage;
    private Graphics doubleBufferedGraphics;

    private final ToolBar toolBar = DrawingDesktopPane.getInstance().getToolBar();
    private final ShapeMemory shapeMemory = new ShapeMemory(this);

    public DrawingPanel() {
        super.setBackground(Color.WHITE);
        super.addMouseListener(new DrawingPanelMouseEventHandler());
        super.addMouseMotionListener(new DrawingPanelMouseEventHandler());
    }

    @Override
    public void paint(Graphics g) {
        try {
            this.doubleBufferedImage = Objects.requireNonNull(super.createImage(super.getWidth(), super.getHeight()));
            this.doubleBufferedGraphics = this.doubleBufferedImage.getGraphics();
            super.paint(this.doubleBufferedGraphics);
            this.shapes.forEach(shape -> shape.draw((Graphics2D) this.doubleBufferedGraphics));
            g.drawImage(this.doubleBufferedImage, 0, 0, this);
        } catch (NullPointerException e) {
            super.paint(g);
            this.shapes.forEach(shape -> shape.draw((Graphics2D) g));
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        this.shapes.forEach(shape -> shape.draw(graphics2D));
        return PAGE_EXISTS;
    }

    public void loadShapes(List<CustomShape> shapes) {
        this.shapes = shapes;
        super.repaint();
    }

    private void prepareDrawing(Point point) {
        this.updated = true;
        this.allShapesSetSelectedFalse();
        this.currentShape = this.toolBar.getSelectedButton().initShape(point);
        this.validateImageShape();
        this.currentShape.setFillColor(ShapeAttributeToolBar.getInstance().getFillColor());
        this.currentShape.setLineColor(ShapeAttributeToolBar.getInstance().getLineColor());
        this.currentShape.setStroke(ShapeAttributeToolBar.getInstance().getStrokeThicknessValue());
        this.setCurrentDrawingState();
        super.setCursor(CursorConstant.CROSS.getCursor());
        if (!(this.currentShape instanceof Eraser)) this.savePreviousShapes();
        this.shapes.add(this.currentShape);
    }

    @SneakyThrows
    private void validateImageShape() {
        if (this.currentShape instanceof ImageShape) {
            File file = requireNonNull(Constant.IMAGE_FILE_CHOOSER.getSelectedFile());
            ((ImageShape) this.currentShape).setImage(ImageIO.read(file));
        }
    }

    private void setCurrentDrawingState() {
        switch (this.currentShape.getPointCount()) {
            case TWO:
                this.currentDrawingState = DrawingState.TWO_POINT_DRAWING;
                return;
            case N:
                this.currentDrawingState = DrawingState.N_POINT_DRAWING;
        }
    }

    private void keepDrawing(Point point) {
        this.currentShape.resize(point);
        super.repaint();
    }

    private void finishDrawing() {
        this.currentDrawingState = DrawingState.IDLE;
        this.currentShape.setSelected(true);
        this.eraseShapes();
        super.setCursor(CursorConstant.DEFAULT.getCursor());
        super.repaint();
    }

    private void eraseShapes() {
        this.shapes.stream()
                .filter(shape -> shape instanceof Eraser)
                .map(shape -> shape.getShape().getBounds2D())
                .findFirst()
                .ifPresent(bounds -> this.shapes.removeAll(this.shapes.stream()
                        .filter(shape -> shape.getShape().intersects(bounds)).collect(Collectors.toList())));
        List<CustomShape> erasers = this.shapes.stream().filter(shape -> shape instanceof Eraser).collect(Collectors.toList());
        if (erasers.size() > 0) this.shapes.removeAll(erasers);
    }

    private void addPoint(Point point) {
        this.currentShape.addPoint(point);
    }

    private void changeCursor(Point point) {
        String actionCommand = this.toolBar.getSelectedButton().getActionCommand();
        if (actionCommand.equals(CustomShape.ToolAttributeType.ERASER.name())) {
            super.setCursor(CursorConstant.ERASER.getCursor());
        } else if (this.isCursorOnAnchor(point)) {
            super.setCursor(this.onShape.isAnchorContainsThenReturnCursor(point).orElse(CursorConstant.CROSS.getCursor()));
        } else if (this.isCursorOnShape(point) && (
                actionCommand.equals(CustomShape.ToolAttributeType.SINGLE_SELECTION.name()) ||
                        actionCommand.equals(CustomShape.ToolAttributeType.MULTI_SELECTION.name())
        )) {
            super.setCursor(CursorConstant.HAND.getCursor());
        } else {
            super.setCursor(CursorConstant.CROSS.getCursor());
            this.currentDrawingState = DrawingState.IDLE;
        }
    }

    private boolean isCursorOnShape(Point point) {
        for (int i = this.shapes.size() - 1; i > -1; i--) {
            if (this.shapes.get(i).contains(point)) {
                this.onShape = this.shapes.get(i);
                if (this.shapes.get(i).isSelected()) this.currentDrawingState = DrawingState.MOVE;
                else this.currentDrawingState = DrawingState.IDLE;
                return true;
            }
        }
        return false;
    }

    private boolean isCursorOnAnchor(Point point) {
        for (CustomShape shape : this.shapes) {
            if (shape.isSelected() && shape.containsAnchor(point)) {
                this.validateCursorOnRotateAnchorOrResizeAnchor(point, shape);
                shape.getSelectedAnchor().ifPresent(anchor -> this.selectedAnchorType = anchor.getAnchorType());
                this.onShape = shape;
                return true;
            }
        }
        return false;
    }

    private void validateCursorOnRotateAnchorOrResizeAnchor(Point point, CustomShape shape) {
        shape.getContainsAnchor(point).ifPresent(a ->
                this.currentDrawingState = a.getAnchorType() == Anchor.AnchorType.ROTATE ?
                        DrawingState.ROTATE : DrawingState.RESIZE);
    }

    private void selectSingleShape(Point point) {
        this.changeCursor(point);
        if (this.currentDrawingState != DrawingState.RESIZE && this.isCursorOnShape(point)) {
            this.currentDrawingState = DrawingState.MOVE;
            this.allShapesSetSelectedFalse();
            this.onShape.setSelected(true);
            this.onShape.initMovePoint(point);
            super.repaint();
        }
    }

    private void finishSelectSingleShape() {
        this.currentDrawingState = DrawingState.IDLE;
        this.getSelectedShapesStream().forEach(shape -> shape.setTransforming(false));
        this.savePreviousShapes();
        super.repaint();
    }

    public Stream<CustomShape> getSelectedShapesStream() {
        return this.shapes.stream().filter(CustomShape::isSelected);
    }

    public void allShapesSetSelectedFalse() {
        this.getSelectedShapesStream().forEach(shape -> shape.setSelected(false));
    }

    private void selectMultipleShapes(Point point) {
        this.currentDrawingState = DrawingState.MULTIPLE_SELECTION;
        super.setCursor(CursorConstant.CROSS.getCursor());
        this.multipleShapeSelector.initPoint(point);
        this.savePreviousShapes();
        this.shapes.add(this.multipleShapeSelector);
    }

    private void keepSelectMultipleShapes(Point point) {
        this.multipleShapeSelector.resize(point);
        super.repaint();
    }

    private void finishSelectMultipleShapes() {
        this.shapes.remove(this.multipleShapeSelector);
        this.finishSelectSingleShape();
        this.allShapesSetSelectedFalse();
        this.multipleShapeSelector.setContainsShapesSelectedTrue(this.shapes);
        super.setCursor(CursorConstant.DEFAULT.getCursor());
        super.repaint();
    }

    private void moveShape(Point point) {
        this.getSelectedShapesStream()
                .peek(shape -> shape.setTransforming(true))
                .forEach(shape -> shape.move(point));
        super.repaint();
    }

    private void rotateShape(Point point) {
        this.getSelectedShapesStream()
                .peek(shape -> shape.setTransforming(true))
                .forEach(shape -> shape.rotate(point));
        super.repaint();
    }

    private void scaleShape(Point point) {
        this.getSelectedShapesStream()
                .peek(shape -> shape.setTransforming(true))
                .forEach(shape -> shape.scale(point, this.selectedAnchorType));
        super.repaint();
    }

    public void selectAllShapes() {
        this.shapes.forEach(shape -> shape.setSelected(true));
        super.repaint();
    }

    public void deselectAllShapes() {
        this.shapes.forEach(shape -> shape.setSelected(false));
        super.repaint();
    }

    public void bringToForwardShape() {
        if (this.shapes.size() < 1) return;
        this.savePreviousShapes();
        List<CustomShape> selectedShapes = this.getSelectedShapesStream().collect(Collectors.toList());
        this.shapes.removeAll(selectedShapes);
        this.shapes.addAll(selectedShapes);
        super.repaint();
    }

    public void savePreviousShapes() {
        this.shapeMemory.getPreviousShapes().push(this.shapes.stream().map(SerializationUtils::clone).collect(Collectors.toList()));
    }

    public void bringToFront() {
        if (this.shapes.size() < 1) return;
        List<Integer> indices = new ArrayList<>();
        List<CustomShape> selectedShapes = this.getSelectedShapesStream().collect(Collectors.toList());
        selectedShapes.forEach(shape -> indices.add(this.shapes.indexOf(shape)));
        indices.forEach(index -> {
            if (index != this.shapes.size() - 1) Collections.swap(this.shapes, index + 1, index);
        });
        super.repaint();
    }

    public void sendToBack() {
        if (this.shapes.size() < 1) return;
        List<Integer> indices = new ArrayList<>();
        List<CustomShape> selectedShapes = this.getSelectedShapesStream().collect(Collectors.toList());
        selectedShapes.forEach(shape -> indices.add(this.shapes.indexOf(shape)));
        indices.forEach(index -> {
            if (index != 0) Collections.swap(this.shapes, index - 1, index);
        });
        super.repaint();
    }

    public void sendToBackwardShape() {
        if (this.shapes.size() < 1) return;
        List<CustomShape> selectedShapes = this.getSelectedShapesStream().collect(Collectors.toList());
        this.shapes.removeAll(selectedShapes);
        this.shapes.addAll(0, selectedShapes);
        super.repaint();
    }

    private class DrawingPanelMouseEventHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) leftButtonClick(e);
            else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) leftButtonDoubleClick(e);
        }

        private void leftButtonClick(MouseEvent e) {
            if (currentDrawingState == DrawingState.N_POINT_DRAWING) {
                addPoint(e.getPoint());
            }
        }

        private void leftButtonDoubleClick(MouseEvent e) {
            if (currentDrawingState == DrawingState.N_POINT_DRAWING) {
                finishDrawing();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            String actionCommand = toolBar.getSelectedButton().getActionCommand();
            if (actionCommand.equals(CustomShape.ToolAttributeType.SINGLE_SELECTION.name()) && currentDrawingState != DrawingState.ROTATE) {
                selectSingleShape(e.getPoint());
            } else if (currentDrawingState == DrawingState.IDLE) {
                if (actionCommand.equals(CustomShape.ToolAttributeType.MULTI_SELECTION.name())) {
                    selectMultipleShapes(e.getPoint());
                } else prepareDrawing(e.getPoint());
            }
            getSelectedShapesStream().forEach(shape -> shape.initMovePoint(e.getPoint()));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (currentDrawingState == DrawingState.TWO_POINT_DRAWING) {
                keepDrawing(e.getPoint());
            } else if (currentDrawingState == DrawingState.MOVE) {
                moveShape(e.getPoint());
            } else if (currentDrawingState == DrawingState.ROTATE) {
                rotateShape(e.getPoint());
            } else if (currentDrawingState == DrawingState.RESIZE) {
                scaleShape(e.getPoint());
            } else if (currentDrawingState == DrawingState.MULTIPLE_SELECTION) {
                keepSelectMultipleShapes(e.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (currentDrawingState == DrawingState.TWO_POINT_DRAWING) finishDrawing();
            else if (currentDrawingState == DrawingState.MOVE || currentDrawingState == DrawingState.ROTATE || currentDrawingState == DrawingState.RESIZE)
                finishSelectSingleShape();
            else if (currentDrawingState == DrawingState.MULTIPLE_SELECTION) finishSelectMultipleShapes();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (currentDrawingState == DrawingState.N_POINT_DRAWING) keepDrawing(e.getPoint());
            else changeCursor(e.getPoint());
        }
    }
}
