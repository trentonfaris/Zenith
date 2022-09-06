package com.trentonfaris.zenith.graphics.texture;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.framebuffer.RenderTarget;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A {@link Texture2D} is a 2D image that can be drawn with OpenGL.
 *
 * @author Trenton Faris
 */
public final class Texture2D extends Texture implements RenderTarget {
    /**
     * The {@link InternalFormat} of this {@link Texture2D}.
     */
    private final InternalFormat internalFormat;
    /**
     * The {@link WrappingMode} of this {@link Texture}.
     */
    private final WrappingMode wrappingMode;
    /**
     * The {@link FilteringMode} of this {@link Texture}.
     */
    private final FilteringMode filteringMode;
    /**
     * The width of this {@link Texture2D}.
     */
    private int width;
    /**
     * The height of this {@link Texture2D}.
     */
    private int height;
    /**
     * The {@link PixelFormat} of this {@link Texture2D}.
     */
    private final PixelFormat pixelFormat;
    /**
     * The {@link PixelType} of this {@link Texture2D}.
     */
    private final PixelType pixelType;
    /**
     * The pixel data of this {@link Texture2D}.
     */
    private final Buffer pixelData;

    /**
     * Creates a new {@link Texture2D} with no data from the specified texture
     * format, width, height, pixel format, pixel type, wrapping mode, and filtering
     * mode.
     *
     * @param internalFormat The target {@link InternalFormat}
     * @param width The target width
     * @param height The target height
     * @param pixelFormat The target {@link PixelFormat}
     * @param pixelType The target {@link PixelType}
     * @param pixelData The target {@link Buffer}
     * @param wrappingMode The target {@link WrappingMode}
     * @param filteringMode The target {@link FilteringMode}
     */
    public Texture2D(InternalFormat internalFormat, int width, int height, PixelFormat pixelFormat, PixelType pixelType,
                     Buffer pixelData, WrappingMode wrappingMode, FilteringMode filteringMode) {
        super(Target.TEXTURE_2D);

        if (internalFormat == null) {
            String errorMsg = "Cannot create a Texture2D from a null internalFormat.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (pixelFormat == null) {
            String errorMsg = "Cannot create a Texture2D from a null pixelFormat.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (pixelType == null) {
            String errorMsg = "Cannot create a Texture2D from a null pixelType.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (pixelData != null && !pixelType.getBufferType().isAssignableFrom(pixelData.getClass())) {
            String errorMsg = "The pixelData buffer type does not match pixelType.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (wrappingMode == null) {
            String errorMsg = "Cannot create a Texture2D from a null wrappingMode.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (filteringMode == null) {
            String errorMsg = "Cannot create a Texture2D from a null filteringMode.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.internalFormat = internalFormat;
        this.width = width;
        this.height = height;
        this.pixelFormat = pixelFormat;
        this.pixelType = pixelType;
        this.pixelData = pixelData;
        this.wrappingMode = wrappingMode;
        this.filteringMode = filteringMode;

        update();
    }

    public Texture2D copy() {
        Buffer pixelDataCopy = null;
        if (pixelData instanceof ByteBuffer) {
            pixelDataCopy = MemoryUtil.memAlloc(pixelData.capacity());
            MemoryUtil.memCopy((ByteBuffer) pixelData, (ByteBuffer) pixelDataCopy);
        } else if (pixelData instanceof FloatBuffer) {
            pixelDataCopy = MemoryUtil.memAllocFloat(pixelData.capacity());
            MemoryUtil.memCopy((FloatBuffer) pixelData, (FloatBuffer) pixelDataCopy);
        } else if (pixelData instanceof ShortBuffer) {
            pixelDataCopy = MemoryUtil.memAllocShort(pixelData.capacity());
            MemoryUtil.memCopy((ShortBuffer) pixelData, (ShortBuffer) pixelDataCopy);
        }

        return new Texture2D(InternalFormat.valueOf(internalFormat.name()), width, height,
                PixelFormat.valueOf(pixelFormat.name()), PixelType.valueOf(pixelType.name()), pixelDataCopy,
                WrappingMode.valueOf(wrappingMode.name()), FilteringMode.valueOf(filteringMode.name()));
    }

    public void dispose() {
        super.dispose();

        MemoryUtil.memFree(pixelData);
    }

    public void update() {
        GL11.glBindTexture(target.getValue(), tbo);

        if (pixelType == PixelType.FLOAT) {
            GL11.glTexImage2D(target.getValue(), 0, internalFormat.getValue(), width, height, 0, pixelFormat.getValue(),
                    pixelType.getValue(), (FloatBuffer) pixelData);
        } else if (pixelType == PixelType.UNSIGNED_BYTE) {
            GL11.glTexImage2D(target.getValue(), 0, internalFormat.getValue(), width, height, 0, pixelFormat.getValue(),
                    pixelType.getValue(), (ByteBuffer) pixelData);
        } else if (pixelType == PixelType.UNSIGNED_SHORT) {
            GL11.glTexImage2D(target.getValue(), 0, internalFormat.getValue(), width, height, 0, pixelFormat.getValue(),
                    pixelType.getValue(), (ShortBuffer) pixelData);
        }

        GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_WRAP_S, wrappingMode.getValue());
        GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_WRAP_T, wrappingMode.getValue());

        GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_MIN_FILTER, filteringMode.getValue());
        GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_MAG_FILTER, filteringMode.getValue());

        GL30.glGenerateMipmap(target.getValue());

        GL11.glBindTexture(target.getValue(), 0);
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
     * @param width
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
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the {@link #pixelFormat}.
     *
     * @return The {@link #pixelFormat} value.
     */
    public PixelFormat getPixelFormat() {
        return pixelFormat;
    }

    /**
     * Gets the {@link #pixelType}.
     *
     * @return The {@link #pixelType} value.
     */
    public PixelType getPixelType() {
        return pixelType;
    }

    /**
     * Gets the {@link #pixelData}.
     *
     * @return The {@link #pixelData}.
     */
    public Buffer getPixelData() {
        return pixelData;
    }

    /**
     * Gets the {@link #wrappingMode}.
     *
     * @return The {@link #wrappingMode} value.
     */
    public WrappingMode getWrappingMode() {
        return wrappingMode;
    }

    /**
     * Gets the {@link #filteringMode}.
     *
     * @return The {@link #filteringMode} value.
     */
    public FilteringMode getFilterMode() {
        return filteringMode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((filteringMode == null) ? 0 : filteringMode.hashCode());
        result = prime * result + height;
        result = prime * result + ((internalFormat == null) ? 0 : internalFormat.hashCode());
        result = prime * result + ((pixelData == null) ? 0 : pixelData.hashCode());
        result = prime * result + ((pixelFormat == null) ? 0 : pixelFormat.hashCode());
        result = prime * result + ((pixelType == null) ? 0 : pixelType.hashCode());
        result = prime * result + width;
        result = prime * result + ((wrappingMode == null) ? 0 : wrappingMode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Texture2D other = (Texture2D) obj;
        if (filteringMode != other.filteringMode)
            return false;
        if (height != other.height)
            return false;
        if (internalFormat != other.internalFormat)
            return false;
        if (pixelData == null) {
            if (other.pixelData != null)
                return false;
        } else if (!pixelData.equals(other.pixelData))
            return false;
        if (pixelFormat != other.pixelFormat)
            return false;
        if (pixelType != other.pixelType)
            return false;
        if (width != other.width)
            return false;
        return wrappingMode == other.wrappingMode;
    }
}
