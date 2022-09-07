package com.trentonfaris.zenith.graphics.model.attribute;

import org.joml.Vector3f;

import java.util.Objects;

public final class Vec3Attribute extends Attribute {
    public Vector3f value;

    public Vec3Attribute(Vector3f value) {
        this.value = value;
    }

    @Override
    public Vec3Attribute copy() {
        return new Vec3Attribute(value);
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3Attribute that = (Vec3Attribute) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
