package com.rgz_e.command;

import com.rgz_e.domain.Shape;
import com.rgz_e.domain.ShapeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда удаления прямоугольника.
 */
public class DeleteShapeCommand implements Command {
    private final ShapeManager shapeManager;
    private final String shapeId;
    private Shape deletedShape;
    private List<Shape> originalOrder;
    
    public DeleteShapeCommand(ShapeManager shapeManager, String shapeId) {
        this.shapeManager = shapeManager;
        this.shapeId = shapeId;
    }
    
    @Override
    public void execute() {
        deletedShape = shapeManager.findShapeById(shapeId);
        if (deletedShape != null) {
            shapeManager.getShapeIndex(deletedShape);
            originalOrder = new ArrayList<>(shapeManager.getShapes());
            shapeManager.removeShape(deletedShape);
            shapeManager.deselectAll();
        }
    }
    
    @Override
    public void undo() {
        if (deletedShape != null && originalOrder != null) {
            // Восстанавливаем порядок (originalOrder уже содержит удаленный объект)
            shapeManager.restoreOrder(originalOrder);
            // Восстанавливаем выделение
            shapeManager.selectShape(deletedShape);
        }
    }
    
    @Override
    public String getDescription() {
        return "Delete Rectangle";
    }
}

