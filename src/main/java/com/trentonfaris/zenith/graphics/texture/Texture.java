package com.trentonfaris.zenith.graphics.texture;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;

public abstract class Texture implements Copyable, Disposable {
	/** The texture buffer object of this {@link Texture}. */
	protected final int tbo;

	/** The {@link Target} to which this {@link Texture} is bound. */
	protected final Target target;

	/**
	 * Creates a new {@link Texture} with the specified target.
	 *
	 * @param target
	 */
	public Texture(Target target) {
		if (target == null) {
			String errorMsg = "Cannot create a Texture from a null target.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pTbo = stack.mallocInt(1);
			GL11.glGenTextures(pTbo);

			this.tbo = pTbo.get();
		}

		this.target = target;
	}

	@Override
	public abstract Texture copy();

	@Override
	public void dispose() {
		GL11.glDeleteTextures(tbo);
	}

	/**
	 * Gets the {@link #tbo}.
	 *
	 * @return The {@link #tbo} value.
	 */
	public final int getTbo() {
		return tbo;
	}

	/**
	 * Gets the {@link #target}.
	 *
	 * @return The {@link #target} value.
	 */
	public final Target getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + tbo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Texture other = (Texture) obj;
		if (target != other.target)
			return false;
		if (tbo != other.tbo)
			return false;
		return true;
	}
}
