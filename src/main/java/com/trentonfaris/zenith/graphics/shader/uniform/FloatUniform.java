package com.trentonfaris.zenith.graphics.shader.uniform;

import org.lwjgl.opengl.GL20;

public final class FloatUniform extends Uniform {

	public FloatUniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(float value) {
		if (location < 0) {
			return;
		}

		GL20.glUniform1f(location, value);
	}
}
