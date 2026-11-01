package com.rgz_e.presentation;

import com.rgz_e.command.*;
import com.rgz_e.domain.Circle;
import com.rgz_e.domain.Shape;
import com.rgz_e.domain.ShapeManager;
import com.rgz_e.domain.Triangle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Основной контроллер приложения.
 * Оркестрирует все взаимодействия между компонентами.
 */
public class MainController implements Initializable {
    @FXML
    private Canvas canvas;
    
    @FXML
    private Button createButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button bringToFrontButton;
    
    @FXML
    private Button sendToBackButton;
    
    @FXML
    private Button undoButton;
    
    @FXML
    private Button redoButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private ComboBox<String> shapeTypeComboBox;
    
    private ShapeManager shapeManager;
    private CommandManager commandManager;
    
    // Состояние перетаскивания
    private enum DragMode {
        NONE, MOVE, RESIZE
    }
    
    private DragMode dragMode = DragMode.NONE;
    private Shape draggedShape;
    private double startX, startY;
    private double shapeStartX, shapeStartY;
    private double shapeStartWidth, shapeStartHeight;
    private int resizeCorner = 0;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Инициализация менеджеров
        shapeManager = new ShapeManager();
        commandManager = new CommandManager();
        
        // Инициализация ComboBox для выбора типа фигуры
        if (shapeTypeComboBox != null) {
            shapeTypeComboBox.getItems().addAll("Rectangle", "Circle", "Triangle");
            shapeTypeComboBox.setValue("Rectangle");
        }
        
        // Настройка обработчиков событий
        setupEventHandlers();
        
