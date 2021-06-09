package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.FloatUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Sampler2DUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec2Uniform;
import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * An {@link FXAAShader} applies the Fast Approximate Anti-Aliasing algorithm to
 * an image.
 *
 * @author Trenton Faris
 */
public final class FXAAShader extends ScreenShader {

	// Names of uniforms in the fragment shader.
	public static final String GRAYSCALE = "grayscale";
	public static final String TEXEL_SIZE = "texelSize";
	public static final String CONTRAST_THRESHOLD = "contrastThreshold";
	public static final String RELATIVE_THRESHOLD = "relativeThreshold";

	FXAAShader() {
		super(Shaders.FXAA.getURI());

		// Uniforms in the fragment shader.
		registerUniform(new Sampler2DUniform(program, GRAYSCALE, UniformType.MATERIAL));
		registerUniform(new Vec2Uniform(program, TEXEL_SIZE, UniformType.MATERIAL));
		registerUniform(new FloatUniform(program, CONTRAST_THRESHOLD, UniformType.MATERIAL));
		registerUniform(new FloatUniform(program, RELATIVE_THRESHOLD, UniformType.MATERIAL));
	}
}
