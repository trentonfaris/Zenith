package com.trentonfaris.zenith.graphics.material.property;

public final class FloatProperty extends Property {
    public float value = 0.0f;

    public FloatProperty copy() {
        FloatProperty copy = new FloatProperty();
        copy.value = value;

        return copy;
    }

    public void dispose() {

    }
}
