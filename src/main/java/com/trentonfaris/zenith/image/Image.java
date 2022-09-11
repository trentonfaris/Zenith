package com.trentonfaris.zenith.image;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * An {@link Image} stores immutable image data.
 *
 * @author Trenton Faris
 */
public record Image(Buffer data, int width, int height, int channels) implements Copyable, Disposable {
    /**
     * Creates a new {@link Image} from the specified pixel data, width, height, and
     * number of channels.
     */
    public Image {
        if (data == null) {
            String errorMsg = "Data buffer cannot be null.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (data.capacity() < 1) {
            String errorMsg = "Data buffer capacity must be positive.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (width < 1 || height < 1 || channels < 1) {
            String errorMsg = "Width, height, and channels must be positive.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (data.capacity() != width * height * channels) {
            String errorMsg = "Data buffer capacity does not match width * height * channels.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    /**
     * Loads an {@link Image} from the specified {@code String} URI.
     *
     * @return The {@link Image}.
     */
    public static Image loadImage(String uri) {
        Image resource;
        try {
            resource = Zenith.getEngine().getResourceManager().getResource(uri, Image.class);
        } catch (ResourceNotFoundException | ResourceIOException e) {
            String errorMsg = "Cannot create an Image from URI: " + uri;
            Zenith.getLogger().error(errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        }

        return resource;
    }

    @Override
    public Image copy() {
        Buffer dataCopy = null;
        if (data instanceof ByteBuffer) {
            dataCopy = MemoryUtil.memAlloc(data.capacity());
            MemoryUtil.memCopy((ByteBuffer) data, (ByteBuffer) dataCopy);
        } else if (data instanceof ShortBuffer) {
            dataCopy = MemoryUtil.memAllocShort(data.capacity());
            MemoryUtil.memCopy((ShortBuffer) data, (ShortBuffer) dataCopy);
        }

        return new Image(dataCopy, width, height, channels);
    }

    @Override
    public void dispose() {
        MemoryUtil.memFree(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Image image = (Image) o;

        if (width != image.width) {
            return false;
        }
        if (height != image.height) {
            return false;
        }
        if (channels != image.channels) {
            return false;
        }
        return data.equals(image.data);
    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + channels;
        return result;
    }
}
