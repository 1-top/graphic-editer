package com.chocobi.paint.shape;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
public abstract class CustomShape implements Serializable {
    public enum PointCount {
        TWO, N
    }

    public enum ToolAttributeType {
        ONLY_LINE, LINE_AND_FILL, SINGLE_SELECTION, MULTI_SELECTION, ERASER, IMAGE
    }

    protected PointCount pointCount;
    protected Point firstPoint;
    protected Color lineColor = Color.BLACK;
    protected Color fillColor = new Color(0f, 0f, 0f, 0f);
    protected int stroke;
    protected boolean selected;
    protected boolean transforming;

    protected transient Optional<Anchor> selectedAnchor = Optional.empty();
    protected List<Anchor> anchors = new ArrayList<>();
    protected Shape shape;
    protected Rectangle boundsRectangle;

    protected Point previousMovePoint = new Point();
    protected Point centerPoint = new Point();

    public CustomShape(PointCount pointCount, Point firstPoint, Shape shape) {
        this.pointCount = pointCount;
        this.firstPoint = firstPoint;
        this.shape = shape;
        for (Anchor.AnchorType anchorType : Anchor.AnchorType.values()) {
            this.anchors.add(new Anchor(anchorType));
        }
    }

    public abstract void resize(Point point);

    public void draw(Graphics2D graphics2D) {
        this.drawFillColor(graphics2D);
        this.drawLineColor(graphics2D);
        this.drawAnchor(graphics2D);
    }

    protected void drawAnchor(Graphics2D graphics2D) {
        if (this.selected && !this.transforming) this.anchors.forEach(anchor -> anchor.draw(graphics2D, this.shape.getBounds2D()));
    }

    protected void drawFillColor(Graphics2D graphics2D) {
        graphics2D.setColor(this.fillColor);
        graphics2D.fill(this.shape);
    }

    protected void drawLineColor(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(this.stroke));
        graphics2D.setColor(this.lineColor);
        graphics2D.draw(this.shape);
    }

    public void addPoint(Point point) {
    }

    public boolean contains(Point point) {
        if (this.shape instanceof Line2D || this.shape instanceof Path2D)
            return this.shape.intersects(point.getX(), point.getY(), 3, 3);
        return this.shape.contains(point.getX(), point.getY());
    }

    public boolean contains(Rectangle2D selectBoundsRectangle) {
        if (this.shape instanceof Line2D || this.shape instanceof Path2D)
            return this.shape.intersects(selectBoundsRectangle);
        return selectBoundsRectangle.contains(this.shape.getBounds2D());
    }

    public boolean containsAnchor(Point point) {
        for (Anchor anchor : this.anchors) {
            if (anchor.contains(point)) {
                this.selectedAnchor = Optional.of(anchor);
                return true;
            }
        }
        this.selectedAnchor = Optional.empty();
        return false;
    }

    public Optional<Cursor> isAnchorContainsThenReturnCursor(Point point) {
        for (Anchor anchor : this.anchors)
            if (anchor.contains(point)) return Optional.of(anchor.getAnchorType().getCursor());
        return Optional.empty();
    }

    public Optional<Anchor> getContainsAnchor(Point point) {
        for (Anchor anchor : this.anchors)
            if (anchor.contains(point)) return Optional.of(anchor);
        return Optional.empty();
    }

    public void initMovePoint(Point point) {
        this.previousMovePoint.setLocation(point);
        this.resetCenterPoint();
    }

    public void resetCenterPoint() {
        this.centerPoint.setLocation(this.shape.getBounds2D().getCenterX(), this.shape.getBounds2D().getCenterY());
    }

    public void move(Point point) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(point.getX() - this.previousMovePoint.getX(), point.getY() - this.previousMovePoint.getY());
        this.shape = affineTransform.createTransformedShape(this.shape);
        this.boundsRectangle = this.shape.getBounds();
        this.previousMovePoint.setLocation(point);
    }

    public void rotate(Point point) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToRotation(Math.toRadians(this.calculateAngle(point)), centerPoint.getX(), centerPoint.getY());
        this.shape = affineTransform.createTransformedShape(this.shape);
        this.boundsRectangle = this.shape.getBounds();
    }

    private double calculateAngle(Point endPoint) {
        double startAngle = Math.toDegrees(Math.atan2(
                this.centerPoint.getX() - this.previousMovePoint.getX(),
                this.centerPoint.getY() - this.previousMovePoint.getY()));
        double endAngle = Math.toDegrees(Math.atan2(
                this.centerPoint.getX() - endPoint.getX(),
                this.centerPoint.getY() - endPoint.getY()));
        double angle = startAngle - endAngle;
        if (angle < 0) angle += 360;
        this.previousMovePoint.setLocation(endPoint);
        return angle;
    }

    public void scale(Point point, Anchor.AnchorType anchorType) {
        AffineTransform affineTransform = new AffineTransform();
        Rectangle bound = this.shape.getBounds();

        double dx = (point.x - this.previousMovePoint.x) / bound.getWidth();
        double dy = (point.y - this.previousMovePoint.y) / bound.getHeight();

        switch (anchorType) {
            case NORTH_WEST:
                affineTransform.setToTranslation(bound.getMinX() + bound.getWidth(), bound.getMinY() + bound.getHeight());
                affineTransform.scale(1 - dx, 1 - dy);
                affineTransform.translate(-(bound.getMinX() + bound.getWidth()), -(bound.getMinY() + bound.getHeight()));
                break;
            case NORTH:
                affineTransform.setToTranslation(0, bound.getMinY() + bound.getHeight());
                affineTransform.scale(1, 1 - dy);
                affineTransform.translate(0, -(bound.getMinY() + bound.getHeight()));
                break;
            case NORTH_EAST:
                affineTransform.setToTranslation(bound.getMinX(), bound.getMinY() + bound.getHeight());
                affineTransform.scale(1 + dx, 1 - dy);
                affineTransform.translate(-(bound.getMinX()), -(bound.getMinY() + bound.getHeight()));
                break;
            case EAST:
                affineTransform.setToTranslation(bound.getMinX(), 0);
                affineTransform.scale(1 + dx, 1);
                affineTransform.translate(-(bound.getMinX()), 0);
                break;
            case SOUTH_EAST:
                affineTransform.setToTranslation(bound.getMinX(), bound.getMinY());
                affineTransform.scale(1 + dx, 1 + dy);
                affineTransform.translate(-(bound.getMinX()), -(bound.getMinY()));
                break;
            case SOUTH:
                affineTransform.setToTranslation(0, bound.getMinY());
                affineTransform.scale(1, 1 + dy);
                affineTransform.translate(0, -(bound.getMinY()));
                break;
            case SOUTH_WEST:
                affineTransform.setToTranslation(bound.getMinX() + bound.getWidth(), bound.getMinY());
                affineTransform.scale(1 - dx, 1 + dy);
                affineTransform.translate(-(bound.getMinX() + bound.getWidth()), -(bound.getMinY()));
                break;
            case WEST:
                affineTransform.setToTranslation(bound.getMinX() + bound.getWidth(), 0);
                affineTransform.scale(1 - dx, 1);
                affineTransform.translate(-(bound.getMinX() + bound.getWidth()), 0);
                break;
        }

        this.shape = affineTransform.createTransformedShape(this.shape);
        this.boundsRectangle = this.shape.getBounds();
        this.previousMovePoint.setLocation(point);
    }
}
