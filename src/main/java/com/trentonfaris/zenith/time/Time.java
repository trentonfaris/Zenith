package com.trentonfaris.zenith.time;

import com.trentonfaris.zenith.Engine;
import com.trentonfaris.zenith.utility.Initializable;
import com.trentonfaris.zenith.utility.Updatable;

/**
 * The {@link Time} class keeps track of engine time.
 *
 * @author Trenton Faris
 */
public final class Time implements Initializable, Updatable {
	/** The factor used to smooth the {@link #deltaTime}. */
	private static final double T = 0.2f;

	/** The time that the {@link Engine} was started, in milliseconds. */
	private long startupTime;

	/**
	 * The time at the beginning of this {@link #update}, in seconds, since the
	 * {@link Engine} was started.
	 */
	private double time;

	/**
	 * The time at the beginning of the last {@link #update}, in seconds, since the
	 * {@link Engine} was started.
	 */
	private double prevTime;

	/** The time it took to complete the last {@link #update}, in seconds. */
	private double deltaTime;

	/** The current smoothed out {@link #deltaTime}, in seconds. */
	private double smoothDeltaTime;

	/** The previous smoothed out {@link #deltaTime}, in seconds. */
	private double prevSmoothDeltaTime;

	@Override
	public void init() {
		this.startupTime = System.currentTimeMillis();
		this.time = 0;
		this.prevTime = 0;
		this.deltaTime = 0;
		this.smoothDeltaTime = 0;
		this.prevSmoothDeltaTime = 0;
	}

	/** Updates the time tracking variables. */
	public void update() {
		double diff = (double) (System.currentTimeMillis() - startupTime);

		this.time = diff / 1000.0;
		this.deltaTime = time - prevTime;
		this.prevTime = time;

		this.smoothDeltaTime = T * deltaTime + (1 - T) * prevSmoothDeltaTime;
		this.prevSmoothDeltaTime = smoothDeltaTime;
	}

	/**
	 * Gets the real time, in seconds, since the {@link Engine} was started.
	 *
	 * @return The real time, in seconds, since the {@link Engine} was started.
	 */
	public double getRealTimeSinceStartup() {
		return (System.currentTimeMillis() - startupTime) / 1000.0;
	}

	/**
	 * Gets the time at the beginning of this {@link #update()}. This is the time,
	 * in seconds, since the {@link Engine} was started.
	 *
	 * @return The {@link #time} at the beginning of this {@link #update}. This is
	 *         the time, in seconds, since the {@link Engine} was started.
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Gets the time, in seconds, it took to complete the last {@link #update}.
	 *
	 * @return The {@link #deltaTime}, in seconds, of the last {@link #update}.
	 */
	public double getDeltaTime() {
		return deltaTime;
	}

	/**
	 * Gets the smoothed out {@link #deltaTime}, in seconds.
	 *
	 * @return The {@link #smoothDeltaTime}, in seconds.
	 */
	public double getSmoothDeltaTime() {
		return smoothDeltaTime;
	}
}
