package com.lab4.domain;

/**
 * Событие прекращения кипения воды в кастрюле.
 */
public record BoilingStoppedEvent(double temperature, double volume) implements PotEvent {
}

