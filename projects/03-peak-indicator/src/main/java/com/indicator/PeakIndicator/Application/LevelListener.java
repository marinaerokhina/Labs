package com.indicator.PeakIndicator.Application;

import com.indicator.PeakIndicator.Domain.LevelUpdated;

public interface LevelListener {
    void onLevel(LevelUpdated e);
}

