package com.trentonfaris.zenith.input;

import com.trentonfaris.zenith.Zenith;

/**
 * A {@link MouseMovementAxis} represents mouse movement input along a
 * {@link MouseAxis} with mouse sensitivity.
 *
 * @author Trenton Faris
 */
public final class MouseMovementAxis extends Axis {
	/** The default {@link MouseAxis} of a {@link MouseMovementAxis}. */
	public static final MouseAxis DEFAULT_MOUSE_AXIS = MouseAxis.X_AXIS;

	/** The default sensitivity of a {@link MouseMovementAxis}. */
	public static final float DEFAULT_SENSITIVITY = 0.1f;

	/** The {@link MouseAxis} of this {@link MouseMovementAxis}. */
	private MouseAxis mouseAxis;

	/** The sensitivity of this {@link MouseMovementAxis}. */
	private float sensitivity;

	/**
	 * Creates a new {@link MouseMovementAxis} from the specified name and default
	 * {@link MouseAxis} and sensitivity.
	 *
	 * @param name The name of this {@link MouseMovementAxis}
	 */
	public MouseMovementAxis(String name) {
		this(name, DEFAULT_MOUSE_AXIS, DEFAULT_SENSITIVITY);
	}

	/**
	 * Creates a new {@link MouseMovementAxis} from the specified name,
	 * {@link MouseAxis}, and sensitivity.
	 *
	 * @param name The name of this {@link MouseMovementAxis}
	 * @param mouseAxis The {@link MouseAxis} of this {@link MouseMovementAxis}
	 * @param sensitivity The sensitivity of this {@link MouseMovementAxis}
	 */
	public MouseMovementAxis(String name, MouseAxis mouseAxis, float sensitivity) {
		super(name);

		if (mouseAxis == null) {
			String errorMsg = "Cannot create a MouseMovementAxis from a null mouseAxis.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.mouseAxis = mouseAxis;
		this.sensitivity = sensitivity;
	}

	/**
	 * Gets the {@link #mouseAxis}.
	 *
	 * @return The {@link #mouseAxis} value.
	 */
	public MouseAxis getMouseAxis() {
		return mouseAxis;
	}

	/**
	 * Sets the {@link #mouseAxis}.
	 *
	 * @param mouseAxis The target {@link MouseAxis}
	 */
	public void setMouseAxis(MouseAxis mouseAxis) {
		if (mouseAxis == null) {
			String errorMsg = "Cannot set mouseAxis to null.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.mouseAxis = mouseAxis;
	}

	/**
	 * Gets the {@link #sensitivity}.
	 *
	 * @return The {@link #sensitivity} value.
	 */
	public float getSensitivity() {
		return sensitivity;
	}

	/**
	 * Sets the {@link #sensitivity}.
	 *
	 * @param sensitivity The target sensitivity
	 */
	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mouseAxis == null) ? 0 : mouseAxis.hashCode());
		result = prime * result + Float.floatToIntBits(sensitivity);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MouseMovementAxis other = (MouseMovementAxis) obj;
		if (mouseAxis != other.mouseAxis)
			return false;
		return Float.floatToIntBits(sensitivity) == Float.floatToIntBits(other.sensitivity);
	}

	public enum MouseAxis {
		X_AXIS, Y_AXIS
	}
}
