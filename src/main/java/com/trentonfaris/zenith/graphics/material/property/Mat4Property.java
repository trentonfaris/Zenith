package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Matrix4f;

public final class Mat4Property extends Property {
    public Matrix4f value = new Matrix4f();

    public Mat4Property copy() {
        Mat4Property copy = new Mat4Property();
        if (value != null) {
            copy.value = new Matrix4f(value);
        }

        return copy;
    }

    public void dispose() {

    }
}
