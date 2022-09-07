package com.trentonfaris.zenith.graphics.model.attribute;

import org.joml.Vector4f;

import java.util.Objects;

public final class Vec4Attribute extends Attribute {
    public Vector4f value;

    public Vec4Attribute(Vector4f value) {
        this.value = value;
    }

    @Override
    public Vec4Attribute copy() {
        return new Vec4Attribute(value);
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec4Attribute that = (Vec4Attribute) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
