package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Matrix2f;

import java.util.Objects;

public final class Mat2Property extends Property {
    public Matrix2f value = new Matrix2f();

    @Override
    public Mat2Property copy() {
        Mat2Property copy = new Mat2Property();
        if (value != null) {
            copy.value = new Matrix2f(value);
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mat2Property that = (Mat2Property) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
