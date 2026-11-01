package com.indicator.Presentation;

import com.indicator.Infrastructure.PeriodicTimer;
import com.indicator.PeakIndicator.Application.DataSource;
import com.indicator.PeakIndicator.Application.ThresholdListener;
import com.indicator.PeakIndicator.Domain.MaxThresholdExceeded;
import com.indicator.PeakIndicator.Domain.PeakMaxThresholdCrossedUp;
import com.indicator.PeakIndicator.Domain.PeakMaxThresholdCrossedDown;
import com.indicator.PeakIndicator.Presentation.PeakIndicatorPane;
import com.indicator.Infrastructure.RealSineSource;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController {
    private PeakIndicatorPane indicatorPane;
    private PeriodicTimer timer;
    private final Label status = new Label("—");
    private final Label peakStatus = new Label("Peak: —");
    private BorderPane root;
    
    private final TextField dataMinField = new TextField("0.0");
    private final TextField dataMaxField = new TextField("150.0");
    private final TextField thMinField = new TextField("20.0");
    private final TextField thMaxField = new TextField("120.0");
    private final TextField peakDecayField = new TextField("0.15");
    private final TextField historyCapacityField = new TextField("512");
    private final TextField timerPeriodField = new TextField("50");
        
    public MainController() {
    }
    
    private void createIndicatorPane(
        double dataMin,
        double dataMax,
        double thMin,
        double thMax,
        double peakDecayPerSecond,
        int historyCapacity,
        DataSource dataSource,
        long timerPeriodMillis) {
        if (timer != null) {
            timer.stop();
        }
        
        indicatorPane = new PeakIndicatorPane(
                dataMin, dataMax,
                thMin, thMax,
                peakDecayPerSecond,
                historyCapacity,
                dataSource
        );
        
        indicatorPane.setThresholdListener(new ThresholdListener() {
            @Override
            public void onExceeded(MaxThresholdExceeded e) {
                Platform.runLater(() -> {
                    status.setText("(ㆆ_ㆆ) Max exceeded: " + e.current + " ≥ " + e.threshold);
                });
            }
            
            @Override
            public void onPeakCrossedUp(PeakMaxThresholdCrossedUp e) {
                Platform.runLater(() -> {
                    peakStatus.setText("Peak: ↑ (" + e.peak + " ≥ " + e.threshold + ")");
                });
            }
            
            @Override
            public void onPeakCrossedDown(PeakMaxThresholdCrossedDown e) {
                Platform.runLater(() -> {
                    peakStatus.setText("Peak: ↓ (" + e.peak + " < " + e.threshold + ")");
                });
            }
        });
        
        timer = new PeriodicTimer();
        timer.start(timerPeriodMillis, indicatorPane::onTick);
    }
    
    private void applySettings() {
        try {
            double dataMin = Double.parseDouble(dataMinField.getText());
            double dataMax = Double.parseDouble(dataMaxField.getText());
            double thMin = Double.parseDouble(thMinField.getText());
            double thMax = Double.parseDouble(thMaxField.getText());
            double peakDecayPerSecond = Double.parseDouble(peakDecayField.getText());
            int historyCapacity = Integer.parseInt(historyCapacityField.getText());
            long timerPeriodMillis = Long.parseLong(timerPeriodField.getText());
            
            DataSource dataSource = new RealSineSource(dataMin, dataMax, 0.05);
            
            createIndicatorPane(dataMin, dataMax, thMin, thMax, peakDecayPerSecond,
                              historyCapacity, dataSource, timerPeriodMillis);
            
            updateUI();
            
            status.setText("Настройки применены");
        } catch (NumberFormatException e) {
            status.setText("Ошибка: неверный формат числа");
        } catch (IllegalArgumentException e) {
            status.setText("Ошибка: " + e.getMessage());
        }
    }
    
    private void updateUI() {
        if (root != null) {
            root.setCenter(indicatorPane);
        }
    }
    
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }

    public Pane getRoot() {
        root = new BorderPane();
        root.setPadding(new Insets(12));
        
        if (indicatorPane == null) {
            applySettings();
        }
        
        VBox settingsPanel = new VBox(8);
        settingsPanel.setPadding(new Insets(8));
        settingsPanel.setPrefWidth(200);
        
        settingsPanel.getChildren().add(new Label("Настройки:"));
        
        settingsPanel.getChildren().add(createLabeledField("Data Min:", dataMinField));
        settingsPanel.getChildren().add(createLabeledField("Data Max:", dataMaxField));
        settingsPanel.getChildren().add(createLabeledField("Threshold Min:", thMinField));
        settingsPanel.getChildren().add(createLabeledField("Threshold Max:", thMaxField));
        settingsPanel.getChildren().add(createLabeledField("Peak Decay/s:", peakDecayField));
        settingsPanel.getChildren().add(createLabeledField("History Capacity:", historyCapacityField));
        settingsPanel.getChildren().add(createLabeledField("Timer Period (ms):", timerPeriodField));
        
        Button applyButton = new Button("Применить");
        applyButton.setOnAction(e -> applySettings());
        settingsPanel.getChildren().add(applyButton);
        
        root.setCenter(indicatorPane);
        root.setRight(settingsPanel);
        
        // Статус внизу
        HBox bottom = new HBox(10, status, peakStatus);
        bottom.setPadding(new Insets(8));
        root.setBottom(bottom);
        
        return root;
    }
    
    private HBox createLabeledField(String label, TextField field) {
        HBox hbox = new HBox(5);
        Label lbl = new Label(label);
        lbl.setPrefWidth(120);
        field.setPrefWidth(70);
        hbox.getChildren().addAll(lbl, field);
        return hbox;
    }
}
