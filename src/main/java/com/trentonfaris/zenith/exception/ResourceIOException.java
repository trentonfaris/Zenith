package com.trentonfaris.zenith.exception;

import java.io.IOException;

/**
 * A {@link ResourceIOException} indicates that a resource cannot be read.
 *
 * @author Trenton Faris
 */
public class ResourceIOException extends IOException {
	private static final long serialVersionUID = -2623855150564843216L;

	/**
	 * Creates a new {@link ResourceIOException} with the specified resource.
	 *
	 * @param resource
	 */
	public ResourceIOException(String resource) {
		super("Cannot read the resource: " + resource);
	}
}
