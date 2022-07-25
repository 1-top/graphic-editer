package com.chocobi.paint.shape;

import com.chocobi.paint.global.constant.CursorConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class Anchor implements Serializable {
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;

    @Getter
    @RequiredArgsConstructor
    public enum AnchorType {
        NORTH_WEST(CursorConstant.NORTH_WEST_RESIZE.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX(), boundsRectangle.getY());
                return this.getPoint();
            }
        },
        NORTH(CursorConstant.NORTH_RESIZE.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX() + (boundsRectangle.getWidth() / 2), boundsRectangle.getY());
                return this.getPoint();
            }
        },
        NORTH_EAST(CursorConstant.NORTH_EAST_RESIZE.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX() + boundsRectangle.getWidth(), boundsRectangle.getY());
                return this.getPoint();
            }
        },
        EAST(CursorConstant.EAST_RESIZE.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX() + boundsRectangle.getWidth(), boundsRectangle.getY() + (boundsRectangle.getHeight() / 2));
                return this.getPoint();
            }
        },
        SOUTH_EAST(CursorConstant.SOUTH_EAST.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX() + boundsRectangle.getWidth(), boundsRectangle.getY() + boundsRectangle.getHeight());
                return this.getPoint();
            }
        },
        SOUTH(CursorConstant.SOUTH.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX() + (boundsRectangle.getWidth() / 2), boundsRectangle.getY() + boundsRectangle.getHeight());
                return this.getPoint();
            }
        },
        SOUTH_WEST(CursorConstant.SOUTH_WEST.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX(), boundsRectangle.getY() + boundsRectangle.getHeight());
                return this.getPoint();
            }
        },
        WEST(CursorConstant.WEST.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX(), boundsRectangle.getY() + (boundsRectangle.getHeight() / 2));
                return this.getPoint();
            }
        },
        ROTATE(CursorConstant.ROTATE.getCursor()) {
            @Override
            public Point2D calculatePoint(Rectangle2D boundsRectangle) {
                this.getPoint().setLocation(boundsRectangle.getX() + (boundsRectangle.getWidth() / 2), boundsRectangle.getY() - 30);
                return this.getPoint();
            }
        };

        private final Cursor cursor;
        private final Point2D point = new Point2D.Double();

        public abstract Point2D calculatePoint(Rectangle2D boundsRectangle);
    }

    private final AnchorType anchorType;
    private final Rectangle2D shape = new Rectangle2D.Double();
    private final Color fillColor = Color.WHITE;
    private final Color lineColor = Color.BLUE;
    private final int stroke = 1;

    public void draw(Graphics2D graphics2D, Rectangle2D boundsRectangle) {
        Point2D point2D = this.getAnchorType().calculatePoint(boundsRectangle);
        this.shape.setFrame(point2D.getX() - Integer.divideUnsigned(WIDTH, 2), point2D.getY() - Integer.divideUnsigned(HEIGHT, 2), WIDTH, HEIGHT);
        this.drawFillColor(graphics2D);
        this.drawLineColor(graphics2D);
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

    public boolean contains(Point point) {
        return this.shape.contains(point);
    }
}
