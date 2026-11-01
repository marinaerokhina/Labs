package com.indicator.PeakIndicator.Domain;

import java.time.Instant;

import com.indicator.PeakIndicator.Domain.ValueObjects.LevelValue;

public class MaxThresholdExceeded {
    public final LevelValue current;
    public final LevelValue threshold;
    public final Instant at;
    
    public MaxThresholdExceeded(LevelValue current, LevelValue threshold, Instant at) {
        this.current = current; this.threshold = threshold; this.at = at;
    }
}

