package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.graphics.shader.uniform.Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.utility.Disposable;
import org.lwjgl.opengl.GL20;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link Shader} is a program run on the GPU that directs how data is
 * computed in the graphics pipeline.
 *
 * @author Trenton Faris
 */
public abstract class Shader implements Disposable {
    /**
     * The program for this {@link Shader}.
     */
    protected final int program;

    /**
     * The map of uniforms.
     */
    private final Map<String, Uniform> uniforms = new HashMap<>();

    /**
     * Creates a new {@link Shader} from the specified URI.
     *
     * @param uri The {@link URI} of the shader to load
     */
    Shader(String uri) {
        int resource;
        try {
            resource = Zenith.getEngine().getResourceManager().getResource(uri, Integer.class);
        } catch (ResourceNotFoundException | ResourceIOException e) {
            String errorMsg = "Cannot create a Shader from URI: " + uri;
            Zenith.getLogger().error(errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        }

        this.program = resource;
    }

    public final void dispose() {
        GL20.glUseProgram(0);
        GL20.glDeleteProgram(program);
    }

    /**
     * Activates this {@link Shader} in the current rendering state.
     */
    public void use() {
        GL20.glUseProgram(program);
        Zenith.getEngine().getGraphics().getShaderManager().setActiveShader(this);
    }

    /**
     * Registers the specified {@link Uniform} with this {@link Shader}. If an
     * association for the {@link Uniform} name or {@link UniformType} already
     * exists, it will be overridden.
     *
     * @param uniform The target {@link Uniform}
     */
    protected final void registerUniform(Uniform uniform) {
        if (uniform == null) {
            String errorMsg = "Cannot register a Uniform from a null uniform.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        uniforms.put(uniform.getName(), uniform);
    }

    /**
     * Gets an unmodifiable map of the {@link #uniforms}.
     *
     * @return An unmodifiable map of the {@link #uniforms}.
     */
    public final Map<String, Uniform> getUniforms() {
        return Collections.unmodifiableMap(uniforms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shader shader = (Shader) o;
        return program == shader.program;
    }

    @Override
    public int hashCode() {
        return Objects.hash(program);
    }
}
