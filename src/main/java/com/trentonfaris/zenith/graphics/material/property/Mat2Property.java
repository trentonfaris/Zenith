package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Matrix2f;

public final class Mat2Property extends Property {
    public Matrix2f value = new Matrix2f();

    public Mat2Property copy() {
        Mat2Property copy = new Mat2Property();
        if (value != null) {
            copy.value = new Matrix2f(value);
        }

        return copy;
    }

    public void dispose() {

    }
}
