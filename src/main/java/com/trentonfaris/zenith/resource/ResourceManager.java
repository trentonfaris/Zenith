package com.trentonfaris.zenith.resource;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceLoaderNotFoundException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The {@link ResourceManager} is responsible for loading and caching resources from a
 * list of managed unique {@link ResourceLoader}s.
 *
 * @author Trenton Faris
 */
public final class ResourceManager implements Disposable {
    /**
     * The {@link File} indicated the location of the resources directory.
     */
    public static final File RESOURCES_DIRECTORY = new File("resources");

    /**
     * The list of loaders.
     */
    private final Map<Class<? extends ResourceLoader<?>>, ResourceLoader<?>> resourceLoaders = new HashMap<>();

    /**
     * The cache of loaded resources.
     */
    private final Map<URI, Object> cache = new HashMap<>();

    public ResourceManager() {
        if (!ResourceManager.RESOURCES_DIRECTORY.exists()) {
            // TODO : handle return value
            ResourceManager.RESOURCES_DIRECTORY.mkdir();
        }
    }

    @Override
    public void dispose() {
        for (Object object : cache.values()) {
            if (object instanceof Disposable disposable) {
                disposable.dispose();
            }
        }

        cache.clear();
    }

    /**
     * Registers a {@link ResourceLoader} of the specified type with this
     * {@link ResourceManager}. If necessary, creates a directory in the resources folder
     * for the specified resource loader's scheme.
     */
    public <S, T extends ResourceLoader<S>> void registerResourceLoader(Class<T> resourceLoaderType) {
        Zenith.getLogger().log(Level.INFO, "Registering resource loader: " + resourceLoaderType.getSimpleName());

        if (resourceLoaders.containsKey(resourceLoaderType)) {
            Zenith.getLogger().warn(
                    "A ResourceLoader of type " + resourceLoaderType + " is already registered with this Resources.");
            return;
        }

        ResourceLoader<S> resourceLoader;
        try {
            Constructor<T> constructor = resourceLoaderType.getConstructor();
            resourceLoader = constructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            String errorMsg = "Cannot create a ResourceLoader from type: " + resourceLoaderType
                    + ". Must be accessible and contain a no-args constructor.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        resourceLoaders.put(resourceLoaderType, resourceLoader);

        File file = new File(ResourceManager.RESOURCES_DIRECTORY.getPath() + File.separator + resourceLoader.getScheme());
        if (!file.exists()) {
            // TODO : handle return value
            file.mkdir();
        }
    }

    /**
     * Removes a {@link ResourceLoader} of the specified type from this
     * {@link ResourceManager}.
     */
    public <S, T extends ResourceLoader<S>> void removeResourceLoader(Class<T> resourceLoaderType) {
        if (resourceLoaderType == null) {
            return;
        }

        if (!resourceLoaders.containsKey(resourceLoaderType)) {
            return;
        }

        resourceLoaders.remove(resourceLoaderType);
    }

    /**
     * Gets a resource from a {@code String} URI. This call assumes that the
     * inferred return type is actually the correct type of the resource loader's
     * return type.
     *
     * @param uri   The target {@link URI} to load.
     * @param clazz The {@link Class} of the target resource
     * @param <T>   The type of the target resource
     * @return The resource, of inferred type.
     * @throws ResourceIOException       A throwable {@link ResourceIOException}
     * @throws ResourceNotFoundException A throwable {@link ResourceNotFoundException}
     */
    public <T> T getResource(String uri, Class<T> clazz) throws ResourceIOException, ResourceNotFoundException {
        if (uri == null || uri.isEmpty()) {
            String errorMsg = "Cannot get a resource from a null or empty URI.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        URI realURI = toURI(uri);

        if (!cache.containsKey(realURI)) {
            try {
                loadResource(realURI);
            } catch (ResourceLoaderNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }

        T resource = clazz.cast(cache.get(realURI));

        if (resource instanceof Copyable copyable) {
            return clazz.cast(copyable.copy());
        }

        return resource;
    }

    /**
     * Loads a resource into the cache.
     *
     * @param uri The target {@link URI} to load.
     * @throws ResourceIOException             A throwable {@link ResourceIOException}
     * @throws ResourceLoaderNotFoundException A throwable {@link ResourceLoaderNotFoundException}
     * @throws ResourceNotFoundException       A throwable {@link ResourceNotFoundException}
     */
    private void loadResource(URI uri)
            throws ResourceIOException, ResourceLoaderNotFoundException, ResourceNotFoundException {
        String scheme = uri.getScheme();

        ResourceLoader<?> resourceLoader = null;

        for (Entry<Class<? extends ResourceLoader<?>>, ResourceLoader<?>> entry : resourceLoaders
                .entrySet()) {
            if (entry.getValue().getScheme().equalsIgnoreCase(scheme)) {
                resourceLoader = entry.getValue();
            }
        }

        if (resourceLoader == null) {
            Zenith.getLogger().error("Cannot not find the ResourceLoader for scheme: " + scheme);
            throw new ResourceLoaderNotFoundException(scheme);
        }

        Object resource = resourceLoader.load(uri);

        if (resource == null) {
            Zenith.getLogger().error("Cannot read the resource: " + uri);
            throw new ResourceIOException(uri.toString());
        }

        cache.put(uri, resource);
    }

    /**
     * Converts a {@code String} URI to {@link URI}.
     *
     * @return The {@link URI}.
     */
    private URI toURI(String uri) {
        URI realURI;
        try {
            realURI = new URI(uri);
        } catch (URISyntaxException e) {
            String errorMsg = "The specified URI '" + uri + "' is invalid.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        return realURI;
    }
}
