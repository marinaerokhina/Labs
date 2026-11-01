package com.indicator.PeakIndicator.Application;

import com.indicator.PeakIndicator.Domain.MaxThresholdExceeded;
import com.indicator.PeakIndicator.Domain.PeakMaxThresholdCrossedUp;
import com.indicator.PeakIndicator.Domain.PeakMaxThresholdCrossedDown;

public interface ThresholdListener {
    void onExceeded(MaxThresholdExceeded e);
    
    void onPeakCrossedUp(PeakMaxThresholdCrossedUp e);
    
    void onPeakCrossedDown(PeakMaxThresholdCrossedDown e);
}

