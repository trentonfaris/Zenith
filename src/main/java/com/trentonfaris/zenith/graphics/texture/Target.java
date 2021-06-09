package com.trentonfaris.zenith.graphics.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * A {@link Target} specifies the OpenGL texture target to which a
 * {@link Texture} is bound.
 *
 * @author Trenton Faris
 */
public enum Target {
	/** A {@link Texture2D} target. */
	TEXTURE_2D(GL11.GL_TEXTURE_2D),

	/** A {@link Cubemap} target. */
	TEXTURE_CUBE_MAP(GL13.GL_TEXTURE_CUBE_MAP);

	/** The OpenGL value of this {@link Target}. */
	private final int value;

	/** Creates a new {@link Target} from the specified OpenGL value. */
	Target(int value) {
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
