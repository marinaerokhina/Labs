package com.rgz_e.domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.UUID;

/**
 * Базовый абстрактный класс графического объекта.
 */
public abstract class Shape {
    protected final String id;
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected Color fillColor;
    protected boolean selected;
    
    protected static final double MIN_SIZE = 5.0;
    protected static final double RESIZE_HANDLE_SIZE = 8.0;
    
    public Shape(double x, double y, double width, double height, Color fillColor) {
        this.id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        this.width = Math.max(MIN_SIZE, width);
        this.height = Math.max(MIN_SIZE, height);
        this.fillColor = fillColor;
        this.selected = false;
    }
    
    public Shape(Shape other) {
        this.id = other.id;
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
        this.fillColor = other.fillColor;
        this.selected = other.selected;
    }
    
    /**
     * Отрисовка объекта на графическом контексте.
     */
    public abstract void draw(GraphicsContext gc);
    
    /**
     * Отрисовка маркеров выделения и изменения размера.
     */
    protected void drawSelection(GraphicsContext gc) {
        if (!selected) return;
        
        // Отрисовка рамки выделения
        drawSelectionFrame(gc);
        
        // Отрисовка маркеров изменения размера
        drawResizeHandles(gc);
    }
    
    /**
     * Отрисовка рамки выделения (переопределяется в подклассах при необходимости).
     */
    protected void drawSelectionFrame(GraphicsContext gc) {
        // Базовая реализация для прямоугольных фигур
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2.0);
        drawBounds(gc);
    }
    
    /**
     * Отрисовка границ фигуры (переопределяется в подклассах).
     */
    protected abstract void drawBounds(GraphicsContext gc);
    
    /**
     * Отрисовка маркеров изменения размера.
     */
    protected void drawResizeHandles(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        
        double[] handles = getResizeHandlePositions();
        for (int i = 0; i < handles.length; i += 2) {
            drawResizeHandle(gc, handles[i], handles[i + 1]);
        }
    }
    
    /**
     * Получает позиции маркеров изменения размера.
     * @return массив координат [x1, y1, x2, y2, ...]
     */
    protected abstract double[] getResizeHandlePositions();
    
    protected void drawResizeHandle(GraphicsContext gc, double centerX, double centerY) {
        double halfSize = RESIZE_HANDLE_SIZE / 2.0;
        gc.fillRect(centerX - halfSize, centerY - halfSize, RESIZE_HANDLE_SIZE, RESIZE_HANDLE_SIZE);
        gc.strokeRect(centerX - halfSize, centerY - halfSize, RESIZE_HANDLE_SIZE, RESIZE_HANDLE_SIZE);
    }
    
    /**
     * Проверка попадания точки в границы объекта.
     */
    public abstract boolean contains(double px, double py);
    
    /**
     * Проверка, находится ли точка в области изменения размера.
     * @return 0 - не в области, 1-4 - маркеры изменения размера
     */
    public abstract int isInResizeArea(double px, double py);
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getWidth() {
        return width;
    }
    
    public void setWidth(double width) {
        this.width = Math.max(MIN_SIZE, width);
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = Math.max(MIN_SIZE, height);
    }
    
    public Color getFillColor() {
        return fillColor;
    }
    
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /**
     * Создает копию объекта.
     */
    public abstract Shape copy();
    
    /**
     * Получает тип фигуры для отображения.
     */
    public abstract String getShapeType();
}

