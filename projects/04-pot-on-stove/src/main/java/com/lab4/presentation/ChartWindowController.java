package com.lab4.presentation;

import com.lab4.application.PotOnStoveController;
import com.lab4.domain.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер отдельного окна с графиком температуры.
 * Является независимым подписчиком на события модели (шаблон "Наблюдатель").
 */
public class ChartWindowController implements Initializable, PotStateListener {
    @FXML
    private VBox chartContainer;
    
    private TemperatureChart temperatureChart;
    private PotOnStoveController potController;
    
    public Stage stage;
    
    /**
     * Создает и показывает окно графика.
     */
    public static ChartWindowController createAndShow(PotOnStoveController potController) {
        try {
            FXMLLoader loader = new FXMLLoader(ChartWindowController.class.getResource("/com/lab4/chartWindow.fxml"));
            VBox root = loader.load();
            ChartWindowController controller = loader.getController();
            
            controller.potController = potController;
            controller.initializeChart();
            
            // Подписка на обновления модели
            potController.addStateListener(controller);
            
            // Создание окна
            controller.stage = new Stage();
            controller.stage.setTitle("График температуры");
            controller.stage.setScene(new Scene(root, 600, 400));
            controller.stage.setResizable(true);
            controller.stage.setMinWidth(400);
            controller.stage.setMinHeight(300);
            
            // Обработка закрытия окна - отписка от модели
            controller.stage.setOnCloseRequest(e -> {
                if (controller.potController != null) {
                    controller.potController.removeStateListener(controller);
                }
            });
            
            controller.stage.show();
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Инициализация будет выполнена в createAndShow
    }
    
    /**
     * Инициализация графика температуры.
     */
    private void initializeChart() {
        temperatureChart = new TemperatureChart();
        temperatureChart.setPrefHeight(350);
        temperatureChart.setPrefWidth(550);
        if (chartContainer != null) {
            chartContainer.getChildren().add(temperatureChart);
        }
    }
    
    @Override
    public void onStateUpdate(PotState state) {
        // Обновление графика в потоке JavaFX
        Platform.runLater(() -> {
            if (temperatureChart != null) {
                temperatureChart.addDataPoint(state.temperature());
            }
        });
    }
    
    @Override
    public void onEvent(PotEvent event) {
        // График не обрабатывает события, только обновления состояния
    }
    
    /**
     * Закрывает окно и отписывается от модели.
     */
    public void close() {
        if (potController != null) {
            potController.removeStateListener(this);
        }
        if (stage != null) {
            stage.close();
        }
    }
}

