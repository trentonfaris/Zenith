package com.trentonfaris.zenith.graphics.material.property;

public final class IntProperty extends Property {
    public int value = 0;

    public IntProperty copy() {
        IntProperty copy = new IntProperty();
        copy.value = value;

        return copy;
    }

    public void dispose() {

    }
}
