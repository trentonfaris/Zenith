package com.trentonfaris.zenith.exception;

import com.trentonfaris.zenith.resource.ResourceLoader;

/**
 * A {@link ResourceLoaderNotFoundException} indicates that a
 * {@link ResourceLoader} cannot be found with a specified scheme.
 *
 * @author Trenton Faris
 */
public class ResourceLoaderNotFoundException extends Exception {
	private static final long serialVersionUID = 4148053455384044740L;

	/**
	 * Creates a new {@link ResourceLoaderNotFoundException} with the specified
	 * scheme.
	 *
	 * @param scheme
	 */
	public ResourceLoaderNotFoundException(String scheme) {
		super("Cannot find the resource loader for scheme: " + scheme);
	}
}
