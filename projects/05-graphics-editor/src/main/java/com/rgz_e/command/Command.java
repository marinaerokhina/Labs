package com.rgz_e.command;

/**
 * Интерфейс команды с методами выполнения и отмены.
 */
public interface Command {
    /**
     * Выполняет команду.
     */
    void execute();
    
    /**
     * Отменяет выполнение команды.
     */
    void undo();
    
    /**
     * Возвращает текстовое описание команды.
     */
    String getDescription();
}

