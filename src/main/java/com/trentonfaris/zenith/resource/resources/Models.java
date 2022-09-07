package com.trentonfaris.zenith.resource.resources;

import com.trentonfaris.zenith.graphics.model.Model;
import com.trentonfaris.zenith.resource.ModelLoader;
import com.trentonfaris.zenith.utility.Utility;

import java.net.URI;

/**
 * The {@link Models} {@code enum} contains {@code String} URIs to various model
 * resources.
 *
 * @author Trenton Faris
 */
public enum Models {
	/** A unit cube. */
	CUBE(ModelLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ModelLoader.SCHEME + "/simple/cube.fbx"),

	/** A unit plane. */
	PLANE(ModelLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ModelLoader.SCHEME + "/simple/plane.fbx"),

	/** An oriented 2x2 plane. */
	SCREEN(ModelLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ModelLoader.SCHEME + "/simple/screen.fbx");

	/** The {@code String} URI to the resource. */
	private final String uri;

	/**
	 * Creates a new {@link Models} from the specified {@code String} URI.
	 *
	 * @param uri The {@link URI} of the {@link Model} to load
	 */
	Models(String uri) {
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
