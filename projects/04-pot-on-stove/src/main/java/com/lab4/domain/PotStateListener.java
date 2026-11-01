package com.lab4.domain;

/**
 * Интерфейс подписчика на обновления состояния кастрюли и события.
 */
public interface PotStateListener {
    /**
     * Вызывается при обновлении состояния кастрюли.
     * @param state новое состояние кастрюли
     */
    void onStateUpdate(PotState state);

    /**
     * Вызывается при возникновении события.
     * @param event событие, произошедшее в модели
     */
    void onEvent(PotEvent event);
}

