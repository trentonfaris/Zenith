package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link FilmicShader} applies a simple ACES ODT(RRT(x)) curve to convert HDR
 * values to LDR.
 *
 * @author Trenton Faris
 */
public final class FilmicShader extends ScreenShader {

	/** Creates a new {@link FilmicShader}. */
	FilmicShader() {
		super(Shaders.FILMIC.getURI());
	}
}
