package com.trentonfaris.zenith.graphics.model.attribute;

import com.trentonfaris.zenith.Zenith;
import org.joml.Vector2f;

import java.util.Objects;

public final class Vec2Attribute extends Attribute {
    private final Vector2f value;

    public Vec2Attribute(Vector2f value) {
        if (value == null) {
            String errorMsg = "Cannot create a Vec2Attribute from a null value.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.value = value;
    }

    @Override
    public Vec2Attribute copy() {
        return new Vec2Attribute(value);
    }

    @Override
    public int getSize() {
        return 2;
    }

    public Vector2f getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2Attribute that = (Vec2Attribute) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
