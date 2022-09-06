package com.trentonfaris.zenith.graphics.material.property;

import com.trentonfaris.zenith.graphics.texture.Texture2D;

public final class Texture2DProperty extends Property {
    public Texture2D value;

    public Texture2DProperty copy() {
        Texture2DProperty copy = new Texture2DProperty();
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
