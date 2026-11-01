package com.indicator.PeakIndicator.Domain;

import java.time.Instant;

import com.indicator.PeakIndicator.Domain.ValueObjects.LevelValue;

public class PeakMaxThresholdCrossedUp {
    public final LevelValue peak;
    public final LevelValue threshold;
    public final Instant at;
    
    public PeakMaxThresholdCrossedUp(LevelValue peak, LevelValue threshold, Instant at) {
        this.peak = peak; 
        this.threshold = threshold; 
        this.at = at;
    }
}

