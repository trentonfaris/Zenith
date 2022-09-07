package com.trentonfaris.zenith.graphics.shader.uniform;

import org.lwjgl.opengl.GL20;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.shader.Shader;

/**
 * A {@link Uniform} enables data to be passed to a {@link Shader}.
 *
 * @author Trenton Faris
 */
public abstract class Uniform {
	/** The program of this {@link Uniform}. */
	private final int program;

	/** The name of this {@link Uniform}. */
	private final String name;

	/** The location of this {@link Uniform}. */
	protected final int location;

	/**
	 * The {@link UniformType} that describes the content of the data in this
	 * {@link Uniform}.
	 */
	private final UniformType uniformType;

	/**
	 * Creates a new {@link Uniform} from the program, name, {@link UniformType} and
	 * {@link UniformType}.
	 *
	 * @param program The OpenGL program value of the {@link Shader}
	 * @param name The name of this {@link Uniform}
	 * @param uniformType The {@link UniformType} of this {@link Uniform}
	 */
	public Uniform(int program, String name, UniformType uniformType) {
		if (name == null || name.isEmpty()) {
			String errorMsg = "Cannot create a Uniform from a null or empty name.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (uniformType == null) {
			String errorMsg = "Cannot create a Uniform from a null uniformType.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.program = program;
		this.name = name;
		this.location = GL20.glGetUniformLocation(program, name);
		this.uniformType = uniformType;
	}

	/**
	 * Gets the {@link #program}.
	 *
	 * @return The {@link #program} value.
	 */
	public int getProgram() {
		return program;
	}

	/**
	 * Gets the {@link #name}.
	 *
	 * @return The {@link #name} value.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the {@link #uniformType}.
	 *
	 * @return The {@link #uniformType} value.
	 */
	public UniformType getUniformType() {
		return uniformType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + location;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + program;
		result = prime * result + ((uniformType == null) ? 0 : uniformType.hashCode());
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
		Uniform other = (Uniform) obj;
		if (location != other.location)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (program != other.program)
			return false;
		return uniformType == other.uniformType;
	}
}
