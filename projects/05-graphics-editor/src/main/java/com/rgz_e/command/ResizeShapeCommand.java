package com.rgz_e.command;

import com.rgz_e.domain.Shape;
import com.rgz_e.domain.ShapeManager;

/**
 * Команда изменения размера фигуры.
 */
public class ResizeShapeCommand implements Command {
    private final ShapeManager shapeManager;
    private final String shapeId;
    private final double oldX;
    private final double oldY;
    private final double oldWidth;
    private final double oldHeight;
    private final double newX;
    private final double newY;
    private final double newWidth;
    private final double newHeight;
    
    public ResizeShapeCommand(ShapeManager shapeManager, String shapeId,
                             double oldX, double oldY, double oldWidth, double oldHeight,
                             double newX, double newY, double newWidth, double newHeight) {
        this.shapeManager = shapeManager;
        this.shapeId = shapeId;
        this.oldX = oldX;
        this.oldY = oldY;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newX = newX;
        this.newY = newY;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }
    
    @Override
    public void execute() {
        Shape shape = shapeManager.findShapeById(shapeId);
        if (shape != null) {
            shape.setX(newX);
            shape.setY(newY);
            shape.setWidth(newWidth);
            shape.setHeight(newHeight);
        }
    }
    
    @Override
    public void undo() {
        Shape shape = shapeManager.findShapeById(shapeId);
        if (shape != null) {
            shape.setX(oldX);
            shape.setY(oldY);
            shape.setWidth(oldWidth);
            shape.setHeight(oldHeight);
        }
    }
    
    @Override
    public String getDescription() {
        Shape shape = shapeManager.findShapeById(shapeId);
        if (shape != null) {
            return "Resize " + shape.getShapeType();
        }
        return "Resize Shape";
    }
}

