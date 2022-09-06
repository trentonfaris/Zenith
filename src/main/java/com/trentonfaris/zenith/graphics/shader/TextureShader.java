package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Sampler2DUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link TextureShader} draws objects with an unlit {@link Texture2D}.
 *
 * @author Trenton Faris
 */
public class TextureShader extends Shader {
	// Names of uniforms in the fragment shader.
	public static final String TEXTURE2D = "texture2D";
	// Names of uniforms in the vertex shader.
	private static final String MODEL_VIEW_PROJECTION = "modelViewProjection";

	/** Creates a new {@link TextureShader}. */
	TextureShader() {
		this(Shaders.TEXTURE.getURI());
	}

	/**
	 * Creates a new {@link TextureShader} from the specified {@code String} URI.
	 */
	TextureShader(String uri) {
		super(uri);

		// Uniforms in the vertex shader.
		registerUniform(new Mat4Uniform(program, MODEL_VIEW_PROJECTION, UniformType.MODEL_VIEW_PROJECTION_MATRIX));

		// Uniforms in the fragment shader.
		registerUniform(new Sampler2DUniform(program, TEXTURE2D, UniformType.MATERIAL));
	}
}
