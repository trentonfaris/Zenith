package com.trentonfaris.zenith.graphics.material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.shader.Shader;
import com.trentonfaris.zenith.graphics.shader.uniform.BoolUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.FloatUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.IntUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Mat2Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Mat3Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Sampler2DUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.SamplerCubeUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec2Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec3Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec4Uniform;
import com.trentonfaris.zenith.graphics.texture.Cubemap;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;

/**
 * A {@link Material} stores data associated with a {@link Shader}.
 *
 * @author Trenton Faris
 */
public final class Material implements Copyable, Disposable {
	/** The {@link Shader} used by this {@link Material}. */
	private final Class<? extends Shader> shaderType;

	/** The map of material properties. */
	private final Map<String, MaterialProperty<?>> materialProperties = new HashMap<>();

	/**
	 * Creates a new {@link Material} with the specified {@link Shader} type.
	 *
	 * @param shaderType
	 */
	@SuppressWarnings("unchecked")
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

			if (uniform.getUniformType() == UniformType.MATERIAL) {
				MaterialProperty<?> materialProperty;

				// Create a material property corresponding to each uniform and initialize its
				// default value.
				if (uniform instanceof BoolUniform) {
					materialProperty = MaterialProperty.of(Boolean.class);
					((MaterialProperty<Boolean>) materialProperty).value = false;
				} else if (uniform instanceof FloatUniform) {
					materialProperty = MaterialProperty.of(Float.class);
					((MaterialProperty<Float>) materialProperty).value = 0.0f;
				} else if (uniform instanceof IntUniform) {
					materialProperty = MaterialProperty.of(Integer.class);
					((MaterialProperty<Integer>) materialProperty).value = 0;
				} else if (uniform instanceof Mat2Uniform) {
					materialProperty = MaterialProperty.of(Matrix2f.class);
					((MaterialProperty<Matrix2f>) materialProperty).value = new Matrix2f();
				} else if (uniform instanceof Mat3Uniform) {
					materialProperty = MaterialProperty.of(Matrix3f.class);
					((MaterialProperty<Matrix3f>) materialProperty).value = new Matrix3f();
				} else if (uniform instanceof Mat4Uniform) {
					materialProperty = MaterialProperty.of(Matrix4f.class);
					((MaterialProperty<Matrix4f>) materialProperty).value = new Matrix4f();
				} else if (uniform instanceof Sampler2DUniform) {
					materialProperty = MaterialProperty.of(Texture2D.class);
				} else if (uniform instanceof SamplerCubeUniform) {
					materialProperty = MaterialProperty.of(Cubemap.class);
				} else if (uniform instanceof Vec2Uniform) {
					materialProperty = MaterialProperty.of(Vector2f.class);
					((MaterialProperty<Vector2f>) materialProperty).value = new Vector2f();
				} else if (uniform instanceof Vec3Uniform) {
					materialProperty = MaterialProperty.of(Vector3f.class);
					((MaterialProperty<Vector3f>) materialProperty).value = new Vector3f();
				} else if (uniform instanceof Vec4Uniform) {
					materialProperty = MaterialProperty.of(Vector4f.class);
					((MaterialProperty<Vector4f>) materialProperty).value = new Vector4f();
				} else {
					String errorMsg = "Cannot create a MaterialProperty for an unsupported Uniform type.";
					Zenith.getLogger().error(errorMsg);
					throw new IllegalArgumentException(errorMsg);
				}

				materialProperties.put(name, materialProperty);
			}
		}
	}

	@Override
	public Material copy() {
		Material material = new Material(shaderType);

		for (Entry<String, MaterialProperty<?>> entry : materialProperties.entrySet()) {
			String name = entry.getKey();
			MaterialProperty<?> materialProperty = entry.getValue();

			material.materialProperties.put(name, materialProperty.copy());
		}

		return material;
	}

	@Override
	public void dispose() {
		for (Entry<String, MaterialProperty<?>> entry : materialProperties.entrySet()) {
			MaterialProperty<?> materialProperty = entry.getValue();

			if (materialProperty.value instanceof Texture2D) {
				((Texture2D) materialProperty.value).dispose();
			} else if (materialProperty.value instanceof Cubemap) {
				((Cubemap) materialProperty.value).dispose();
			}
		}
	}

	/**
	 * Sends {@link MaterialProperty} data associated with this {@link Material} to
	 * the {@link Shader}. Data types must match the associated {@link Uniform}
	 * {@link VarType}.
	 */
	public void preDraw() {
		Shader shader = Zenith.getEngine().getGraphics().getShaderManager().getShader(shaderType);
		shader.use();

		int numTextures = 0;
		for (Entry<String, MaterialProperty<?>> entry : materialProperties.entrySet()) {
			String name = entry.getKey();
			MaterialProperty<?> materialProperty = entry.getValue();

			if (materialProperty.value == null) {
				continue;
			}

			Uniform uniform = shader.getUniforms().get(name);

			/**
			 * Send material property data to the corresponding shader uniform. We assume
			 * the material property and the corresponding uniform have matching data types.
			 */
			if (uniform instanceof BoolUniform) {
				((BoolUniform) uniform).set((boolean) materialProperty.value);
			} else if (uniform instanceof FloatUniform) {
				((FloatUniform) uniform).set((float) materialProperty.value);
			} else if (uniform instanceof IntUniform) {
				((IntUniform) uniform).set((int) materialProperty.value);
			} else if (uniform instanceof Mat2Uniform) {
				((Mat2Uniform) uniform).set((Matrix2f) materialProperty.value);
			} else if (uniform instanceof Mat3Uniform) {
				((Mat3Uniform) uniform).set((Matrix3f) materialProperty.value);
			} else if (uniform instanceof Mat4Uniform) {
				((Mat4Uniform) uniform).set((Matrix4f) materialProperty.value);
			} else if (uniform instanceof Sampler2DUniform) {
				((Sampler2DUniform) uniform).set(numTextures, (Texture2D) materialProperty.value);
				numTextures++;
			} else if (uniform instanceof SamplerCubeUniform) {
				((SamplerCubeUniform) uniform).set(numTextures, (Cubemap) materialProperty.value);
				numTextures++;
			} else if (uniform instanceof Vec2Uniform) {
				((Vec2Uniform) uniform).set((Vector2f) materialProperty.value);
			} else if (uniform instanceof Vec3Uniform) {
				((Vec3Uniform) uniform).set((Vector3f) materialProperty.value);
			} else if (uniform instanceof Vec4Uniform) {
				((Vec4Uniform) uniform).set((Vector4f) materialProperty.value);
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
	 * Gets an unmodifiable map of the {@link #materialProperties}.
	 *
	 * @return An unmodifiable map of the {@link #materialProperties}.
	 */
	public Map<String, MaterialProperty<?>> getMaterialProperties() {
		return Collections.unmodifiableMap(materialProperties);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((materialProperties == null) ? 0 : materialProperties.hashCode());
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
		if (materialProperties == null) {
			if (other.materialProperties != null)
				return false;
		} else if (!materialProperties.equals(other.materialProperties))
			return false;
		if (shaderType == null) {
			if (other.shaderType != null)
				return false;
		} else if (!shaderType.equals(other.shaderType))
			return false;
		return true;
	}
}
