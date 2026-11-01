package com.lab4.domain;

/**
 * Интерфейс команд для управления состоянием кастрюли.
 */
public interface PotCommands {
    /**
     * Устанавливает уровень конфорки.
     * @param level уровень конфорки от 0.0 (выключена) до 1.0 (максимальная мощность)
     */
    void setBurnerLevel(double level);

    /**
     * Переключает состояние крышки (надеть/снять).
     */
    void toggleLid();
    
    /**
     * Доливает воду заданной температуры в кастрюлю.
     * @param volume объем доливаемой воды в литрах
     * @param temperature температура доливаемой воды в градусах Цельсия
     */
    void addWater(double volume, double temperature);
}

