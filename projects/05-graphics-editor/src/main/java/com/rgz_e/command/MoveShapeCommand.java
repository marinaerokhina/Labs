package com.rgz_e.command;

import com.rgz_e.domain.Shape;
import com.rgz_e.domain.ShapeManager;

/**
 * Команда перемещения прямоугольника.
 */
public class MoveShapeCommand implements Command {
    private final ShapeManager shapeManager;
    private final String shapeId;
    private final double oldX;
    private final double oldY;
    private final double newX;
    private final double newY;
    
    public MoveShapeCommand(ShapeManager shapeManager, String shapeId, double oldX, double oldY, double newX, double newY) {
        this.shapeManager = shapeManager;
        this.shapeId = shapeId;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }
    
    @Override
    public void execute() {
        Shape shape = shapeManager.findShapeById(shapeId);
        if (shape != null) {
            shape.setX(newX);
            shape.setY(newY);
        }
    }
    
    @Override
    public void undo() {
        Shape shape = shapeManager.findShapeById(shapeId);
        if (shape != null) {
            shape.setX(oldX);
            shape.setY(oldY);
        }
    }
    
    @Override
    public String getDescription() {
        return "Move Rectangle";
    }
}

