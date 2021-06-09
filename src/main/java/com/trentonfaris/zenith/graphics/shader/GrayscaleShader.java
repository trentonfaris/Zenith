package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link GrayscaleShader} converts a color texture to a grayscale texture.
 *
 * @author Trenton Faris
 */
public final class GrayscaleShader extends ScreenShader {

	/** Creates a new {@link GrayscaleShader}. */
	GrayscaleShader() {
		super(Shaders.GRAYSCALE.getURI());
	}
}
