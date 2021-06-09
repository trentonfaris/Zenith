package com.trentonfaris.zenith.graphics.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

import com.trentonfaris.zenith.graphics.framebuffer.Renderbuffer;

/**
 * A {@link InternalFormat} specifies the number and resolution of channels in a
 * {@link Renderbuffer} or {@link Texture}.
 *
 * @author Trenton Faris
 */
public enum InternalFormat {
	/** A {@link Texture} with a single depth channel. */
	DEPTH(GL11.GL_DEPTH_COMPONENT),

	/** A {@link Texture} with a single, 16-bit depth channel. */
	DEPTH16(GL14.GL_DEPTH_COMPONENT16),

	/** A {@link Texture} with a single, 24-bit depth channel. */
	DEPTH24(GL14.GL_DEPTH_COMPONENT24),

	/** A {@link Texture} with a single, 32-bit depth channel. */
	DEPTH32(GL14.GL_DEPTH_COMPONENT32),

	/** A {@link Texture} with a single, 32-bit floating point depth channel. */
	DEPTH32F(GL30.GL_DEPTH_COMPONENT32F),

	/** A {@link Texture} with a depth channel and a stencil channel. */
	DEPTH_STENCIL(GL30.GL_DEPTH_STENCIL),

	/**
	 * A {@link Texture} with a 24-bit depth channel and an 8-bit stencil channel.
	 */
	DEPTH24_STENCIL8(GL30.GL_DEPTH24_STENCIL8),

	/**
	 * A {@link Texture} with a 32-bit floating point depth channel and an 8-bit
	 * stencil channel.
	 */
	DEPTH32F_STENCIL8(GL30.GL_DEPTH32F_STENCIL8),

	/** A {@link Texture} with a single color channel. */
	RED(GL11.GL_RED),

	/** A {@link Texture} with a single 8-bit color channel. */
	R8(GL30.GL_R8),

	/** A {@link Texture} with a single 16-bit color channel. */
	R16(GL30.GL_R16),

	/** A {@link Texture} with a single 16-bit floating point color channel. */
	R16F(GL30.GL_R16F),

	/** A {@link Texture} with a single 32-bit floating point color channel. */
	R32F(GL30.GL_R32F),

	/** A {@link Texture} with two color channels. */
	RG(GL30.GL_RG),

	/** A {@link Texture} with two 8-bit color channels. */
	RG8(GL30.GL_RG8),

	/** A {@link Texture} with two 16-bit color channels. */
	RG16(GL30.GL_RG16),

	/** A {@link Texture} with two 16-bit floating point color channels. */
	RG16F(GL30.GL_RG16F),

	/** A {@link Texture} with two 32-bit floating point color channels. */
	RG32F(GL30.GL_RG32F),

	/** A {@link Texture} with three color channels. */
	RGB(GL11.GL_RGB),

	/** A {@link Texture} with three 8-bit color channels. */
	RGB8(GL11.GL_RGB8),

	/** A {@link Texture} with three 16-bit color channels. */
	RGB16(GL11.GL_RGB16),

	/** A {@link Texture} with three 16-bit floating point color channels. */
	RGB16F(GL30.GL_RGB16F),

	/** A {@link Texture} with three 32-bit floating point color channels. */
	RGB32F(GL30.GL_RGB32F),

	/** A {@link Texture} with three color channels and an alpha channel. */
	RGBA(GL11.GL_RGBA),

	/**
	 * A {@link Texture} with three 8-bit color channels and an 8-bit alpha channel.
	 */
	RGBA8(GL11.GL_RGBA8),

	/**
	 * A {@link Texture} with three 16-bit color channels and a 16-bit alpha
	 * channel.
	 */
	RGBA16(GL11.GL_RGBA16),

	/**
	 * A {@link Texture} with three 16-bit floating point color channels and a
	 * 16-bit floating point alpha channel.
	 */
	RGBA16F(GL30.GL_RGBA16F),

	/**
	 * A {@link Texture} with three 32-bit floating point color channels and a
	 * 32-bit floating point alpha channel.
	 */
	RGBA32F(GL30.GL_RGBA32F),

	/** A {@link Texture} with three sRGB color channels. */
	SRGB(GL21.GL_SRGB),

	/** A {@link Texture} with three 8-bit sRGB color channels. */
	SRGB8(GL21.GL_SRGB8),

	/** A {@link Texture} with three sRGB color channels and an alpha channel. */
	SRGB_ALPHA(GL21.GL_SRGB_ALPHA),

	/**
	 * A {@link Texture} with three 8-bit sRGB color channels and an 8-bit alpha
	 * channel.
	 */
	SRGB8_ALPHA8(GL21.GL_SRGB8_ALPHA8);

	/** The OpenGL value of this {@link InternalFormat}. */
	private final int value;

	/**
	 * Creates a new {@link InternalFormat} from the specified OpenGL value.
	 *
	 * @param value
	 */
	InternalFormat(int value) {
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
