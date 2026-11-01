package com.indicator.PeakIndicator.Application;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import com.indicator.PeakIndicator.Domain.*;
import com.indicator.PeakIndicator.Domain.ValueObjects.*;

public class IndicatorService {
    private final Thresholds thresholds;
    private final double peakDecayPerSecond; // linear decay per second (0..1 per sec)
    private final DataSource dataSource;
    private LevelListener levelListener;
    private ThresholdListener thresholdListener;
    
    private final double dataMin;
    private final double dataRange;
        
    private double current = 0.0;
    private double peak = 0.0;
    private Instant lastUpdate = Instant.now();
    
    private boolean wasPeakAboveMax = false;
        
    private final int historyCapacity;
    private final List<Double> history = new ArrayList<>();
        
    public IndicatorService(Thresholds thresholds, double peakDecayPerSecond, int historyCapacity, DataSource dataSource) {
        this(thresholds, peakDecayPerSecond, historyCapacity, dataSource, 0.0, 1.0);
    }
    
    private IndicatorService(Thresholds thresholds, double peakDecayPerSecond, int historyCapacity, DataSource dataSource, double dataMin, double dataRange) {
        this.thresholds = Objects.requireNonNull(thresholds);
        this.peakDecayPerSecond = Math.max(0, peakDecayPerSecond);
        this.historyCapacity = Math.max(1, historyCapacity);
        this.dataSource = Objects.requireNonNull(dataSource);
        this.dataMin = dataMin;
        this.dataRange = dataRange;
    }

    public static IndicatorService createWithRealScale(
            double dataMin,
            double dataMax,
            double thMin,
            double thMax,
            double peakDecayPerSecond,
            int historyCapacity,
            DataSource realDataSource
    ) {
        if (!(dataMax > dataMin)) throw new IllegalArgumentException("dataMax must be > dataMin");
        double range = dataMax - dataMin;
        
        Thresholds normThresholds = new Thresholds(
                new LevelValue(normalize(thMin, dataMin, range)),
                new LevelValue(normalize(thMax, dataMin, range))
        );
        
        DataSource normalized = () -> normalize(realDataSource.next(), dataMin, range);
        
        return new IndicatorService(normThresholds, peakDecayPerSecond, historyCapacity, normalized, dataMin, range);
    }

    private static double normalize(double x, double min, double range) {
        double v = (x - min) / range;
        return Math.max(0.0, Math.min(1.0, v));
    }
    
    private double denormalize(double normalized) {
        return normalized * dataRange + dataMin;
    }

    public void setLevelListener(LevelListener l) { 
        this.levelListener = l; 
    }
    
    public void setThresholdListener(ThresholdListener l) { 
        this.thresholdListener = l; 
    }
        
    public synchronized void onTick() {
        Instant now = Instant.now();
        double dt = Math.max(0.0, Duration.between(lastUpdate, now).toNanos() / 1_000_000_000.0);
        lastUpdate = now;
                
        double raw = dataSource.next();
        current = clamp01(raw);
                
        double decayedPeak = Math.max(0.0, peak - peakDecayPerSecond * dt);
        peak = Math.max(decayedPeak, current);
                
        history.add(current);
        if (history.size() > historyCapacity) history.remove(0);
        
        double realCurrent = denormalize(current);
        double realPeak = denormalize(peak);
        double realMaxThreshold = denormalize(thresholds.max().asDouble());
        double dataMax = dataMin + dataRange;
        
        class RealLevelValue extends LevelValue {
            private final double realVal;
            RealLevelValue(double real, double max) {
                super(max > 0 ? real / max : 0);
                this.realVal = real;
            }
            @Override public String toString() { return String.format("%.1f", realVal); }
        }
        
        RealLevelValue realCurrentLv = new RealLevelValue(realCurrent, dataMax);
        RealLevelValue realPeakLv = new RealLevelValue(realPeak, dataMax);
        RealLevelValue realMaxThresholdLv = new RealLevelValue(realMaxThreshold, dataMax);

        LevelUpdated levelEvent = new LevelUpdated(realCurrentLv, realPeakLv, now);
        if (levelListener != null) levelListener.onLevel(levelEvent);
                
        if (current >= thresholds.max().asDouble()) {
            if (thresholdListener != null) thresholdListener.onExceeded(new MaxThresholdExceeded(realCurrentLv, realMaxThresholdLv, now));
        }
        
        boolean isPeakAboveMax = peak >= thresholds.max().asDouble();
        
        if (!wasPeakAboveMax && isPeakAboveMax && thresholdListener != null) {
            thresholdListener.onPeakCrossedUp(new PeakMaxThresholdCrossedUp(
                realPeakLv, realMaxThresholdLv, now));
        }
        
        if (wasPeakAboveMax && !isPeakAboveMax && thresholdListener != null) {
            thresholdListener.onPeakCrossedDown(new PeakMaxThresholdCrossedDown(
                realPeakLv, realMaxThresholdLv, now));
        }
        
        wasPeakAboveMax = isPeakAboveMax;
    }
        
    public synchronized IndicatorSnapshot snapshot() {
        return new IndicatorSnapshot(new LevelValue(current), new LevelValue(peak), thresholds, lastUpdate);
    }
        
    public synchronized List<Double> getHistory() { 
        return List.copyOf(history); 
    }
        
    private static double clamp01(double v) { 
        return Math.max(0.0, Math.min(1.0, v));
    }
}

