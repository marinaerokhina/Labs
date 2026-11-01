package com.indicator.PeakIndicator.Domain.ValueObjects;

public class LevelValue {
    private final double value; // normalized 0..1

    public LevelValue(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) throw new IllegalArgumentException("invalid");
        
        this.value = Math.max(0, Math.min(1, value));
    }

    public double asDouble() { 
        return value; 
    }

    @Override 
    public String toString() { 
        return String.format("%.3f", value); 
    }
}

