package com.rgz_e.domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Класс круга.
 */
public class Circle extends Shape {
    
    public Circle(double x, double y, double width, double height, Color fillColor) {
        super(x, y, width, height, fillColor);
    }
    
    public Circle(Circle other) {
        super(other);
    }
    
    /**
     * Получает радиус круга (используется меньшая из сторон).
     */
    private double getRadius() {
        return Math.min(width, height) / 2.0;
    }
    
    /**
     * Получает центр круга по X.
     */
    private double getCenterX() {
        return x + width / 2.0;
    }
    
    /**
     * Получает центр круга по Y.
     */
    private double getCenterY() {
        return y + height / 2.0;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        // Отрисовка заливки
        gc.setFill(fillColor);
        gc.fillOval(x, y, width, height);
        
        // Отрисовка контура
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokeOval(x, y, width, height);
        
        // Отрисовка выделения
        drawSelection(gc);
    }
    
    @Override
    protected void drawSelectionFrame(GraphicsContext gc) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2.0);
        gc.strokeOval(x - 1, y - 1, width + 2, height + 2);
    }
    
    @Override
    protected void drawBounds(GraphicsContext gc) {
        gc.strokeOval(x - 1, y - 1, width + 2, height + 2);
    }
    
    @Override
    protected double[] getResizeHandlePositions() {
        double centerX = getCenterX();
        double centerY = getCenterY();
        double radius = getRadius();
        
        return new double[]{
            centerX - radius, centerY - radius,  // Верхний
            centerX + radius, centerY - radius,   // Правый верхний
            centerX + radius, centerY + radius,   // Правый нижний
            centerX - radius, centerY + radius    // Левый нижний
        };
    }
    
    @Override
    public boolean contains(double px, double py) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        double radius = getRadius();
        double dx = px - centerX;
        double dy = py - centerY;
        return (dx * dx + dy * dy) <= radius * radius;
    }
    
    @Override
    public int isInResizeArea(double px, double py) {
        if (!selected) return 0;
        
        double tolerance = RESIZE_HANDLE_SIZE;
        double[] handles = getResizeHandlePositions();
        
        // Используем евклидово расстояние для более точной проверки
        for (int i = 0; i < handles.length; i += 2) {
            double hx = handles[i];
            double hy = handles[i + 1];
            double dx = px - hx;
            double dy = py - hy;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < tolerance) {
                return (i / 2) + 1; // 1-4
            }
        }
        
        return 0;
    }
    
    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        // Для круга поддерживаем квадратную форму
        if (this.height != width) {
            this.height = width;
        }
    }
    
    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        // Для круга поддерживаем квадратную форму
        if (this.width != height) {
            this.width = height;
        }
    }
    
    @Override
    public Shape copy() {
        return new Circle(this);
    }
    
    @Override
    public String getShapeType() {
        return "Circle";
    }
}

