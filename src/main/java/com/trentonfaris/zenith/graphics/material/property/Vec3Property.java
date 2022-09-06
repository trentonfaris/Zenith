package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Vector3f;

public final class Vec3Property extends Property {
    public Vector3f value = new Vector3f();

    public Vec3Property copy() {
        Vec3Property copy = new Vec3Property();
        if (value != null) {
            copy.value = new Vector3f(value);
        }

        return copy;
    }

    public void dispose() {

    }
}
