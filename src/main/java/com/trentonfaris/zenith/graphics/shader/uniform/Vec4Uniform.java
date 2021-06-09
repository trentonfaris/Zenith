package com.trentonfaris.zenith.graphics.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public final class Vec4Uniform extends Uniform {

	public Vec4Uniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(Vector4f value) {
		if (location < 0 || value == null) {
			return;
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer pVector = stack.mallocFloat(4);
			value.get(pVector);

			GL20.glUniform4fv(location, pVector);
		}
	}
}
