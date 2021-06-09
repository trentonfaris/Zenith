package com.trentonfaris.zenith.graphics.shader.uniform;

import org.lwjgl.opengl.GL20;

public final class BoolUniform extends Uniform {

	public BoolUniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(boolean value) {
		if (location < 0) {
			return;
		}

		GL20.glUniform1i(location, value ? 1 : 0);
	}
}
