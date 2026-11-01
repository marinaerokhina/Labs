package com.indicator.PeakIndicator.Domain;

import java.time.Instant;

import com.indicator.PeakIndicator.Domain.ValueObjects.LevelValue;

public class PeakMaxThresholdCrossedDown {
    public final LevelValue peak;
    public final LevelValue threshold;
    public final Instant at;
    
    public PeakMaxThresholdCrossedDown(LevelValue peak, LevelValue threshold, Instant at) {
        this.peak = peak; 
        this.threshold = threshold; 
        this.at = at;
    }
}

