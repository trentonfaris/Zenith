package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.Zenith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The {@link ShaderManager} is a cache of shaders which provide easy access to
 * those objects which have already been instantiated.
 *
 * @author Trenton Faris
 */
public final class ShaderManager {
    /**
     * The list of shaders.
     */
    private final Map<Class<? extends Shader>, Shader> shaders = new HashMap<>();

    /**
     * The active {@link Shader}.
     */
    private Shader activeShader = null;

    public void dispose() {
        // TODO :  Properly dispose of everything and reset for reuse?
        for (Entry<Class<? extends Shader>, Shader> entry : shaders.entrySet()) {
            Shader shader = entry.getValue();

            shader.dispose();
        }
    }

    /**
     * Gets the {@link Shader} with the specified shader type.
     *
     * @param shaderType
     * @return The {@link Shader} with the specified shader type.
     */
    public <T extends Shader> Shader getShader(Class<T> shaderType) {
        if (shaderType == null) {
            String errorMsg = "Cannot get a Shader from a null shaderType.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        Shader shader = shaders.get(shaderType);
        if (shader == null) {
            try {
                Constructor<T> constructor = shaderType.getDeclaredConstructor();
                constructor.setAccessible(true);
                shader = constructor.newInstance();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                String errorMsg = "Cannot create a Shader from type: " + shaderType
                        + ". Must contain a single argument constructor, where the argument is an OpenGL context.";
                Zenith.getLogger().error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            shaders.put(shaderType, shader);
        }

        return shader;
    }

    /**
     * Returns the {@link #activeShader}. May be {@code null} if there is no active
     * shader.
     *
     * @return The {@link #activeShader} or {@code null} if there is no active
     * shader.
     */
    public Shader getActiveShader() {
        return activeShader;
    }

    /**
     * Sets the {@link #activeShader} to the specified shader.
     *
     * @param shader
     */
    void setActiveShader(Shader shader) {
        this.activeShader = shader;
    }
}
