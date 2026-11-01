package com.lab4.presentation;

import com.lab4.application.PotOnStoveController;
import com.lab4.domain.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер главного окна приложения.
 * Связывает модель (PotOnStoveController) с представлением (FXML).
 */
public class MainController implements Initializable, PotStateListener {
    @FXML
    private Label temperatureLabel;

    @FXML
    private Label volumeLabel;

    @FXML
    private Label lidStatusLabel;

    @FXML
    private Slider burnerSlider;

    @FXML
    private Label burnerValueLabel;

    @FXML
    private Button lidToggleButton;

    @FXML
    private Label statusLabel;
    
    @FXML
    private TextField waterVolumeField;
    
    @FXML
    private TextField waterTemperatureField;
    
    @FXML
    private Button addWaterButton;
    

    private PotOnStoveController potController;
    private Thread simulationThread;
    private ChartWindowController chartWindowController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Создание контроллера модели
        potController = new PotOnStoveController();

        // Подписка на обновления модели
        potController.addStateListener(this);

        // Настройка обработчиков элементов управления
        setupControlHandlers();

        // Запуск симуляции в отдельном потоке
        startSimulation();
    }
    
    /**
     * Настраивает обработчики элементов управления GUI.
     */
    private void setupControlHandlers() {
        // Обработчик слайдера конфорки - теперь 0-4 вместо процентов
        burnerSlider.setMin(0);
        burnerSlider.setMax(4);
        burnerSlider.setValue(0);
        burnerSlider.setMajorTickUnit(1);
        burnerSlider.setMinorTickCount(0);
        burnerSlider.setSnapToTicks(true);
        
        burnerSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int level = newVal.intValue();
            double normalizedLevel = level / 4.0; // Преобразование 0-4 в 0.0-1.0
            potController.setBurnerLevel(normalizedLevel);
            burnerValueLabel.setText(String.valueOf(level));
        });

        // Обработчик кнопки крышки
        lidToggleButton.setOnAction(e -> onLidToggleClicked());
        
        // Обработчик кнопки доливания воды
        if (addWaterButton != null) {
            addWaterButton.setOnAction(e -> onAddWaterClicked());
        }
    }
    
    /**
     * Открывает окно с графиком температуры.
     */
    @FXML
    private void onShowChartClicked() {
        if (chartWindowController == null || 
            (chartWindowController.stage != null && !chartWindowController.stage.isShowing())) {
            chartWindowController = ChartWindowController.createAndShow(potController);
        } else {
            // Окно уже открыто, просто показываем его
            chartWindowController.stage.toFront();
        }
    }
    
    /**
     * Обработчик нажатия кнопки доливания воды.
     */
    @FXML
    private void onAddWaterClicked() {
        try {
            double volume = Double.parseDouble(waterVolumeField.getText());
            double temperature = Double.parseDouble(waterTemperatureField.getText());
            
            if (volume > 0 && temperature >= 0 && temperature <= 100) {
                try {
                    potController.addWater(volume, temperature);
                    statusLabel.setText(String.format("Добавлено %.2f л воды при температуре %.1f°C", volume, temperature));
                } catch (IllegalArgumentException e) {
                    // Показываем предупреждение о превышении максимального объема
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Предупреждение");
                    alert.setHeaderText("Превышен максимальный объем");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    statusLabel.setText("Действие отменено: превышен максимальный объем кастрюли (10 л)");
                }
            } else {
                statusLabel.setText("Ошибка: некорректные значения (объем > 0, температура 0-100°C)");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Ошибка: введите числовые значения");
        }
    }

    /**
     * Обработчик нажатия кнопки переключения крышки.
     */
    @FXML
    private void onLidToggleClicked() {
        potController.toggleLid();
    }

    /**
     * Запускает симуляцию в отдельном потоке.
     */
    private void startSimulation() {
        simulationThread = new Thread(potController);
        simulationThread.setDaemon(true); // Поток завершится при закрытии приложения
        simulationThread.start();
    }

    /**
     * Останавливает симуляцию и отписывается от обновлений.
     */
    public void shutdown() {
        if (potController != null) {
            potController.shutdown();
            potController.removeStateListener(this);
        }

        // Ждем завершения потока (с таймаутом)
        if (simulationThread != null && simulationThread.isAlive()) {
            try {
                simulationThread.join(1000); // Ждем до 1 секунды
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void onStateUpdate(PotState state) {
        // Обновление GUI должно происходить в потоке JavaFX
        Platform.runLater(() -> {
            temperatureLabel.setText(String.format("%.1f °C", state.temperature()));
            volumeLabel.setText(String.format("%.2f л", state.volume()));

            if (state.isLidOn()) {
                lidStatusLabel.setText("Крышка: на месте");
                lidToggleButton.setText("Снять крышку");
            } else {
                lidStatusLabel.setText("Крышка: снята");
                lidToggleButton.setText("Надеть крышку");
            }
        });
    }

    @Override
    public void onEvent(PotEvent event) {
        // Обработка событий в потоке JavaFX
        Platform.runLater(() -> {
            if (event instanceof BoilingStartedEvent) {
                statusLabel.setText("Вода закипела! Началось испарение.");
            } else if (event instanceof BoilingStoppedEvent) {
                statusLabel.setText("Кипение прекратилось.");
            }
        });
    }
}
