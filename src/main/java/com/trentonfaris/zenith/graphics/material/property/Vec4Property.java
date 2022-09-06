package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Vector4f;

public final class Vec4Property extends Property {
    public Vector4f value = new Vector4f();

    public Vec4Property copy() {
        Vec4Property copy = new Vec4Property();
        if (value != null) {
            copy.value = new Vector4f(value);
        }

        return copy;
    }

    public void dispose() {

    }
}
