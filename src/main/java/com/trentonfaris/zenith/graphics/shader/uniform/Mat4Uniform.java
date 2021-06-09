package com.trentonfaris.zenith.graphics.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public final class Mat4Uniform extends Uniform {

	public Mat4Uniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(Matrix4f value) {
		if (location < 0 || value == null) {
			return;
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer pMatrix = stack.mallocFloat(16);
			value.get(pMatrix);

			GL20.glUniformMatrix4fv(location, false, pMatrix);
		}
	}
}
