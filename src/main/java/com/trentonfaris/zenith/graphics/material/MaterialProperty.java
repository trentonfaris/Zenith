package com.trentonfaris.zenith.graphics.material;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.trentonfaris.zenith.graphics.shader.uniform.Uniform;
import com.trentonfaris.zenith.graphics.texture.Cubemap;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.utility.Copyable;

/**
 * A {@link MaterialProperty} stores data to be sent to a mapped
 * {@link Uniform}.
 *
 * @author Trenton Faris
 */
public final class MaterialProperty<T> implements Copyable {
	private final Class<T> type;
	public T value;

	private MaterialProperty(Class<T> type) {
		this.type = type;
	}

	static <T> MaterialProperty<T> of(Class<T> type) {
		return new MaterialProperty<T>(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MaterialProperty<T> copy() {
		MaterialProperty<T> copy = new MaterialProperty<T>(type);
		if (copy.value instanceof Boolean) {
			copy.value = (T) Boolean.valueOf((boolean) value);
		} else if (copy.value instanceof Float) {
			copy.value = (T) Float.valueOf((float) value);
		} else if (copy.value instanceof Integer) {
			copy.value = (T) Integer.valueOf((int) value);
		} else if (copy.value instanceof Matrix2f) {
			copy.value = (T) new Matrix2f((Matrix2f) value);
		} else if (copy.value instanceof Matrix3f) {
			copy.value = (T) new Matrix3f((Matrix3f) value);
		} else if (copy.value instanceof Matrix4f) {
			copy.value = (T) new Matrix4f((Matrix4f) value);
		} else if (copy.value instanceof Texture2D) {
			copy.value = (T) ((Texture2D) value).copy();
		} else if (copy.value instanceof Cubemap) {
			copy.value = (T) ((Cubemap) value).copy();
		} else if (copy.value instanceof Vector2f) {
			copy.value = (T) new Vector2f((Vector2f) value);
		} else if (copy.value instanceof Vector3f) {
			copy.value = (T) new Vector3f((Vector3f) value);
		} else if (copy.value instanceof Vector4f) {
			copy.value = (T) new Vector4f((Vector4f) value);
		} else {
			copy.value = value;
		}

		return copy;
	}

	public Class<T> getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		MaterialProperty<?> other = (MaterialProperty<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
