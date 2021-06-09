package com.trentonfaris.zenith.graphics.shader.uniform;

import org.lwjgl.opengl.GL20;

public final class IntUniform extends Uniform {

	public IntUniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(int value) {
		if (location < 0) {
			return;
		}

		GL20.glUniform1i(location, value);
	}
}
