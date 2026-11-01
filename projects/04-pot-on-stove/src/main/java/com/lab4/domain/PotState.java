package com.lab4.domain;

/**
 * Неизменяемый объект-значение, представляющий полный снимок состояния кастрюли
 * в определенный момент времени.
 */
public record PotState(
        double temperature,      // Температура в градусах Цельсия
        double volume,           // Объем содержимого в литрах
        boolean isLidOn,         // Наличие крышки
        double burnerLevel,      // Уровень конфорки (0.0 - 1.0)
        boolean isBoiling        // Состояние кипения
) {
    /**
     * Создает начальное состояние кастрюли
     */
    public static PotState initial() {
        return new PotState(20.0, 1.0, true, 0.0, false);
    }
}

