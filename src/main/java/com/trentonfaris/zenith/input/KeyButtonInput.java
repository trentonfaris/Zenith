package com.trentonfaris.zenith.input;

import com.trentonfaris.zenith.Zenith;

/**
 * A {@link KeyButtonInput} represents input from a keyboard key or a mouse
 * button. The type of input is specified by the {@link KeyButtonType} enum.
 *
 * @author Trenton Faris
 */
public final class KeyButtonInput {
	/** The default {@link KeyButtonType} of a {@link KeyButtonInput}. */
	public static final KeyButtonType DEFAULT_KEY_BUTTON_TYPE = KeyButtonType.KEY;

	/** The default value of a {@link KeyButtonInput}. */
	public static final int DEFAULT_VALUE = 0;

	/** The {@link KeyButtonType} of this {@link KeyButtonInput}. */
	private KeyButtonType keyButtonType;

	/** The value of this {@link KeyButtonInput}. */
	private int value;

	/**
	 * Creates a new {@link KeyButtonInput} with default {@link KeyButtonType} and
	 * value.
	 */
	public KeyButtonInput() {
		this(DEFAULT_KEY_BUTTON_TYPE, DEFAULT_VALUE);
	}

	/**
	 * Creates a new {@link KeyButtonInput} with the specified {@link KeyButtonType}
	 * and value.
	 *
	 * @param keyButtonType
	 * @param value
	 */
	public KeyButtonInput(KeyButtonType keyButtonType, int value) {
		if (keyButtonType == null) {
			String errorMsg = "Cannot create a KeyButtonInput from a null keyButtonType.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.keyButtonType = keyButtonType;
		this.value = value;
	}

	/**
	 * Gets the {@link #keyButtonType}.
	 *
	 * @return The {@link #keyButtonType}.
	 */
	public KeyButtonType getKeyButtonType() {
		return keyButtonType;
	}

	/**
	 * Sets the {@link #keyButtonType}.
	 *
	 * @param keyButtonType
	 */
	public void setKeyButtonType(KeyButtonType keyButtonType) {
		if (keyButtonType == null) {
			String errorMsg = "Cannot set keyButtonType to null.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.keyButtonType = keyButtonType;
	}

	/**
	 * Gets the {@link #value}.
	 *
	 * @return The {@link #value}.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the {@link #value}.
	 *
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyButtonType == null) ? 0 : keyButtonType.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyButtonInput other = (KeyButtonInput) obj;
		if (keyButtonType != other.keyButtonType)
			return false;
		return value == other.value;
	}

	public enum KeyButtonType {
		KEY, BUTTON
	}
}
