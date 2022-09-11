package com.trentonfaris.zenith.graphics.model.attribute;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.utility.Copyable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Attribute implements Copyable {

    @Override
    public abstract Attribute copy();

    public abstract int getSize();
}
