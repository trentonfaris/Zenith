package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Matrix3f;

public final class Mat3Property extends Property {
    public Matrix3f value = new Matrix3f();

    public Mat3Property copy() {
        Mat3Property copy = new Mat3Property();
        if (value != null) {
            copy.value = new Matrix3f(value);
        }


        return copy;
    }

    public void dispose() {

    }
}
