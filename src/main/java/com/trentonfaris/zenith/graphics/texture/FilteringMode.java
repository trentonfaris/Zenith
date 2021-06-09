package com.trentonfaris.zenith.graphics.texture;

import org.lwjgl.opengl.GL11;

/**
 * A {@link FilteringMode} specifies how OpenGL should filter a {@link Texture}.
 *
 * @author Trenton Faris
 */
public enum FilteringMode {
	/** Performs bilinear filtering on a {@link Texture}. */
	LINEAR(GL11.GL_LINEAR),

	/** Performs nearest neighbor filtering on a {@link Texture}. */
	NEAREST(GL11.GL_NEAREST);

	/** The OpenGL value of this {@link FilteringMode}. */
	private final int value;

	/**
	 * Creates a new {@link FilteringMode} from the specified OpenGL value.
	 *
	 * @param value
	 */
	FilteringMode(int value) {
		this.value = value;
	}

	/**
	 * Gets the {@link #value}.
	 *
	 * @return The {@link #value}.
	 */
	public int getValue() {
		return value;
	}
}
