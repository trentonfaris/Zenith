package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec3Uniform;
import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link ColorShader} draws objects with an unlit color.
 *
 * @author Trenton Faris
 */
public final class ColorShader extends Shader {
	// Names of uniforms in the fragment shader.
	public static final String COLOR = "color";
	// Names of uniforms in the vertex shader.
	private static final String MODEL_VIEW_PROJECTION = "modelViewProjection";

	/** Creates a new {@link ColorShader}. */
	ColorShader() {
		super(Shaders.COLOR.getURI());

		// Uniforms in the vertex shader.
		registerUniform(new Mat4Uniform(program, MODEL_VIEW_PROJECTION, UniformType.MODEL_VIEW_PROJECTION_MATRIX));

		// Uniforms in the fragment shader.
		registerUniform(new Vec3Uniform(program, COLOR, UniformType.MATERIAL));
	}
}
