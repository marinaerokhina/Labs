package com.rgz_e.domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Класс прямоугольника.
 */
public class Rectangle extends Shape {
    
    public Rectangle(double x, double y, double width, double height, Color fillColor) {
        super(x, y, width, height, fillColor);
    }
    
    public Rectangle(Rectangle other) {
        super(other);
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        // Отрисовка заливки
        gc.setFill(fillColor);
        gc.fillRect(x, y, width, height);
        
        // Отрисовка контура
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokeRect(x, y, width, height);
        
        // Отрисовка выделения
        drawSelection(gc);
    }
    
    @Override
    protected void drawBounds(GraphicsContext gc) {
        gc.strokeRect(x - 1, y - 1, width + 2, height + 2);
    }
    
    @Override
    protected double[] getResizeHandlePositions() {
        return new double[]{
            x, y,                    // Левый верхний
            x + width, y,            // Правый верхний
            x, y + height,           // Левый нижний
            x + width, y + height    // Правый нижний
        };
    }
    
    @Override
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
    
    @Override
    public int isInResizeArea(double px, double py) {
        if (!selected) return 0;
        
        double tolerance = RESIZE_HANDLE_SIZE;
        double[] handles = getResizeHandlePositions();
        
        for (int i = 0; i < handles.length; i += 2) {
            double hx = handles[i];
            double hy = handles[i + 1];
            if (Math.abs(px - hx) < tolerance && Math.abs(py - hy) < tolerance) {
                return (i / 2) + 1; // 1-4
            }
        }
        
        return 0;
    }
    
    @Override
    public Shape copy() {
        return new Rectangle(this);
    }
    
    @Override
    public String getShapeType() {
        return "Rectangle";
    }
}

