package com.trentonfaris.zenith.graphics.framebuffer;

import org.lwjgl.opengl.GL30;

/**
 * An {@link Attachment} specifies a memory location for an attachment that acts
 * as a buffer for a {@link Framebuffer}.
 *
 * @author Trenton Faris
 */
public enum Attachment {
	/** The first color attachment. */
	COLOR0(GL30.GL_COLOR_ATTACHMENT0),

	/** The second color attachment. */
	COLOR1(GL30.GL_COLOR_ATTACHMENT1),

	/** The third color attachment. */
	COLOR2(GL30.GL_COLOR_ATTACHMENT2),

	/** The fourth color attachment. */
	COLOR3(GL30.GL_COLOR_ATTACHMENT3),

	/** The fifth color attachment. */
	COLOR4(GL30.GL_COLOR_ATTACHMENT4),

	/** The sixth color attachment. */
	COLOR5(GL30.GL_COLOR_ATTACHMENT5),

	/** The seventh color attachment. */
	COLOR6(GL30.GL_COLOR_ATTACHMENT6),

	/** The eighth color attachment. */
	COLOR7(GL30.GL_COLOR_ATTACHMENT7),

	/** The ninth color attachment. */
	COLOR8(GL30.GL_COLOR_ATTACHMENT8),

	/** The tenth color attachment. */
	COLOR9(GL30.GL_COLOR_ATTACHMENT9),

	/** A depth attachment. */
	DEPTH(GL30.GL_DEPTH_ATTACHMENT),

	/** A depth and stencil attachment. */
	DEPTH_STENCIL(GL30.GL_DEPTH_STENCIL_ATTACHMENT),

	/** A stencil attachment. */
	STENCIL(GL30.GL_STENCIL_ATTACHMENT);

	/** The OpenGL value of this {@link Attachment}. */
	private final int value;

	/**
	 * Creates a new {@link Attachment} from the specified OpenGL value.
	 *
	 * @param value
	 */
	Attachment(int value) {
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
