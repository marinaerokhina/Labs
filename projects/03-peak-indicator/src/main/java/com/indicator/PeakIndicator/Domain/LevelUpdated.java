package com.indicator.PeakIndicator.Domain;

import java.time.Instant;

import com.indicator.PeakIndicator.Domain.ValueObjects.LevelValue;

public class LevelUpdated {
    public final LevelValue current;
    public final LevelValue peak;
    public final Instant at;

    public LevelUpdated(LevelValue current, LevelValue peak, Instant at) {
        this.current = current; this.peak = peak; this.at = at;
    }
}