        // Начальная отрисовка
        redraw();
    }
    
    /**
     * Настройка обработчиков событий.
     */
    private void setupEventHandlers() {
        // Обработка мыши
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMouseClicked(this::onMouseClicked);
        
        // Обработка клавиатуры
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::onKeyPressed);
        
        // Обработчики кнопок
        createButton.setOnAction(e -> onCreateButtonClicked());
        deleteButton.setOnAction(e -> onDeleteButtonClicked());
        bringToFrontButton.setOnAction(e -> onBringToFrontClicked());
        sendToBackButton.setOnAction(e -> onSendToBackClicked());
        undoButton.setOnAction(e -> onUndoClicked());
        redoButton.setOnAction(e -> onRedoClicked());
        
        // Обновление состояния кнопок
        updateButtonStates();
    }
    
    /**
     * Обработка нажатия мыши.
     */
    private void onMousePressed(MouseEvent event) {
        canvas.requestFocus();
        
        double x = event.getX();
        double y = event.getY();
        
        // Сначала проверяем выделенную фигуру на попадание в область изменения размера
        // Это важно для фигур типа круга, где маркеры могут быть вне области contains()
        Shape selectedShape = shapeManager.getSelectedShape();
        if (selectedShape != null) {
            resizeCorner = selectedShape.isInResizeArea(x, y);
            if (resizeCorner > 0) {
                // Клик по маркеру изменения размера выделенной фигуры
                draggedShape = selectedShape;
                startX = x;
                startY = y;
                shapeStartX = selectedShape.getX();
                shapeStartY = selectedShape.getY();
                shapeStartWidth = selectedShape.getWidth();
                shapeStartHeight = selectedShape.getHeight();
                dragMode = DragMode.RESIZE;
                redraw();
                updateStatus();
                return;
            }
        }
        
        // Ищем фигуру по координатам
        Shape shape = shapeManager.findShapeAt(x, y);
        
        if (shape != null) {
            draggedShape = shape;
            startX = x;
            startY = y;
            shapeStartX = shape.getX();
            shapeStartY = shape.getY();
            shapeStartWidth = shape.getWidth();
            shapeStartHeight = shape.getHeight();
            
            // Проверка области изменения размера
            resizeCorner = shape.isInResizeArea(x, y);
            
            if (resizeCorner > 0) {
                dragMode = DragMode.RESIZE;
            } else {
                dragMode = DragMode.MOVE;
                // Выделяем объект
                shapeManager.selectShape(shape);
            }
        } else {
            // Клик по пустой области - снимаем выделение
            shapeManager.deselectAll();
            dragMode = DragMode.NONE;
        }
        
        redraw();
        updateStatus();
    }
    
    /**
     * Обработка перетаскивания мыши.
     */
    private void onMouseDragged(MouseEvent event) {
        if (draggedShape == null || dragMode == DragMode.NONE) {
            return;
        }
        
        double deltaX = event.getX() - startX;
        double deltaY = event.getY() - startY;
        
        if (dragMode == DragMode.MOVE) {
            // Перемещение
            double newX = shapeStartX + deltaX;
            double newY = shapeStartY + deltaY;
            
            // Ограничение в пределах холста
            newX = Math.max(0, Math.min(newX, canvas.getWidth() - draggedShape.getWidth()));
            newY = Math.max(0, Math.min(newY, canvas.getHeight() - draggedShape.getHeight()));
            
            draggedShape.setX(newX);
            draggedShape.setY(newY);
        } else if (dragMode == DragMode.RESIZE) {
            // Специальная обработка для круга - центр должен оставаться на месте
            if (draggedShape instanceof Circle) {
                resizeCircle(event.getX(), event.getY());
            } else if (draggedShape instanceof Triangle) {
                // Специальная обработка для треугольника
                resizeTriangle(event.getX(), event.getY());
            } else {
                // Изменение размера для других фигур (прямоугольник)
                double newX = shapeStartX;
                double newY = shapeStartY;
                double newWidth = shapeStartWidth;
                double newHeight = shapeStartHeight;
                
                switch (resizeCorner) {
                    case 1: // Левый верхний
                        newX = shapeStartX + deltaX;
                        newY = shapeStartY + deltaY;
                        newWidth = shapeStartWidth - deltaX;
                        newHeight = shapeStartHeight - deltaY;
                        break;
                    case 2: // Правый верхний
                        newY = shapeStartY + deltaY;
                        newWidth = shapeStartWidth + deltaX;
                        newHeight = shapeStartHeight - deltaY;
                        break;
                    case 3: // Левый нижний
                        newX = shapeStartX + deltaX;
                        newWidth = shapeStartWidth - deltaX;
                        newHeight = shapeStartHeight + deltaY;
                        break;
                    case 4: // Правый нижний
                        newWidth = shapeStartWidth + deltaX;
                        newHeight = shapeStartHeight + deltaY;
                        break;
                }
                
                // Ограничение в пределах холста
                if (newX < 0) {
                    newWidth += newX;
                    newX = 0;
                }
                if (newY < 0) {
                    newHeight += newY;
                    newY = 0;
                }
                if (newX + newWidth > canvas.getWidth()) {
                    newWidth = canvas.getWidth() - newX;
                }
                if (newY + newHeight > canvas.getHeight()) {
                    newHeight = canvas.getHeight() - newY;
                }
                
                draggedShape.setX(newX);
                draggedShape.setY(newY);
                draggedShape.setWidth(newWidth);
                draggedShape.setHeight(newHeight);
            }
        }
        
        redraw();
    }
    
    /**
     * Обработка отпускания мыши.
     */
    private void onMouseReleased(MouseEvent event) {
        if (draggedShape != null && dragMode != DragMode.NONE) {
            if (dragMode == DragMode.MOVE) {
                // Создаем команду перемещения
                double newX = draggedShape.getX();
                double newY = draggedShape.getY();
                
                if (newX != shapeStartX || newY != shapeStartY) {
                    Command command = new MoveShapeCommand(
                        shapeManager,
                        draggedShape.getId(),
                        shapeStartX, shapeStartY,
                        newX, newY
                    );
                    commandManager.executeCommand(command);
                }
            } else if (dragMode == DragMode.RESIZE) {
                // Создаем команду изменения размера
                double newX = draggedShape.getX();
                double newY = draggedShape.getY();
                double newWidth = draggedShape.getWidth();
                double newHeight = draggedShape.getHeight();
                
                if (newX != shapeStartX || newY != shapeStartY ||
                    newWidth != shapeStartWidth || newHeight != shapeStartHeight) {
                    Command command = new ResizeShapeCommand(
                        shapeManager,
                        draggedShape.getId(),
                        shapeStartX, shapeStartY, shapeStartWidth, shapeStartHeight,
                        newX, newY, newWidth, newHeight
                    );
                    commandManager.executeCommand(command);
                }
            }
        }
        
        dragMode = DragMode.NONE;
        draggedShape = null;
        updateButtonStates();
        updateStatus();
    }
    
    /**
     * Обработка клика мыши (двойной клик для создания).
     */
    private void onMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Двойной клик - создание фигуры выбранного типа
            onCreateShapeAt(event.getX(), event.getY());
        }
    }
    
    /**
     * Обработка нажатия клавиш.
     */
    private void onKeyPressed(KeyEvent event) {
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.Z) {
                onUndoClicked();
            } else if (event.getCode() == KeyCode.Y) {
                onRedoClicked();
            }
        } else if (event.getCode() == KeyCode.DELETE) {
            onDeleteButtonClicked();
        }
    }
    
    /**
     * Обработка нажатия кнопки создания.
     */
    @FXML
    private void onCreateButtonClicked() {
        // Создание в случайном месте
        double x = Math.random() * (canvas.getWidth() - 100) + 20;
        double y = Math.random() * (canvas.getHeight() - 100) + 20;
        onCreateShapeAt(x, y);
    }
    
    /**
     * Создание фигуры в указанной точке.
     */
    private void onCreateShapeAt(double x, double y) {
        double width = 80 + Math.random() * 40;
        double height = 60 + Math.random() * 40;
        
        // Для круга используем одинаковые размеры
        CreateShapeCommand.ShapeType shapeType = getSelectedShapeType();
        if (shapeType == CreateShapeCommand.ShapeType.CIRCLE) {
            double size = Math.min(width, height);
            width = size;
            height = size;
        }
        
        // Корректировка позиции, чтобы фигура не выходила за границы
        x = Math.max(10, Math.min(x, canvas.getWidth() - width - 10));
        y = Math.max(10, Math.min(y, canvas.getHeight() - height - 10));
        
        Command command = new CreateShapeCommand(shapeManager, shapeType, x, y, width, height);
        commandManager.executeCommand(command);
        
        redraw();
        updateButtonStates();
        updateStatus();
    }
    
    /**
     * Получает выбранный тип фигуры из ComboBox.
     */
    private CreateShapeCommand.ShapeType getSelectedShapeType() {
        if (shapeTypeComboBox == null) {
            return CreateShapeCommand.ShapeType.RECTANGLE;
        }
        
        String selected = shapeTypeComboBox.getValue();
        if (selected == null) {
            return CreateShapeCommand.ShapeType.RECTANGLE;
        }
        
        switch (selected) {
            case "Circle":
                return CreateShapeCommand.ShapeType.CIRCLE;
            case "Triangle":
                return CreateShapeCommand.ShapeType.TRIANGLE;
            case "Rectangle":
            default:
                return CreateShapeCommand.ShapeType.RECTANGLE;
        }
    }
    
    /**
     * Обработка нажатия кнопки удаления.
     */
    @FXML
    private void onDeleteButtonClicked() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected != null) {
            Command command = new DeleteShapeCommand(shapeManager, selected.getId());
            commandManager.executeCommand(command);
            
            redraw();
            updateButtonStates();
            updateStatus();
        }
    }
    
    /**
     * Обработка нажатия кнопки "Bring to Front".
     */
    @FXML
    private void onBringToFrontClicked() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected != null) {
            Command command = new BringToFrontCommand(shapeManager, selected.getId());
            commandManager.executeCommand(command);
            
            redraw();
            updateButtonStates();
        }
    }
    
    /**
     * Обработка нажатия кнопки "Send to Back".
     */
    @FXML
    private void onSendToBackClicked() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected != null) {
            Command command = new SendToBackCommand(shapeManager, selected.getId());
            commandManager.executeCommand(command);
            
            redraw();
            updateButtonStates();
        }
    }
    
    /**
     * Обработка нажатия кнопки "Undo".
     */
    @FXML
    private void onUndoClicked() {
        // Сбрасываем состояние перетаскивания при undo
        resetDragState();
        commandManager.undo();
        redraw();
        updateButtonStates();
        updateStatus();
    }
    
    /**
     * Обработка нажатия кнопки "Redo".
     */
    @FXML
    private void onRedoClicked() {
        // Сбрасываем состояние перетаскивания при redo
        resetDragState();
        commandManager.redo();
        redraw();
        updateButtonStates();
        updateStatus();
    }
    
    /**
     * Изменение размера круга с сохранением центра.
     */
    private void resizeCircle(double mouseX, double mouseY) {
        Circle circle = (Circle) draggedShape;
        
        // Вычисляем центр круга до изменения размера
        double centerX = shapeStartX + shapeStartWidth / 2.0;
        double centerY = shapeStartY + shapeStartHeight / 2.0;
        
        // Вычисляем расстояние от центра до текущей позиции мыши
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double newRadius = Math.sqrt(dx * dx + dy * dy);
        
        // Минимальный размер (используем константу из Shape)
        double minSize = 5.0; // MIN_SIZE из Shape
        newRadius = Math.max(minSize / 2.0, newRadius);
        
        // Новый размер (квадрат для круга)
        double newSize = newRadius * 2.0;
        
        // Вычисляем новую позицию так, чтобы центр остался на месте
        double newX = centerX - newRadius;
        double newY = centerY - newRadius;
        
        // Ограничение в пределах холста
        if (newX < 0) {
            newX = 0;
            newSize = Math.min(newSize, (centerX - newX) * 2.0);
        }
        if (newY < 0) {
            newY = 0;
            newSize = Math.min(newSize, (centerY - newY) * 2.0);
        }
        if (newX + newSize > canvas.getWidth()) {
            newSize = canvas.getWidth() - newX;
        }
        if (newY + newSize > canvas.getHeight()) {
            newSize = canvas.getHeight() - newY;
        }
        
        // Устанавливаем новый размер и позицию
        circle.setX(newX);
        circle.setY(newY);
        circle.setWidth(newSize);
        circle.setHeight(newSize);
    }
    
    /**
     * Изменение размера треугольника.
     * У треугольника 3 маркера: верхняя вершина, левая нижняя, правая нижняя.
     */
    private void resizeTriangle(double mouseX, double mouseY) {
        Triangle triangle = (Triangle) draggedShape;
        double deltaX = mouseX - startX;
        double deltaY = mouseY - startY;
        
        double newX = shapeStartX;
        double newY = shapeStartY;
        double newWidth = shapeStartWidth;
        double newHeight = shapeStartHeight;
        
        switch (resizeCorner) {
            case 1: // Верхняя вершина - изменяем только Y и height
                newY = shapeStartY + deltaY;
                newHeight = shapeStartHeight - deltaY;
                break;
            case 2: // Левая нижняя вершина - изменяем X, width и height
                newX = shapeStartX + deltaX;
                newWidth = shapeStartWidth - deltaX;
                newHeight = shapeStartHeight + deltaY;
                break;
            case 3: // Правая нижняя вершина - изменяем width и height
                newWidth = shapeStartWidth + deltaX;
                newHeight = shapeStartHeight + deltaY;
                break;
        }
        
        // Ограничение в пределах холста
        if (newX < 0) {
            newWidth += newX;
            newX = 0;
        }
        if (newY < 0) {
            newHeight += newY;
            newY = 0;
        }
        if (newX + newWidth > canvas.getWidth()) {
            newWidth = canvas.getWidth() - newX;
        }
        if (newY + newHeight > canvas.getHeight()) {
            newHeight = canvas.getHeight() - newY;
        }
        
        // Устанавливаем новый размер и позицию
        triangle.setX(newX);
        triangle.setY(newY);
        triangle.setWidth(newWidth);
        triangle.setHeight(newHeight);
    }
    
    /**
     * Сбрасывает состояние перетаскивания.
     */
    private void resetDragState() {
        dragMode = DragMode.NONE;
        draggedShape = null;
        resizeCorner = 0;
    }
    
    /**
     * Обработка пункта меню "New".
     */
    @FXML
    private void onNew() {
        shapeManager.clear();
        commandManager.clear();
        redraw();
        updateButtonStates();
        updateStatus();
    }
    
    /**
     * Обработка пункта меню "Exit".
     */
    @FXML
    private void onExit() {
        javafx.application.Platform.exit();
    }
    
    /**
     * Обработка пункта меню "Undo".
     */
    @FXML
    private void onUndo() {
        onUndoClicked();
    }
    
    /**
     * Обработка пункта меню "Redo".
     */
    @FXML
    private void onRedo() {
        onRedoClicked();
    }
    
    /**
     * Перерисовка холста.
     */
    private void redraw() {
        var gc = canvas.getGraphicsContext2D();
        
        // Очистка
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Отрисовка сетки
        drawGrid(gc);
        
        // Отрисовка всех объектов
        for (Shape shape : shapeManager.getShapes()) {
            shape.draw(gc);
        }
    }
    
    /**
     * Отрисовка сетки.
     */
    private void drawGrid(javafx.scene.canvas.GraphicsContext gc) {
        gc.setStroke(javafx.scene.paint.Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        
        double gridSize = 20.0;
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        // Вертикальные линии
        for (double x = 0; x <= width; x += gridSize) {
            gc.strokeLine(x, 0, x, height);
        }
        
        // Горизонтальные линии
        for (double y = 0; y <= height; y += gridSize) {
            gc.strokeLine(0, y, width, y);
        }
    }
    
    /**
     * Обновление состояния кнопок.
     */
    private void updateButtonStates() {
        undoButton.setDisable(!commandManager.canUndo());
        redoButton.setDisable(!commandManager.canRedo());
        
        Shape selected = shapeManager.getSelectedShape();
        deleteButton.setDisable(selected == null);
        bringToFrontButton.setDisable(selected == null);
        sendToBackButton.setDisable(selected == null);
    }
    
    /**
     * Обновление статусной строки.
     */
    private void updateStatus() {
        Shape selected = shapeManager.getSelectedShape();
        if (selected != null) {
            statusLabel.setText(String.format(
                "Selected: %s at (%.0f, %.0f), Size: %.0f × %.0f",
                selected.getShapeType(),
                selected.getX(), selected.getY(),
                selected.getWidth(), selected.getHeight()
            ));
        } else {
            String shapeType = shapeTypeComboBox != null && shapeTypeComboBox.getValue() != null 
                ? shapeTypeComboBox.getValue().toLowerCase() 
                : "rectangle";
            statusLabel.setText("No selection. Double-click to create " + shapeType + ".");
        }
    }
}

