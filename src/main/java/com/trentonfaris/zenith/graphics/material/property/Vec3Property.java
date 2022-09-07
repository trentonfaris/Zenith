package com.trentonfaris.zenith.graphics.material.property;

import org.joml.Vector3f;

import java.util.Objects;

public final class Vec3Property extends Property {
    public Vector3f value = new Vector3f();

    @Override
    public Vec3Property copy() {
        Vec3Property copy = new Vec3Property();
        if (value != null) {
            copy.value = new Vector3f(value);
        }

        return copy;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3Property that = (Vec3Property) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
