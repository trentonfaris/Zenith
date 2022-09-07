package com.trentonfaris.zenith.graphics.model;

import org.lwjgl.opengl.GL11;

/**
 * A {@link PrimitiveType} defines which primitive to use when drawing a
 * {@link Mesh}.
 *
 * @author Trenton Faris
 */
public enum PrimitiveType {
	/** The vertices of a {@link Mesh}. */
	POINTS(GL11.GL_POINTS),

	/** Lines between vertices of a {@link Mesh}, defined by the indices. */
	LINES(GL11.GL_LINES),

	/** Lines between vertices of a {@link Mesh}, defined by the indices. */
	LINE_STRIP(GL11.GL_LINE_STRIP),

	/** Triangles, or faces, of a {@link Mesh}, defined by the indices. */
	TRIANGLES(GL11.GL_TRIANGLES);

	/** The OpenGL value of this {@link PrimitiveType}. */
	private final int value;

	/**
	 * Creates a new {@link PrimitiveType} from the specified OpenGL value.
	 *
	 * @param value The underlying value of this enum
	 */
	PrimitiveType(int value) {
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
