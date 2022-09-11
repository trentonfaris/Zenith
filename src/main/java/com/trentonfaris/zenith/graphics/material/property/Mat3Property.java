package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Matrix3f;

import java.util.Objects;

public final class Mat3Property extends Property {
    public Matrix3f value = new Matrix3f();

    @Override
    public Mat3Property copy() {
        Mat3Property copy = new Mat3Property();
        if (value != null) {
            copy.value = new Matrix3f(value);
        }


        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mat3Property that = (Mat3Property) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
