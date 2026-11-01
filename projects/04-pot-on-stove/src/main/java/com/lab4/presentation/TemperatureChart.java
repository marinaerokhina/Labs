package com.lab4.presentation;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Компонент для отображения графика температуры от времени в реальном времени.
 * График отражает состояние кастрюли в течение всей симуляции без ограничений.
 */
public class TemperatureChart extends LineChart<Number, Number> {
    private final XYChart.Series<Number, Number> temperatureSeries;
    private long startTime;
    private boolean isInitialized = false;
    
    public TemperatureChart() {
        super(new NumberAxis(), new NumberAxis());
        
        // Настройка осей
        NumberAxis xAxis = (NumberAxis) getXAxis();
        NumberAxis yAxis = (NumberAxis) getYAxis();
        xAxis.setLabel("Время (секунды)");
        yAxis.setLabel("Температура (°C)");
        yAxis.setAutoRanging(true);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(120);
        
        setTitle("График температуры");
        setAnimated(false); // Отключаем анимацию для лучшей производительности
        setCreateSymbols(false); // Не показываем символы на точках
        
        // Создание серии данных
        temperatureSeries = new XYChart.Series<>();
        temperatureSeries.setName("Температура");
        getData().add(temperatureSeries);
    }
    
    /**
     * Добавляет новую точку данных на график.
     * График не ограничен по количеству точек и показывает всю историю симуляции.
     */
    public void addDataPoint(double temperature) {
        if (!isInitialized) {
            startTime = System.currentTimeMillis();
            isInitialized = true;
        }
        
        long currentTime = System.currentTimeMillis();
        double timeInSeconds = (currentTime - startTime) / 1000.0;
        
        XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(timeInSeconds, temperature);
        temperatureSeries.getData().add(dataPoint);
        
        // Автоматическое обновление диапазона оси X для отображения всего графика
        if (temperatureSeries.getData().size() > 0) {
            NumberAxis xAxis = (NumberAxis) getXAxis();
            double maxTime = timeInSeconds;
            double minTime = 0; // Показываем с начала симуляции
            xAxis.setLowerBound(minTime);
            xAxis.setUpperBound(maxTime + 5); // Небольшой отступ справа
        }
    }
    
    /**
     * Очищает график.
     */
    public void clear() {
        temperatureSeries.getData().clear();
        isInitialized = false;
    }
}

