package com.trentonfaris.zenith.graphics.material.property;

import com.trentonfaris.zenith.graphics.texture.Cubemap;

public final class CubemapProperty extends Property {
    public Cubemap value;

    public CubemapProperty copy() {
        CubemapProperty copy = new CubemapProperty();
        if (value != null) {
            copy.value = value.copy();
        }

        return copy;
    }

    public void dispose() {
        if (value != null) {
            value.dispose();
        }
    }
}
