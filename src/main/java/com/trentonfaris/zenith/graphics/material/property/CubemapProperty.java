package com.trentonfaris.zenith.graphics.material.property;

import com.trentonfaris.zenith.graphics.texture.Cubemap;

import java.util.Objects;

public final class CubemapProperty extends Property {
    public Cubemap value;

    @Override
    public CubemapProperty copy() {
        CubemapProperty copy = new CubemapProperty();
        if (value != null) {
            copy.value = value.copy();
        }

        return copy;
    }

    @Override
    public void dispose() {
        if (value != null) {
            value.dispose();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubemapProperty that = (CubemapProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
