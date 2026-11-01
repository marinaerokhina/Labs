package com.rgz_e.domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Класс треугольника.
 */
public class Triangle extends Shape {
    
    public Triangle(double x, double y, double width, double height, Color fillColor) {
        super(x, y, width, height, fillColor);
    }
    
    public Triangle(Triangle other) {
        super(other);
    }
    
    /**
     * Получает координаты вершин треугольника.
     * @return массив из 6 элементов [x1, y1, x2, y2, x3, y3]
     */
    private double[] getVertices() {
        return new double[]{
            x + width / 2.0, y,              // Верхняя вершина
            x, y + height,                    // Левая нижняя вершина
            x + width, y + height            // Правая нижняя вершина
        };
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        double[] vertices = getVertices();
        
        // Отрисовка заливки
        gc.setFill(fillColor);
        gc.fillPolygon(
            new double[]{vertices[0], vertices[2], vertices[4]},
            new double[]{vertices[1], vertices[3], vertices[5]},
            3
        );
        
        // Отрисовка контура
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokePolygon(
            new double[]{vertices[0], vertices[2], vertices[4]},
            new double[]{vertices[1], vertices[3], vertices[5]},
            3
        );
        
        // Отрисовка выделения
        drawSelection(gc);
    }
    
    @Override
    protected void drawSelectionFrame(GraphicsContext gc) {
        double[] vertices = getVertices();
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2.0);
        gc.strokePolygon(
            new double[]{vertices[0], vertices[2], vertices[4]},
            new double[]{vertices[1], vertices[3], vertices[5]},
            3
        );
    }
    
    @Override
    protected void drawBounds(GraphicsContext gc) {
        drawSelectionFrame(gc);
    }
    
    @Override
    protected double[] getResizeHandlePositions() {
        double[] vertices = getVertices();
        return new double[]{
            vertices[0], vertices[1],  // Верхняя вершина
            vertices[2], vertices[3],  // Левая нижняя вершина
            vertices[4], vertices[5]   // Правая нижняя вершина
        };
    }
    
    @Override
    public boolean contains(double px, double py) {
        double[] vertices = getVertices();
        double v0x = vertices[0], v0y = vertices[1];
        double v1x = vertices[2], v1y = vertices[3];
        double v2x = vertices[4], v2y = vertices[5];
        
        // Проверка попадания точки в треугольник методом барицентрических координат
        double denom = (v1y - v2y) * (v0x - v2x) + (v2x - v1x) * (v0y - v2y);
        if (Math.abs(denom) < 1e-10) return false;
        
        double a = ((v1y - v2y) * (px - v2x) + (v2x - v1x) * (py - v2y)) / denom;
        double b = ((v2y - v0y) * (px - v2x) + (v0x - v2x) * (py - v2y)) / denom;
        double c = 1 - a - b;
        
        return a >= 0 && b >= 0 && c >= 0;
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
                return (i / 2) + 1; // 1-3
            }
        }
        
        return 0;
    }
    
    @Override
    public Shape copy() {
        return new Triangle(this);
    }
    
    @Override
    public String getShapeType() {
        return "Triangle";
    }
}

