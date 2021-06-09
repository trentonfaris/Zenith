package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.FloatUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link GammaCorrectionShader} transforms color values from linear space to
 * gamma space.
 *
 * @author Trenton Faris
 */
public final class GammaCorrectionShader extends ScreenShader {
	// Names of uniforms in the fragment shader.
	public static final String GAMMA = "gamma";

	/** Creates a new {@link GammaCorrectionShader}. */
	GammaCorrectionShader() {
		super(Shaders.GAMMA_CORRECTION.getURI());

		// Uniforms in the fragment shader.
		registerUniform(new FloatUniform(program, GAMMA, UniformType.MATERIAL));
	}
}
