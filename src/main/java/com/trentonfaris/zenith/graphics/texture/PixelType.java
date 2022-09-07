package com.trentonfaris.zenith.graphics.texture;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.opengl.GL11;

/**
 * A {@link PixelType} specifies the data type of a pixel data buffer.
 *
 * @author Trenton Faris
 */
public enum PixelType {
	/** Floating point pixel data. */
	FLOAT(GL11.GL_FLOAT, FloatBuffer.class),

	/** Unsigned byte pixel data. */
	UNSIGNED_BYTE(GL11.GL_UNSIGNED_BYTE, ByteBuffer.class),

	/** Unsigned short pixel data. */
	UNSIGNED_SHORT(GL11.GL_UNSIGNED_SHORT, ShortBuffer.class);

	/** The OpenGL value of this {@link PixelType}. */
	private final int value;

	/** The {@link Buffer} type of this {@link PixelType}. */
	private final Class<? extends Buffer> bufferType;

	/**
	 * Creates a new {@link PixelFormat} from the specified OpenGL value.
	 *
	 * @param value The underlying value of this enum
	 * @param bufferType A class associated with this enum
	 * @param <T> A generic encapsulating the  class type
	 */
	<T extends Buffer> PixelType(int value, Class<T> bufferType) {
		this.value = value;
		this.bufferType = bufferType;
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
	 * Gets the {@link #bufferType}.
	 *
	 * @return The {@link #bufferType}.
	 */
	public Class<? extends Buffer> getBufferType() {
		return bufferType;
	}
}
