package com.trentonfaris.zenith.graphics.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public final class Vec2Uniform extends Uniform {

	public Vec2Uniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(Vector2f value) {
		if (location < 0 || value == null) {
			return;
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer pVector = stack.mallocFloat(2);
			value.get(pVector);

			GL20.glUniform2fv(location, pVector);
		}
	}
}
