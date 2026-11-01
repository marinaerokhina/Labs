package com.indicator.Infrastructure;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicTimer {
    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "indicator-timer");

        t.setDaemon(true);

        return t;
    });

    public void start(long periodMillis, Runnable tick) {
        exec.scheduleAtFixedRate(tick, 0, periodMillis, TimeUnit.MILLISECONDS);
    }

    public void stop() { 
        exec.shutdownNow(); 
    }
}
