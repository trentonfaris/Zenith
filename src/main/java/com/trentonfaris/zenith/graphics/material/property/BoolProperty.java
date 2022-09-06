package com.trentonfaris.zenith.graphics.material.property;

public final class BoolProperty extends Property {
    public boolean value = false;

    public BoolProperty copy() {
        BoolProperty copy = new BoolProperty();
        copy.value = value;

        return copy;
    }

    public void dispose() {

    }
}
