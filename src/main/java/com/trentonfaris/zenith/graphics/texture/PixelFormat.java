package com.trentonfaris.zenith.graphics.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * A {@link PixelFormat} specifies the number and resolution of channels in a
 * pixel data buffer.
 *
 * @author Trenton Faris
 */
public enum PixelFormat {
	/** Pixel data with a depth component. */
	DEPTH(GL11.GL_DEPTH_COMPONENT),

	/** Pixel data with depth and stencil components. */
	DEPTH_STENCIL(GL30.GL_DEPTH_STENCIL),

	/** Pixel data with a single color channel. */
	RED(GL11.GL_RED),

	/** Pixel data with two color channels. */
	RG(GL30.GL_RG),

	/** Pixel data with three color channels. */
	RGB(GL11.GL_RGB),

	/** Pixel data with four color channels. */
	RGBA(GL11.GL_RGBA);

	/** The OpenGL value of this {@link PixelFormat}. */
	private final int value;

	/**
	 * Creates a new {@link PixelFormat} from the specified OpenGL value.
	 *
	 * @param value The underlying value of this enum
	 */
	PixelFormat(int value) {
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
