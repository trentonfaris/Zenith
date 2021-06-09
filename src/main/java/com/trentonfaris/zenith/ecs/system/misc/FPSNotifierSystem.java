package com.trentonfaris.zenith.ecs.system.misc;

import com.artemis.BaseSystem;

public class FPSNotifierSystem extends BaseSystem {
    private static final int SAMPLE_SIZE = 100;

    private final double[] samples = new double[SAMPLE_SIZE];
    private int index = 0;
    private double sum = 0;

    private double time = 0;

    @Override
    protected void processSystem() {
        this.sum -= samples[index];
        this.sum += world.delta;
        samples[index] = world.delta;

        this.index += 1;
        if (index == SAMPLE_SIZE) {
            this.index = 0;
        }

        this.time += world.delta;
        if (time >= 1) {
            System.out.println("FPS: " + (SAMPLE_SIZE / sum));

            this.time = 0;
        }
    }
}
