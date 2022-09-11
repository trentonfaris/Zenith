package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Matrix4f;

import java.util.Objects;

public final class Mat4Property extends Property {
    public Matrix4f value = new Matrix4f();

    @Override
    public Mat4Property copy() {
        Mat4Property copy = new Mat4Property();
        if (value != null) {
            copy.value = new Matrix4f(value);
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mat4Property that = (Mat4Property) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
