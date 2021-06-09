package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.SamplerCubeUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.resource.resources.Shaders;

public final class SkyboxShader extends Shader {
	// Names of uniforms in the fragment shader.
	public static final String SKYBOX = "skybox";
	// Names of uniforms in the vertex shader.
	private static final String VIEW_PROJECTION = "viewProjection";

	/** Creates a new {@link SkyboxShader}. */
	SkyboxShader() {
		super(Shaders.SKYBOX.getURI());

		// Uniforms in the vertex shader.
		registerUniform(new Mat4Uniform(program, VIEW_PROJECTION, UniformType.VIEW_PROJECTION_MATRIX));

		// Uniforms in the fragment shader.
		registerUniform(new SamplerCubeUniform(program, SKYBOX, UniformType.MATERIAL));
	}
}
