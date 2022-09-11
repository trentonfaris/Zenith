package com.trentonfaris.zenith.graphics.material;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.material.property.*;
import com.trentonfaris.zenith.graphics.shader.Shader;
import com.trentonfaris.zenith.graphics.shader.ShaderType;
import com.trentonfaris.zenith.graphics.shader.uniform.*;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link Material} stores data associated with a {@link Shader}.
 *
 * @author Trenton Faris
 */
public final class Material implements Copyable, Disposable {
    /**
     * The {@link Shader} used by this {@link Material}.
     */
    private final Class<? extends Shader> shaderType;

    /**
     * The map of material properties.
     */
    private final Map<String, Property> properties = new HashMap<>();

    /**
     * Creates a new {@link Material} with the specified {@link Shader} type.
     *
     * @param shaderType The underlying {@link ShaderType}
     */
    public Material(Class<? extends Shader> shaderType) {
        if (shaderType == null) {
            String errorMsg = "Cannot create a Material from a null shaderType.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.shaderType = shaderType;

        Shader shader = Zenith.getEngine().getGraphics().getShaderManager().getShader(shaderType);

        for (Entry<String, Uniform> entry : shader.getUniforms().entrySet()) {
            String name = entry.getKey();
            Uniform uniform = entry.getValue();

            // Create a material property corresponding to each uniform and initialize its
            // default value.
            if (uniform.getUniformType() == UniformType.MATERIAL) {
                if (uniform instanceof BoolUniform) {
                    properties.put(name, new BoolProperty());
                } else if (uniform instanceof FloatUniform) {
                    properties.put(name, new FloatProperty());
                } else if (uniform instanceof IntUniform) {
                    properties.put(name, new IntProperty());
                } else if (uniform instanceof Mat2Uniform) {
                    properties.put(name, new Mat2Property());
                } else if (uniform instanceof Mat3Uniform) {
                    properties.put(name, new Mat3Property());
                } else if (uniform instanceof Mat4Uniform) {
                    properties.put(name, new Mat4Property());
                } else if (uniform instanceof Sampler2DUniform) {
                    properties.put(name, new Texture2DProperty());
                } else if (uniform instanceof SamplerCubeUniform) {
                    properties.put(name, new CubemapProperty());
                } else if (uniform instanceof Vec2Uniform) {
                    properties.put(name, new Vec2Property());
                } else if (uniform instanceof Vec3Uniform) {
                    properties.put(name, new Vec3Property());
                } else if (uniform instanceof Vec4Uniform) {
                    properties.put(name, new Vec4Property());
                } else {
                    String errorMsg = "Cannot create a MaterialProperty for an unsupported Uniform type.";
                    Zenith.getLogger().error(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        }
    }

    @Override
    public Material copy() {
        Material material = new Material(shaderType);

        for (Entry<String, Property> entry : properties.entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();

            material.properties.put(name, property.copy());
        }

        return material;
    }

    @Override
    public void dispose() {
        for (Entry<String, Property> entry : properties.entrySet()) {
            Property property = entry.getValue();
            if (property instanceof Disposable disposable) {
                disposable.dispose();
            }
        }
    }

    /**
     * Sends {@link Property} data associated with this {@link Material} to
     * the {@link Shader}. Data types must match the associated {@link Uniform}
     * {@link UniformType}.
     */
    public void preDraw() {
        Shader shader = Zenith.getEngine().getGraphics().getShaderManager().getShader(shaderType);
        shader.use();

        int numTextures = 0;
        for (Entry<String, Property> entry : properties.entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();

            Uniform uniform = shader.getUniforms().get(name);

            // Send material property data to the corresponding shader uniform.
            if (property instanceof BoolProperty && uniform instanceof BoolUniform) {
                ((BoolUniform) uniform).set(((BoolProperty) property).value);
            } else if (property instanceof FloatProperty && uniform instanceof FloatUniform) {
                ((FloatUniform) uniform).set(((FloatProperty) property).value);
            } else if (property instanceof IntProperty && uniform instanceof IntUniform) {
                ((IntUniform) uniform).set(((IntProperty) property).value);
            } else if (property instanceof Mat2Property && uniform instanceof Mat2Uniform) {
                ((Mat2Uniform) uniform).set(((Mat2Property) property).value);
            } else if (property instanceof Mat3Property && uniform instanceof Mat3Uniform) {
                ((Mat3Uniform) uniform).set(((Mat3Property) property).value);
            } else if (property instanceof Mat4Property && uniform instanceof Mat4Uniform) {
                ((Mat4Uniform) uniform).set(((Mat4Property) property).value);
            } else if (property instanceof Texture2DProperty && uniform instanceof Sampler2DUniform) {
                ((Sampler2DUniform) uniform).set(numTextures, ((Texture2DProperty) property).value);
                numTextures++;
            } else if (property instanceof CubemapProperty && uniform instanceof SamplerCubeUniform) {
                ((SamplerCubeUniform) uniform).set(numTextures, ((CubemapProperty) property).value);
                numTextures++;
            } else if (property instanceof Vec2Property && uniform instanceof Vec2Uniform) {
                ((Vec2Uniform) uniform).set(((Vec2Property) property).value);
            } else if (property instanceof Vec3Property && uniform instanceof Vec3Uniform) {
                ((Vec3Uniform) uniform).set(((Vec3Property) property).value);
            } else if (property instanceof Vec4Property && uniform instanceof Vec4Uniform) {
                ((Vec4Uniform) uniform).set(((Vec4Property) property).value);
            }
        }
    }

    /**
     * Gets the {@link #shaderType}.
     *
     * @return The {@link #shaderType}.
     */
    public Class<? extends Shader> getShaderType() {
        return shaderType;
    }

    /**
     * Gets an unmodifiable map of the {@link #properties}.
     *
     * @return An unmodifiable map of the {@link #properties}.
     */
    public Map<String, Property> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + properties.hashCode();
        result = prime * result + ((shaderType == null) ? 0 : shaderType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Material other = (Material) obj;
        if (!properties.equals(other.properties))
            return false;
        if (shaderType == null) {
            return other.shaderType == null;
        } else return shaderType.equals(other.shaderType);
    }
}
