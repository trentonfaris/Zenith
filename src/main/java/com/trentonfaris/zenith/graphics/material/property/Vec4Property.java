package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Vector4f;

import java.util.Objects;

public final class Vec4Property extends Property {
    public Vector4f value = new Vector4f();

    @Override
    public Vec4Property copy() {
        Vec4Property copy = new Vec4Property();
        if (value != null) {
            copy.value = new Vector4f(value);
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec4Property that = (Vec4Property) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
