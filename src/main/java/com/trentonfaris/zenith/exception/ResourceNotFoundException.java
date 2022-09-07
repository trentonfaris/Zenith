package com.trentonfaris.zenith.exception;

import java.io.FileNotFoundException;
import java.io.Serial;

/**
 * A {@link ResourceNotFoundException} indicates that a resource cannot not be
 * found.
 *
 * @author Trenton Faris
 */
public class ResourceNotFoundException extends FileNotFoundException {
	@Serial
	private static final long serialVersionUID = 4046510700645867493L;

	/**
	 * Creates a new {@link ResourceNotFoundException} with the specified resource.
	 *
	 * @param resource The resource that could not be found
	 */
	public ResourceNotFoundException(String resource) {
		super("Cannot find the resource: " + resource);
	}
}
