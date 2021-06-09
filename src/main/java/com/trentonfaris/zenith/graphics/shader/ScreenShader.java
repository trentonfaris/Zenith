package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.Sampler2DUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link ScreenShader} draws a {@link Texture2D} to the screen.
 *
 * @author Trenton Faris
 */
public class ScreenShader extends Shader {
	// Names of uniforms in the fragment shader.
	public static final String SCREEN = "screen";

	/** Creates a new {@link ScreenShader}. */
	ScreenShader() {
		this(Shaders.SCREEN.getURI());
	}

	/** Creates a new {@link ScreenShader} from the specified {@code String} URI. */
	ScreenShader(String uri) {
		super(uri);

		// Uniforms in the fragment shader.
		registerUniform(new Sampler2DUniform(program, SCREEN, UniformType.MATERIAL));
	}
}
