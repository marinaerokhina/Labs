package com.rgz_e.presentation;

import com.rgz_e.domain.Shape;
import com.rgz_e.domain.ShapeManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Специализированный компонент Canvas для отрисовки графических объектов.
 */
public class DrawingCanvas extends Canvas {
    private final ShapeManager shapeManager;
    private boolean showGrid = true;
    private static final double GRID_SIZE = 20.0;
    
    public DrawingCanvas(ShapeManager shapeManager, double width, double height) {
        super(width, height);
        this.shapeManager = shapeManager;
        
        // Автоматическая перерисовка при изменении размеров
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());
    }
    
    /**
     * Перерисовывает весь холст.
     */
    public void redraw() {
        GraphicsContext gc = getGraphicsContext2D();
        
        // Очистка
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        
        // Отрисовка сетки
        if (showGrid) {
            drawGrid(gc);
        }
        
        // Отрисовка всех объектов
        for (Shape shape : shapeManager.getShapes()) {
            shape.draw(gc);
        }
    }
    
    /**
     * Отрисовка сетки.
     */
    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        
        double width = getWidth();
        double height = getHeight();
        
        // Вертикальные линии
        for (double x = 0; x <= width; x += GRID_SIZE) {
            gc.strokeLine(x, 0, x, height);
        }
        
        // Горизонтальные линии
        for (double y = 0; y <= height; y += GRID_SIZE) {
            gc.strokeLine(0, y, width, y);
        }
    }
    
    /**
     * Включает/выключает отображение сетки.
     */
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        redraw();
    }
    
    public boolean isShowGrid() {
        return showGrid;
    }
}

