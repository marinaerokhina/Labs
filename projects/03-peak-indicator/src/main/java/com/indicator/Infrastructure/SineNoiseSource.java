package com.indicator.Infrastructure;

import java.util.Random;

import com.indicator.PeakIndicator.Application.DataSource;

public class SineNoiseSource implements DataSource {
    private final Random rnd = new Random();
    private double t = 0.0; // seconds
    private final double step; // seconds per tick (approx)
    
    public SineNoiseSource(double stepSeconds) { 
        this.step = stepSeconds; 
    }
    
    public double next() {
        t += step;

        double s = 0.5 + 0.45 * Math.sin(2 * Math.PI * 0.2 * t) + 0.05 * (rnd.nextDouble() - 0.5) * 2.0;
        
        return Math.max(0.0, Math.min(1.0, s));
    }
}
