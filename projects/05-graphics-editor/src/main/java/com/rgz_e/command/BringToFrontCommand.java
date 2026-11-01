package com.rgz_e.command;

import com.rgz_e.domain.Shape;
import com.rgz_e.domain.ShapeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда перемещения объекта на передний план.
 */
public class BringToFrontCommand implements Command {
    private final ShapeManager shapeManager;
    private final String shapeId;
    private List<Shape> originalOrder;
    
    public BringToFrontCommand(ShapeManager shapeManager, String shapeId) {
        this.shapeManager = shapeManager;
        this.shapeId = shapeId;
    }
    
    @Override
    public void execute() {
        Shape shape = shapeManager.findShapeById(shapeId);
        if (shape != null) {
            // Сохраняем порядок только при первом выполнении
            if (originalOrder == null) {
                originalOrder = new ArrayList<>(shapeManager.getShapes());
            }
            shapeManager.bringToFront(shape);
        }
    }
    
    @Override
    public void undo() {
        if (originalOrder != null) {
            shapeManager.restoreOrder(originalOrder);
        }
    }
    
    @Override
    public String getDescription() {
        return "Bring to Front";
    }
}

