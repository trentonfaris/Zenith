package com.trentonfaris.zenith.demo.resource.resources;

import com.trentonfaris.zenith.resource.ImageLoader;
import com.trentonfaris.zenith.utility.Utility;

/**
 * The {@link DemoImages} {@code enum} contains {@code String} URIs to various
 * image resources.
 *
 * @author Trenton Faris
 */
public enum DemoImages {
	/** Rock albedo. */
	ROCK_ALBEDO(ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME
			+ "/demo/rock/albedo.png"),

	/** Rock roughness. */
	ROCK_ROUGHNESS(ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME
			+ "/demo/rock/roughness.png"),

	/** Rock ao. */
	ROCK_AO(ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/demo/rock/ao.png"),

	/** Rock normal. */
	ROCK_NORMAL(ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME
			+ "/demo/rock/normal.png"),

	/** Rock height. */
	ROCK_HEIGHT(ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME
			+ "/demo/rock/height.png");

	/** The {@code String} URI to the resource. */
	private final String uri;

	/** Creates a new {@link DemoImages} from the specified {@code String} URI. */
	DemoImages(String uri) {
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
