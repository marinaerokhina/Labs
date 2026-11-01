package com.lab4.application;

import com.lab4.domain.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Контроллер, реализующий логику симуляции кастрюли на плите.
 * Выполняется в отдельном потоке и управляет физической моделью.
 */
public class PotOnStoveController implements Runnable, PotCommands {
    // Физические константы
    private static final double AMBIENT_TEMPERATURE = 20.0; // Температура окружающей среды
    private static final double BOILING_TEMPERATURE = 100.0; // Температура кипения
    private static final double HEAT_CAPACITY = 4200.0; // Теплоемкость воды (Дж/(кг*К))
    private static final double WATER_DENSITY = 1.0; // Плотность воды (кг/л)
    private static final double HEAT_TRANSFER_COEFFICIENT = 0.1; // Коэффициент теплопередачи
    private static final double HEAT_TRANSFER_COEFFICIENT_WITH_LID = 0.05; // С крышкой
    private static final double MAX_HEAT_POWER = 2000.0; // Максимальная мощность конфорки (Вт)
    private static final double EVAPORATION_RATE = 0.01; // Скорость испарения при кипении без крышки (л/с на единицу мощности)
    private static final double EVAPORATION_RATE_WITH_LID = 0.001; // Скорость испарения при кипении с крышкой (л/с на единицу мощности) - в 10 раз медленнее
    private static final double MAX_VOLUME = 10.0; // Максимальный объем кастрюли в литрах
    
    // Состояние модели
    private double temperature = 20.0;
    private double volume = 1.0;
    private double burnerLevel = 0.0;
    private boolean isLidOn = true;
    private boolean isBoiling = false;
    private boolean wasBoiling = false; // Предыдущее состояние кипения для отслеживания событий
    
    // Управление потоком
    private volatile boolean isRunning = true;
    private long lastUpdateTime = System.currentTimeMillis();
    
    // Список подписчиков (потокобезопасный)
    private final List<PotStateListener> listeners = new CopyOnWriteArrayList<>();
    
