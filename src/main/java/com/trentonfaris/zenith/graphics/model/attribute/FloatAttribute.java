package com.trentonfaris.zenith.graphics.model.attribute;

import java.util.Objects;

public final class FloatAttribute extends Attribute {
    private final float value;

    public FloatAttribute(float value) {
        this.value = value;
    }

    @Override
    public FloatAttribute copy() {
        return new FloatAttribute(value);
    }

    @Override
    public int getSize() {
        return 1;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatAttribute that = (FloatAttribute) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
