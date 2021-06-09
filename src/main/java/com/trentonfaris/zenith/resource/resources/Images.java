package com.trentonfaris.zenith.resource.resources;

import com.trentonfaris.zenith.resource.ImageLoader;
import com.trentonfaris.zenith.utility.Utility;

/**
 * The {@link Images} {@code enum} contains {@code String} URIs to various image
 * resources.
 *
 * @author Trenton Faris
 */
public enum Images {
	/** The right side of the skybox. */
	DEFAULT_SKYBOX_RIGHT(
			ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/skybox/right.jpg"),

	/** The left side of the skybox. */
	DEFAULT_SKYBOX_LEFT(
			ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/skybox/left.jpg"),

	/** The top side of the skybox. */
	DEFAULT_SKYBOX_TOP(
			ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/skybox/top.jpg"),

	/** The bottom side of the skybox. */
	DEFAULT_SKYBOX_BOTTOM(
			ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/skybox/bottom.jpg"),

	/** The back side of the skybox. */
	DEFAULT_SKYBOX_BACK(
			ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/skybox/back.jpg"),

	/** The front side of the skybox. */
	DEFAULT_SKYBOX_FRONT(
			ImageLoader.SCHEME + "://" + Utility.PACKAGED_FILE_HOST + "/" + ImageLoader.SCHEME + "/skybox/front.jpg");

	/** The {@code String} URI to the resource. */
	private final String uri;

	/**
	 * Creates a new {@link Images} from the specified {@code String} URI.
	 *
	 * @param uri
	 */
	Images(String uri) {
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
