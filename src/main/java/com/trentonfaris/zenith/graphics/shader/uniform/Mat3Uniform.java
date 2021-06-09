package com.trentonfaris.zenith.graphics.shader.uniform;

import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public final class Mat3Uniform extends Uniform {

	public Mat3Uniform(int program, String name, UniformType uniformBinding) {
		super(program, name, uniformBinding);
	}

	public void set(Matrix3f value) {
		if (location < 0 || value == null) {
			return;
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer pMatrix = stack.mallocFloat(9);
			value.get(pMatrix);

			GL20.glUniformMatrix3fv(location, false, pMatrix);
		}
	}
}
