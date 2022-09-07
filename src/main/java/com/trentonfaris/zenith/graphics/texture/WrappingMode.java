package com.trentonfaris.zenith.graphics.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

/**
 * A {@link WrappingMode} specifies how OpenGL should handle a {@link Texture}
 * who's coordinates reside outside the range [0, 1].
 *
 * @author Trenton Faris
 */
public enum WrappingMode {
	/** Clamps the coordinates of a {@link Texture} between 0 and 1. */
	CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE),

	/** Repeats a mirrored {@link Texture}. */
	MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT),

	/** Repeats a {@link Texture}. */
	REPEAT(GL11.GL_REPEAT);

	/** The OpenGL value of this {@link WrappingMode}. */
	private final int value;

	/**
	 * Creates a new {@link WrappingMode} from the specified OpenGL value.
	 *
	 * @param value The underlying value of this enum
	 */
	WrappingMode(int value) {
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
