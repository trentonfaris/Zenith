package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Vector2f;

public final class Vec2Property extends Property {
    public Vector2f value = new Vector2f();

    public Vec2Property copy() {
        Vec2Property copy = new Vec2Property();
        if (value != null) {
            copy.value = new Vector2f(value);
        }

        return copy;
    }

    public void dispose() {

    }
}