    /**
     * Добавляет подписчика на обновления состояния.
     */
    public void addStateListener(PotStateListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /**
     * Удаляет подписчика.
     */
    public void removeStateListener(PotStateListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Останавливает симуляцию.
     */
    public void shutdown() {
        isRunning = false;
    }
    
    @Override
    public void setBurnerLevel(double level) {
        synchronized (this) {
            this.burnerLevel = Math.max(0.0, Math.min(1.0, level));
        }
    }
    
    @Override
    public void toggleLid() {
        synchronized (this) {
            this.isLidOn = !this.isLidOn;
        }
    }
    
    @Override
    public void addWater(double volume, double temperature) {
        synchronized (this) {
            if (volume <= 0) return;
            
            // Проверка максимального объема
            if (this.volume + volume > MAX_VOLUME) {
                throw new IllegalArgumentException(
                    String.format("Превышен максимальный объем кастрюли (10 л). Текущий объем: %.2f л, попытка добавить: %.2f л", 
                        this.volume, volume)
                );
            }
            
            // Вычисление новой температуры по закону сохранения энергии
            // Q = m * c * ΔT, где Q - количество теплоты, m - масса, c - теплоемкость
            double currentMass = this.volume * WATER_DENSITY;
            double addedMass = volume * WATER_DENSITY;
            double totalMass = currentMass + addedMass;
            
            if (totalMass > 0) {
                // Средняя температура = (m1*T1 + m2*T2) / (m1 + m2)
                // Без ограничения снизу - температура может быть любой
                double newTemperature = (currentMass * this.temperature + addedMass * temperature) / totalMass;
                this.temperature = newTemperature;
                this.volume += volume;
                
                // Если температура упала ниже 100°C, прекращаем кипение
                if (this.temperature < BOILING_TEMPERATURE) {
                    this.isBoiling = false;
                }
            }
        }
    }
    
    /**
     * Основной цикл симуляции.
     */
    @Override
    public void run() {
        lastUpdateTime = System.currentTimeMillis();
        
        while (isRunning) {
            try {
                long currentTime = System.currentTimeMillis();
                double dt = (currentTime - lastUpdateTime) / 1000.0; // Дельта времени в секундах
                lastUpdateTime = currentTime;
                
                // Обновление физики
                updatePhysics(dt);
                
                // Создание снимка состояния
                PotState state = getCurrentState();
                
                // Уведомление подписчиков об обновлении состояния
                notifyStateUpdate(state);
                
                // Проверка событий
                checkAndNotifyEvents(state);
                
                // Пауза между обновлениями (100 мс)
                Thread.sleep(100);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Обновляет физическое состояние модели.
     */
    private void updatePhysics(double dt) {
        synchronized (this) {
            // Вычисление теплопотерь
            double heatLossCoefficient = isLidOn 
                ? HEAT_TRANSFER_COEFFICIENT_WITH_LID 
                : HEAT_TRANSFER_COEFFICIENT;
            
            double temperatureDiff = temperature - AMBIENT_TEMPERATURE;
            double heatLoss = heatLossCoefficient * temperatureDiff * dt;
            
            // Вычисление притока тепла от конфорки
            double heatInput = MAX_HEAT_POWER * burnerLevel * dt;
            
            // Изменение температуры
            if (volume > 0) {
                double mass = volume * WATER_DENSITY; // Масса воды в кг
                double energyChange = heatInput - heatLoss;
                double temperatureChange = energyChange / (mass * HEAT_CAPACITY);
                temperature += temperatureChange;
                
                // Ограничение температуры при кипении
                if (temperature >= BOILING_TEMPERATURE) {
                    temperature = BOILING_TEMPERATURE;
                    
                    // Испарение при кипении - разная скорость в зависимости от крышки
                    if (burnerLevel > 0) {
                        double evaporationRate = isLidOn ? EVAPORATION_RATE_WITH_LID : EVAPORATION_RATE;
                        double evaporation = evaporationRate * burnerLevel * dt;
                        volume = Math.max(0.0, volume - evaporation);
                    }
                    
                    if (!isBoiling) {
                        isBoiling = true;
                    }
                } else {
                    if (isBoiling) {
                        isBoiling = false;
                    }
                }
                
                // Температура может быть ниже температуры окружающей среды
                // (например, при доливании холодной воды)
                // Ограничение только при отсутствии воды
            } else {
                // Если воды нет, температура выравнивается с окружающей
                if (temperature > AMBIENT_TEMPERATURE) {
                    temperature = AMBIENT_TEMPERATURE;
                }
                isBoiling = false;
            }
        }
    }
    
    /**
     * Получает текущее состояние кастрюли.
     */
    private PotState getCurrentState() {
        synchronized (this) {
            return new PotState(temperature, volume, isLidOn, burnerLevel, isBoiling);
        }
    }
    
    /**
     * Проверяет события и уведомляет подписчиков.
     */
    private void checkAndNotifyEvents(PotState newState) {
        synchronized (this) {
            // Проверка начала кипения
            if (newState.isBoiling() && !wasBoiling) {
                notifyEvent(new BoilingStartedEvent(newState.temperature(), newState.volume()));
            }
            
            // Проверка прекращения кипения
            if (!newState.isBoiling() && wasBoiling) {
                notifyEvent(new BoilingStoppedEvent(newState.temperature(), newState.volume()));
            }
            
            // Обновление предыдущего состояния
            wasBoiling = newState.isBoiling();
        }
    }
    
    /**
     * Уведомляет всех подписчиков об обновлении состояния.
     */
    private void notifyStateUpdate(PotState state) {
        synchronized (listeners) {
            for (PotStateListener listener : listeners) {
                try {
                    listener.onStateUpdate(state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Уведомляет всех подписчиков о событии.
     */
    private void notifyEvent(PotEvent event) {
        synchronized (listeners) {
            for (PotStateListener listener : listeners) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

