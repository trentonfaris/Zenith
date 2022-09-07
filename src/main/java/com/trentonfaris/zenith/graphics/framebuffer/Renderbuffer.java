package com.trentonfaris.zenith.graphics.framebuffer;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.texture.InternalFormat;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public final class Renderbuffer implements RenderTarget {
    /**
     * The {@link InternalFormat} of this {@link Renderbuffer}.
     */
    private final InternalFormat internalFormat;

    /**
     * The width of this {@link Renderbuffer}.
     */
    private int width;

    /**
     * The height of this {@link Renderbuffer}.
     */
    private int height;

    /**
     * The render buffer object of this {@link Renderbuffer}.
     */
    private final int rbo;

    /**
     * Creates a new {@link Renderbuffer} from the specified {@link InternalFormat}, width, and height.
     *
     * @param internalFormat The {@link InternalFormat} of this {@link Renderbuffer}
     * @param width The width of this {@link Renderbuffer}
     * @param height The height of this {@link Renderbuffer}
     */
    public Renderbuffer(InternalFormat internalFormat, int width, int height) {
        if (internalFormat == null) {
            String errorMsg = "Cannot create a renderbuffer from a null internalFormat.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.internalFormat = internalFormat;
        this.width = width;
        this.height = height;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pRbo = stack.mallocInt(1);
            GL30.glGenRenderbuffers(pRbo);

            this.rbo = pRbo.get();
        }

        update();
    }

    public void dispose() {
        GL30.glDeleteRenderbuffers(rbo);
    }

    public void update() {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat.getValue(), width, height);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
    }

    /**
     * Gets the {@link #internalFormat}.
     *
     * @return The {@link #internalFormat} value.
     */
    public InternalFormat getInternalFormat() {
        return internalFormat;
    }

    /**
     * Gets the {@link #width}.
     *
     * @return The {@link #width} value.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the {@link #width}. It is recommended to {@link #update()} after setting
     * the {@link #width}.
     *
     * @param width The target width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the {@link #height}.
     *
     * @return The {@link #height} value.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the {@link #height}. It is recommended to {@link #update()} after
     * setting the {@link #height}.
     *
     * @param height The target height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the {@link #rbo}.
     *
     * @return The {@link #rbo} value.
     */
    public int getRbo() {
        return rbo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + ((internalFormat == null) ? 0 : internalFormat.hashCode());
        result = prime * result + rbo;
        result = prime * result + width;
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
        Renderbuffer other = (Renderbuffer) obj;
        if (height != other.height)
            return false;
        if (internalFormat != other.internalFormat)
            return false;
        if (rbo != other.rbo)
            return false;
        return width == other.width;
    }
}
