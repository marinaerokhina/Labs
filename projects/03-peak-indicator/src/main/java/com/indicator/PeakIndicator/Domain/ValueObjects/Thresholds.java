package com.indicator.PeakIndicator.Domain.ValueObjects;

public class Thresholds {
    private final LevelValue min;
    private final LevelValue max;

    public Thresholds(LevelValue min, LevelValue max) {
        if (min.asDouble() > max.asDouble()) throw new IllegalArgumentException("min > max");

        this.min = min; this.max = max;
    }

    public LevelValue min() { 
        return min; 
    }

    public LevelValue max() { 
        return max; 
    }
}

