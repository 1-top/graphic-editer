package com.chocobi.paint.shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ImageShape extends CustomShape {
    private transient BufferedImage bufferedImage;

    public ImageShape(Point point) {
        super(PointCount.TWO, point, new Rectangle2D.Double());
        this.boundsRectangle = new Rectangle();
        ((Rectangle2D.Double) super.shape).setRect(point.getX(), point.getY(), 0, 0);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(this.bufferedImage, super.boundsRectangle.x, super.boundsRectangle.y, super.boundsRectangle.width, super.boundsRectangle.height, null);
        super.drawAnchor(graphics2D);
    }

    @Override
    public void resize(Point point) {
        ((Rectangle2D.Double) super.shape).setRect(
                Math.min(super.firstPoint.x, point.x),
                Math.min(super.firstPoint.y, point.y),
                Math.abs(super.firstPoint.x - point.x),
                Math.abs(super.firstPoint.y - point.y)
        );
        super.boundsRectangle.setRect(
                Math.min(super.firstPoint.x, point.x),
                Math.min(super.firstPoint.y, point.y),
                Math.abs(super.firstPoint.x - point.x),
                Math.abs(super.firstPoint.y - point.y)
        );
    }

    public void setImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
