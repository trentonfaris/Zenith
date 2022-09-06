package com.trentonfaris.zenith.input;

import com.trentonfaris.zenith.Zenith;

/**
 * A {@link KeyButtonAxis} assigns a {@link KeyButtonInput} to represent
 * positive and negative input on the {@link Axis}.
 *
 * @author Trenton Faris
 */
public class KeyButtonAxis extends Axis {
	/** The positive {@link KeyButtonInput} of this {@link KeyButtonAxis}. */
	private KeyButtonInput positive;

	/** The negative {@link KeyButtonInput} of this {@link KeyButtonAxis}. */
	private KeyButtonInput negative;

	/**
	 * Creates a new {@link KeyButtonAxis} from the specified name and default
	 * positive and negative inputs.
	 *
	 * @param name
	 */
	public KeyButtonAxis(String name) {
		this(name, new KeyButtonInput(), new KeyButtonInput());
	}

	/**
	 * Creates a new {@link KeyButtonAxis} from the specified name, positive, and
	 * negative inputs.
	 *
	 * @param name
	 * @param positive
	 * @param negative
	 */
	public KeyButtonAxis(String name, KeyButtonInput positive, KeyButtonInput negative) {
		super(name);

		if (positive == null || negative == null) {
			String errorMsg = "Cannot create a KeyButtonAxis from a null KeyButtonInput.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.positive = positive;
		this.negative = negative;
	}

	/**
	 * Gets the {@link #positive} input.
	 *
	 * @return The {@link #positive} input.
	 */
	public KeyButtonInput getPositive() {
		return positive;
	}

	/**
	 * Sets the {@link #positive} input.
	 *
	 * @param positive
	 */
	public void setPositive(KeyButtonInput positive) {
		if (positive == null) {
			String errorMsg = "Cannot set positive to null.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.positive = positive;
	}

	/**
	 * Gets the {@link #negative} input.
	 *
	 * @return The {@link #negative} input.
	 */
	public KeyButtonInput getNegative() {
		return negative;
	}

	/**
	 * Sets the {@link #negative} input.
	 *
	 * @param negative
	 */
	public void setNegative(KeyButtonInput negative) {
		if (negative == null) {
			String errorMsg = "Cannot set negative to null.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.negative = negative;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((negative == null) ? 0 : negative.hashCode());
		result = prime * result + ((positive == null) ? 0 : positive.hashCode());
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
		KeyButtonAxis other = (KeyButtonAxis) obj;
		if (negative == null) {
			if (other.negative != null)
				return false;
		} else if (!negative.equals(other.negative))
			return false;
		if (positive == null) {
			return other.positive == null;
		} else return positive.equals(other.positive);
	}
}
