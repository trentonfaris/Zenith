package com.trentonfaris.zenith.graphics.material.property;

import java.util.Objects;

public final class IntProperty extends Property {
    public int value = 0;

    @Override
    public IntProperty copy() {
        IntProperty copy = new IntProperty();
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
        IntProperty that = (IntProperty) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
