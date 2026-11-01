package com.lab4.domain;

/**
 * Событие начала кипения воды в кастрюле.
 */
public record BoilingStartedEvent(double temperature, double volume) implements PotEvent {
}

