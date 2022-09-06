package com.trentonfaris.zenith.graphics.model;

import com.trentonfaris.zenith.Zenith;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

// TODO : Currently only supports floats or vectors of floats (for simplicity when creating the mesh)
public final class Attribute<T> {
    private final int size;
    private T value;

    public Attribute(T value) {
        if (value instanceof Float) {
            this.size = 1;
        } else if (value instanceof Vector2f) {
            this.size = 2;
        } else if (value instanceof Vector3f) {
            this.size = 3;
        } else if (value instanceof Vector4f) {
            this.size = 4;
        } else {
            String errorMsg = "Cannot create an Attribute from an unsupported data type.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public Attribute<T> copy() {
        Attribute<T> copy;
        if (value instanceof Float) {
            copy = new Attribute<T>((T) Float.valueOf((float) value));
        } else if (value instanceof Vector2f) {
            copy = new Attribute<T>((T) new Vector2f((Vector2f) value));
        } else if (value instanceof Vector3f) {
            copy = new Attribute<T>((T) new Vector3f((Vector3f) value));
        } else if (value instanceof Vector4f) {
            copy = new Attribute<T>((T) new Vector4f((Vector4f) value));
        } else {
            String errorMsg = "Cannot copy this Attribute because it is of an unsupported data type.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        return copy;
    }

    public int getSize() {
        return size;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (value == null) {
            String errorMsg = "Cannot set value to null.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.value = value;
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
        Attribute<?> other = (Attribute<?>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
