package com.trentonfaris.zenith.graphics.material.property;

import java.util.Objects;

public final class FloatProperty extends Property {
    public float value = 0.0f;

    @Override
    public FloatProperty copy() {
        FloatProperty copy = new FloatProperty();
        copy.value = value;

        return copy;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatProperty that = (FloatProperty) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
