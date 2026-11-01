package com.lab4.presentation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Пользовательский компонент GUI для отображения состояния кастрюли
 * и управления ею.
 */
public class PotControlPane extends VBox {
    private final Label temperatureLabel;
    private final Label volumeLabel;
    private final Label lidStatusLabel;
    private final Slider burnerSlider;
    private final Button lidToggleButton;
    private final Label statusLabel;
    
    public PotControlPane() {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        
        // Заголовок
        Label titleLabel = new Label("Кастрюля на плите");
        titleLabel.setFont(new Font(24));
        getChildren().add(titleLabel);
        
        // Температура
        VBox temperatureBox = new VBox(5);
        temperatureBox.setAlignment(Pos.CENTER);
        Label tempTitle = new Label("Температура:");
        tempTitle.setFont(new Font(14));
        temperatureLabel = new Label("20.0 °C");
        temperatureLabel.setFont(new Font(20));
        temperatureBox.getChildren().addAll(tempTitle, temperatureLabel);
        getChildren().add(temperatureBox);
        
        // Объем
        VBox volumeBox = new VBox(5);
        volumeBox.setAlignment(Pos.CENTER);
        Label volumeTitle = new Label("Объем:");
        volumeTitle.setFont(new Font(14));
        volumeLabel = new Label("1.0 л");
        volumeLabel.setFont(new Font(20));
        volumeBox.getChildren().addAll(volumeTitle, volumeLabel);
        getChildren().add(volumeBox);
        
        // Состояние крышки
        lidStatusLabel = new Label("Крышка: на месте");
        lidStatusLabel.setFont(new Font(14));
        getChildren().add(lidStatusLabel);
        
        // Слайдер конфорки
        VBox burnerBox = new VBox(5);
        burnerBox.setAlignment(Pos.CENTER);
        Label burnerTitle = new Label("Уровень конфорки:");
        burnerTitle.setFont(new Font(14));
        burnerSlider = new Slider(0, 100, 0);
        burnerSlider.setShowTickLabels(true);
        burnerSlider.setShowTickMarks(true);
        burnerSlider.setMajorTickUnit(25);
        burnerSlider.setBlockIncrement(5);
        burnerSlider.setPrefWidth(300);
        Label burnerValueLabel = new Label("0%");
        burnerValueLabel.setFont(new Font(12));
        
        // Обновление метки при изменении слайдера
        burnerSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            burnerValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
        });
        
        HBox burnerControl = new HBox(10);
        burnerControl.setAlignment(Pos.CENTER);
        burnerControl.getChildren().addAll(burnerSlider, burnerValueLabel);
        burnerBox.getChildren().addAll(burnerTitle, burnerControl);
        getChildren().add(burnerBox);
        
        // Кнопка переключения крышки
        lidToggleButton = new Button("Снять крышку");
        lidToggleButton.setPrefWidth(200);
        lidToggleButton.setPrefHeight(40);
        getChildren().add(lidToggleButton);
        
        // Статусная строка
        statusLabel = new Label("");
        statusLabel.setFont(new Font(12));
        statusLabel.setStyle("-fx-text-fill: blue;");
        getChildren().add(statusLabel);
    }
    
    /**
     * Обновляет отображаемую температуру.
     */
    public void updateTemperature(double temperature) {
        temperatureLabel.setText(String.format("%.1f °C", temperature));
    }
    
    /**
     * Обновляет отображаемый объем.
     */
    public void updateVolume(double volume) {
        volumeLabel.setText(String.format("%.2f л", volume));
    }
    
    /**
     * Обновляет отображение состояния крышки.
     */
    public void updateLidStatus(boolean isLidOn) {
        if (isLidOn) {
            lidStatusLabel.setText("Крышка: на месте");
            lidToggleButton.setText("Снять крышку");
        } else {
            lidStatusLabel.setText("Крышка: снята");
            lidToggleButton.setText("Надеть крышку");
        }
    }
    
    /**
     * Обновляет статусное сообщение.
     */
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    /**
     * Возвращает слайдер конфорки для настройки обработчиков.
     */
    public Slider getBurnerSlider() {
        return burnerSlider;
    }
    
    /**
     * Возвращает кнопку переключения крышки.
     */
    public Button getLidToggleButton() {
        return lidToggleButton;
    }
}

