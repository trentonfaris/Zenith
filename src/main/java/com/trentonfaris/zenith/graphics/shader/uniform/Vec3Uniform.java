package com.trentonfaris.zenith.graphics.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public final class Vec3Uniform extends Uniform {

	public Vec3Uniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(Vector3f value) {
		if (location < 0 || value == null) {
			return;
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer pVector = stack.mallocFloat(3);
			value.get(pVector);

			GL20.glUniform3fv(location, pVector);
		}
	}
}
