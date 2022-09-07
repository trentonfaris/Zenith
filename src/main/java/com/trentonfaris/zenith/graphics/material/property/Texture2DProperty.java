package com.trentonfaris.zenith.graphics.material.property;

import com.trentonfaris.zenith.graphics.texture.Texture2D;

import java.util.Objects;

public final class Texture2DProperty extends Property {
    public Texture2D value;

    @Override
    public Texture2DProperty copy() {
        Texture2DProperty copy = new Texture2DProperty();
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
        Texture2DProperty that = (Texture2DProperty) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
