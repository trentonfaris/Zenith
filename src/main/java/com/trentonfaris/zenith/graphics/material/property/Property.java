package com.trentonfaris.zenith.graphics.material.property;

import com.trentonfaris.zenith.graphics.shader.uniform.Uniform;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;

/**
 * A {@link Property} stores data to be sent to a mapped
 * {@link Uniform}.
 *
 * @author Trenton Faris
 */
public abstract class Property implements Copyable, Disposable {

    @Override
    public abstract Property copy();
}
