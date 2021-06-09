package com.trentonfaris.zenith.graphics.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Matrix2f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public final class Mat2Uniform extends Uniform {

	public Mat2Uniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(Matrix2f value) {
		if (location < 0 || value == null) {
			return;
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer pMatrix = stack.mallocFloat(4);
			value.get(pMatrix);

			GL20.glUniformMatrix2fv(location, false, pMatrix);
		}
	}
}
