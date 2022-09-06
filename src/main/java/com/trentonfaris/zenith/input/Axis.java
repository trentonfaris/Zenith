package com.trentonfaris.zenith.input;

import com.trentonfaris.zenith.Zenith;

/**
 * An {@link Axis} is used to register {@link Input}. Axes are identified by
 * their name.
 *
 * @author Trenton Faris
 */
public abstract class Axis {
	/** The name of this {@link Axis}. */
	private final String name;

	/**
	 * Creates a new {@link Axis} from the specified name.
	 *
	 * @param name
	 */
	public Axis(String name) {
		if (name == null) {
			String errorMsg = "Cannot create an Axis from a null name.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (name.equals("")) {
			String errorMsg = "Cannot create an Axis from an empty name.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.name = name;
	}

	/**
	 * Gets the {@link #name}.
	 *
	 * @return The {@link #name} value.
	 */
	public final String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Axis other = (Axis) obj;
		if (name == null) {
			return other.name == null;
		} else return name.equals(other.name);
	}
}
