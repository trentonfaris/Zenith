package com.trentonfaris.zenith.ecs.system.misc;

import com.artemis.BaseSystem;

/**
 * The {@link FPSNotifierSystem} averages frame times over the last {@link FPSNotifierSystem#SAMPLE_SIZE} frames.
 * It reports this average as frames per second every (1) second.
 *
 * @author Trenton Faris
 */
public class FPSNotifierSystem extends BaseSystem {
    /**
     * The number of samples to use when averaging frame times.
     */
    private static final int SAMPLE_SIZE = 100;

    /**
     * The array of stored frame times.
     */
    private final double[] samples = new double[SAMPLE_SIZE];

    /**
     * A pointer to the current index of {@link FPSNotifierSystem#samples}.
     */
    private int index = 0;

    /**
     * The running sum of the last {@link FPSNotifierSystem#SAMPLE_SIZE} samples.
     */
    private double sum = 0;

    /**
     * A timer used to report the average frames per second.
     */
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

            this.time -= 1;
        }
    }
}
