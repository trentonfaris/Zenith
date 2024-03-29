package com.trentonfaris.zenith.resource;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;

import java.net.URI;

/**
 * The {@link ResourceLoader} class is the base class for logic responsible for
 * loading various types of system resources.
 *
 * @author Trenton Faris
 */
public abstract class ResourceLoader<T> {
    /**
     * The scheme, or identifier, for the type of resource this loader loads.
     */
    private final String scheme;

    /**
     * Creates a new {@link ResourceLoader} with the specified scheme.
     *
     * @param scheme The target scheme for this {@link ResourceLoader}
     */
    ResourceLoader(String scheme) {
        if (scheme == null || scheme.isEmpty()) {
            String errorMsg = "Cannot create a ResourceLoader from a null or empty scheme.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.scheme = scheme;
    }

    /**
     * Loads a resource from the specified {@link URI}.
     *
     * @param uri The target {@link URI} to load.
     * @return The loaded resource.
     * @throws ResourceIOException       A throwable {@link ResourceIOException}
     * @throws ResourceNotFoundException A throwable {@link ResourceNotFoundException}
     */
    public abstract T load(URI uri) throws ResourceIOException, ResourceNotFoundException;

    /**
     * Gets the scheme for the type of resource this loader loads.
     *
     * @return The {@link #scheme} value.
     */
    public String getScheme() {
        return scheme;
    }
}
