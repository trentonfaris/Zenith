package com.trentonfaris.zenith.resource.resources;

import com.trentonfaris.zenith.graphics.shader.Shader;
import com.trentonfaris.zenith.graphics.texture.Cubemap;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.resource.ShaderLoader;
import com.trentonfaris.zenith.utility.Utility;

import java.net.URI;

/**
 * The {@link Shaders} {@code enum} contains {@code String} URIs to various
 * shader resources.
 *
 * @author Trenton Faris
 */
public enum Shaders {
	/** Unlit color. */
	COLOR(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME + "/unlit/color"),

	/** Fast approximate anti-aliasing. */
	FXAA(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME + "/screen/effect/fxaa"),

	/** FilmicShader (ACES) tonemapping. */
	FILMIC(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME
			+ "/screen/effect/filmic"),

	/** Gamma correction. */
	GAMMA_CORRECTION(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME
			+ "/screen/effect/gammaCorrection"),

	/** Grayscale conversion. */
	GRAYSCALE(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME
			+ "/screen/effect/grayscale"),

	/** Basic screen. */
	SCREEN(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME + "/screen/screen"),

	/** Skybox from a {@link Cubemap}. */
	SKYBOX(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME + "/skybox/skybox"),

	/** PBR lit. */
	STANDARD(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME
			+ "/standard/standard"),

	/** Unlit {@link Texture2D}. */
	TEXTURE(ShaderLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ShaderLoader.SCHEME + "/unlit/texture");

	/** The {@code String} URI to the resource. */
	private final String uri;

	/**
	 * Creates a new {@link Shaders} from the specified {@code String} URI.
	 *
	 * @param uri The {@link URI} of the {@link Shader} to load
	 */
	Shaders(String uri) {
		this.uri = uri;
	}

	/**
	 * Gets the {@link #uri}.
	 *
	 * @return The {@link #uri}.
	 */
	public String getURI() {
		return uri;
	}
}
