package com.trentonfaris.zenith.graphics.shader.uniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.trentonfaris.zenith.graphics.texture.Cubemap;

public final class SamplerCubeUniform extends Uniform {

	public SamplerCubeUniform(int program, String name, UniformType uniformType) {
		super(program, name, uniformType);
	}

	public void set(int texture, Cubemap value) {
		if (location < 0 || value == null) {
			return;
		}

		GL20.glUniform1i(location, texture);
		GL13.glActiveTexture(GL20.GL_TEXTURE0 + texture);
		GL11.glBindTexture(value.getTarget().getValue(), value.getTbo());
	}
}
