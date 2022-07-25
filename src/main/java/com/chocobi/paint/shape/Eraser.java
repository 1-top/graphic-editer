package com.chocobi.paint.shape;

import java.awt.*;

public class Eraser extends CustomPencil {
    public Eraser(Point firstPoint) {
        super(firstPoint);
        super.lineColor = new Color(0f, 0f, 0f, 0.4f);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        this.drawLineColor(graphics2D);
    }

    @Override
    protected void drawLineColor(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(this.stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.1f));
        graphics2D.setColor(this.lineColor);
        graphics2D.draw(this.shape);
    }

    @Override
    public void setLineColor(Color lineColor) {
    }
}
