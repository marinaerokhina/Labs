package com.rgz_e.command;

import com.rgz_e.domain.*;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Команда создания новой фигуры.
 */
public class CreateShapeCommand implements Command {
    public enum ShapeType {
        RECTANGLE, CIRCLE, TRIANGLE
    }
    
    private final ShapeManager shapeManager;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final ShapeType shapeType;
    private Shape createdShape;
    private Color shapeColor; // Сохраняем цвет для корректного redo
    
    private static final Random random = new Random();
    
    public CreateShapeCommand(ShapeManager shapeManager, ShapeType shapeType, double x, double y, double width, double height) {
        this.shapeManager = shapeManager;
        this.shapeType = shapeType;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void execute() {
        // Генерация случайного цвета только при первом выполнении
        if (shapeColor == null) {
            shapeColor = Color.color(
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble()
            );
        }
        
        // Создаем объект только если он еще не создан
        if (createdShape == null) {
            createdShape = createShape(shapeType, x, y, width, height, shapeColor);
        }
        
        shapeManager.addShape(createdShape);
        shapeManager.selectShape(createdShape);
    }
    
    private Shape createShape(ShapeType type, double x, double y, double width, double height, Color color) {
        switch (type) {
            case RECTANGLE:
                return new Rectangle(x, y, width, height, color);
            case CIRCLE:
                return new Circle(x, y, width, height, color);
            case TRIANGLE:
                return new Triangle(x, y, width, height, color);
            default:
                throw new IllegalArgumentException("Unknown shape type: " + type);
        }
    }
    
    @Override
    public void undo() {
        if (createdShape != null) {
            shapeManager.removeShape(createdShape);
            shapeManager.deselectAll();
        }
    }
    
    @Override
    public String getDescription() {
        return "Create " + (createdShape != null ? createdShape.getShapeType() : shapeType.name());
    }
}

