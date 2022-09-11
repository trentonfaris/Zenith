package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Vector2f;

import java.util.Objects;

public final class Vec2Property extends Property {
    public Vector2f value = new Vector2f();

    @Override
    public Vec2Property copy() {
        Vec2Property copy = new Vec2Property();
        if (value != null) {
            copy.value = new Vector2f(value);
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2Property that = (Vec2Property) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
