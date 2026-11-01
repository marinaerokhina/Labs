package com.rgz_e.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Менеджер команд для управления историей операций.
 * Обеспечивает поддержку отмены (Undo) и повтора (Redo).
 */
public class CommandManager {
    private final Deque<Command> undoStack;
    private final Deque<Command> redoStack;
    private final int maxHistorySize;
    
    public CommandManager() {
        this(50); // По умолчанию 50 команд
    }
    
    public CommandManager(int maxHistorySize) {
        this.undoStack = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
        this.maxHistorySize = maxHistorySize;
    }
    
    /**
     * Выполняет новую команду.
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        
        // Очистка стека повтора при выполнении новой команды
        redoStack.clear();
        
        // Ограничение размера истории - удаляем самую старую команду (снизу стека)
        while (undoStack.size() > maxHistorySize) {
            undoStack.removeLast();
        }
    }
    
    /**
     * Отменяет последнюю команду.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }
    
    /**
     * Повторяет отмененную команду.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
    
    /**
     * Проверяет возможность отмены.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    /**
     * Проверяет возможность повтора.
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    /**
     * Получает описание последней команды для отмены.
     */
    public String getUndoDescription() {
        if (canUndo()) {
            return undoStack.peek().getDescription();
        }
        return null;
    }
    
    /**
     * Получает описание последней команды для повтора.
     */
    public String getRedoDescription() {
        if (canRedo()) {
            return redoStack.peek().getDescription();
        }
        return null;
    }
    
    /**
     * Очищает всю историю команд.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}

