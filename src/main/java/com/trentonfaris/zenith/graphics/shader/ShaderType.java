package com.trentonfaris.zenith.graphics.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

/**
 * A {@link ShaderType} is a type of shader (duh), its corresponding OpenGL
 * value, and its file extension.
 *
 * @author Trenton Faris
 */
public enum ShaderType {
	/** A vertex shader. */
	VERTEX(GL20.GL_VERTEX_SHADER, ".vert"),

	/** A geometry shader. */
	GEOMETRY(GL32.GL_GEOMETRY_SHADER, ".geom"),

	/** A fragment shader. */
	FRAGMENT(GL20.GL_FRAGMENT_SHADER, ".frag");

	/** The OpenGL value of this {@link ShaderType}. */
	private int value;

	/** The file extension of this {@link ShaderType}. */
	private String extension;

	/**
	 * Creates a new {@link ShaderType} from the specified OpenGL value.
	 *
	 * @param value
	 */
	ShaderType(int value, String extension) {
		this.value = value;
		this.extension = extension;
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
	 * Gets the {@link #extension}.
	 *
	 * @return The {@link #extension}.
	 */
	public String getExtension() {
		return extension;
	}
}
