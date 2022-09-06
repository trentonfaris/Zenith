package com.trentonfaris.zenith.graphics.model;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Model} is a list of meshes.
 *
 * @author Trenton Faris
 */
public final class Model implements Copyable, Disposable {
    /**
     * The list of meshes .
     */
    private final List<Mesh> meshes = new ArrayList<>();

    /**
     * Creates a new {@link Model} with no meshes.
     */
    public Model() {
    }

    /**
     * Creates a new {@link Model} with the specified meshes.
     */
    public Model(List<Mesh> meshes) {
        this.meshes.addAll(meshes);
    }

    /**
     * Loads a {@link Model} from the {@code String} URI.
     *
     * @param uri The URI of the model to load
     * @return The {@link Model}.
     */
    public static Model loadModel(String uri) {
        Model resource;
        try {
            resource = Zenith.getEngine().getResourceManager().getResource(uri);
        } catch (ResourceNotFoundException | ResourceIOException e) {
            String errorMsg = "Cannot create a Model from URI: " + uri;
            Zenith.getLogger().error(errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        }

        return resource;
    }

    public Model copy() {
        List<Mesh> meshes = new ArrayList<>();
        for (Mesh mesh : this.meshes) {
            meshes.add(mesh.copy());
        }

        return new Model(meshes);
    }

    public void dispose() {
        for (Mesh mesh : meshes) {
            mesh.dispose();
        }
    }

    public void addMesh(Mesh mesh) {
        meshes.add(mesh);
    }

    public void removeMesh(Mesh mesh) {
        meshes.remove(mesh);
    }

    /**
     * Gets the list of {@link #meshes}.
     *
     * @return The list of {@link #meshes}.
     */
    public List<Mesh> getMeshes() {
        return meshes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Model model = (Model) o;

        return meshes.equals(model.meshes);
    }

    @Override
    public int hashCode() {
        return meshes.hashCode();
    }
}
