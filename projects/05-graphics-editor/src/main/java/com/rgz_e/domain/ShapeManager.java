package com.rgz_e.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Менеджер графических объектов.
 * Управляет коллекцией всех графических объектов на холсте.
 */
public class ShapeManager {
    private final List<Shape> shapes;
    private Shape selectedShape;
    
    public ShapeManager() {
        this.shapes = new ArrayList<>();
        this.selectedShape = null;
    }
    
    /**
     * Добавляет объект в коллекцию.
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }
    
    /**
     * Удаляет объект из коллекции.
     */
    public boolean removeShape(Shape shape) {
        if (shape == selectedShape) {
            selectedShape = null;
        }
        return shapes.remove(shape);
    }
    
    /**
     * Удаляет объект по ID.
     */
    public boolean removeShapeById(String id) {
        Shape shape = findShapeById(id);
        if (shape != null) {
            return removeShape(shape);
        }
        return false;
    }
    
    /**
     * Находит объект по ID.
     */
    public Shape findShapeById(String id) {
        return shapes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Находит объект по координатам (с учетом порядка отрисовки - сверху вниз).
     */
    public Shape findShapeAt(double x, double y) {
        // Поиск с конца списка (верхние объекты)
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }
    
    /**
     * Получает все объекты.
     */
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }
    
    /**
     * Очищает все объекты.
     */
    public void clear() {
        shapes.clear();
        selectedShape = null;
    }
    
    /**
     * Выделяет объект (снимает выделение с предыдущего).
     */
    public void selectShape(Shape shape) {
        if (selectedShape != null) {
            selectedShape.setSelected(false);
        }
        selectedShape = shape;
        if (shape != null) {
            shape.setSelected(true);
        }
    }
    
    /**
     * Снимает выделение.
     */
    public void deselectAll() {
        if (selectedShape != null) {
            selectedShape.setSelected(false);
            selectedShape = null;
        }
    }
    
    /**
     * Получает выделенный объект.
     */
    public Shape getSelectedShape() {
        return selectedShape;
    }
    
    /**
     * Перемещает объект на передний план (в конец списка).
     */
    public void bringToFront(Shape shape) {
        if (shapes.remove(shape)) {
            shapes.add(shape);
        }
    }
    
    /**
     * Перемещает объект на задний план (в начало списка).
     */
    public void sendToBack(Shape shape) {
        if (shapes.remove(shape)) {
            shapes.add(0, shape);
        }
    }
    
    /**
     * Получает индекс объекта в списке.
     */
    public int getShapeIndex(Shape shape) {
        return shapes.indexOf(shape);
    }
    
    /**
     * Восстанавливает порядок объектов из списка.
     */
    public void restoreOrder(List<Shape> orderedShapes) {
        shapes.clear();
        shapes.addAll(orderedShapes);
    }
    
    /**
     * Создает глубокую копию состояния менеджера.
     */
    public ShapeManager copy() {
        ShapeManager copy = new ShapeManager();
        for (Shape shape : shapes) {
            copy.addShape(shape.copy());
        }
        if (selectedShape != null) {
            copy.selectShape(copy.findShapeById(selectedShape.getId()));
        }
        return copy;
    }
    
    /**
     * Восстанавливает состояние из копии.
     */
    public void restore(ShapeManager other) {
        this.shapes.clear();
        this.shapes.addAll(other.shapes.stream()
                .map(Shape::copy)
                .collect(Collectors.toList()));
        if (other.selectedShape != null) {
            this.selectedShape = findShapeById(other.selectedShape.getId());
        } else {
            this.selectedShape = null;
        }
    }
}

