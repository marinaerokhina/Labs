package com.indicator.Infrastructure;

import java.util.Random;

import com.indicator.PeakIndicator.Application.DataSource;

public class RealSineSource implements DataSource {
    private final Random rnd = new Random();
    private double t = 0.0;
    private final double step;
    private final double min;
    private final double max;
    public RealSineSource(double min, double max, double stepSeconds) {
        if (!(max > min)) throw new IllegalArgumentException("max must be > min");
        this.min = min; this.max = max; this.step = stepSeconds;
    }
    public double next() {
        t += step;
        double center = (min + max) / 2.0;
        double amp = (max - min) * 0.45;
        double noise = ((rnd.nextDouble() - 0.5) * 2.0) * (max - min) * 0.05;
        double v = center + amp * Math.sin(2 * Math.PI * 0.2 * t) + noise;
        // clamp to [min, max]
        return Math.max(min, Math.min(max, v));
    }
}
