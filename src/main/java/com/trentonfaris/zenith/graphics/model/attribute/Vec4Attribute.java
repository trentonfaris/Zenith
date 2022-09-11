package com.trentonfaris.zenith.graphics.model.attribute;

import com.trentonfaris.zenith.Zenith;
import org.joml.Vector4f;

import java.util.Objects;

public final class Vec4Attribute extends Attribute {
    private final Vector4f value;

    public Vec4Attribute(Vector4f value) {
        if (value == null) {
            String errorMsg = "Cannot create a Vec4Attribute from a null value.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

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

    public Vector4f getValue() {
        return value;
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
