package com.trentonfaris.zenith.graphics.material.property;

import java.util.Objects;

public final class BoolProperty extends Property {
    public boolean value = false;

    @Override
    public BoolProperty copy() {
        BoolProperty copy = new BoolProperty();
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
        BoolProperty property = (BoolProperty) o;
        return value == property.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
