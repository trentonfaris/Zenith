package com.trentonfaris.zenith.scheduler;

import com.trentonfaris.zenith.Zenith;

public class Scheduler {
    private static final int TICKS_PER_SEC = 60;

    private double timeBuffer = 0.0;

    public void update() {
        this.timeBuffer += Zenith.getEngine().getTimer().getDeltaTime();
    }
}
