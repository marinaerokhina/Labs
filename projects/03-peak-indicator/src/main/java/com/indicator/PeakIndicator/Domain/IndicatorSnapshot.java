package com.indicator.PeakIndicator.Domain;

import java.time.Instant;

import com.indicator.PeakIndicator.Domain.ValueObjects.LevelValue;
import com.indicator.PeakIndicator.Domain.ValueObjects.Thresholds;

public class IndicatorSnapshot {
    public final LevelValue current;
    public final LevelValue peak;
    public final Thresholds thresholds;
    public final Instant at;

    public IndicatorSnapshot(LevelValue current, LevelValue peak, Thresholds thresholds, Instant at) {
        this.current = current; this.peak = peak; this.thresholds = thresholds; this.at = at;
    }
}

