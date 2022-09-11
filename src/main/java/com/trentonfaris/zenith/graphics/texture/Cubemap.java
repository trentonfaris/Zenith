package com.trentonfaris.zenith.graphics.texture;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL21;
import org.lwjgl.system.MemoryUtil;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.image.Image;

public final class Cubemap extends Texture {

	/** Individual images of the {@link Cubemap} */
	private final Image[] images = new Image[6];

	/** Buffers which hold the flipped image data */
	private final Buffer[] buffers = new Buffer[6];

	public Cubemap(Image right, Image left, Image top, Image bottom, Image front, Image back) {
		super(Target.TEXTURE_CUBE_MAP);

		if (right == null) {
			String errorMsg = "Cannot create a Cubemap from a null right.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (left == null) {
			String errorMsg = "Cannot create a Cubemap from a null left.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (top == null) {
			String errorMsg = "Cannot create a Cubemap from a null top.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (bottom == null) {
			String errorMsg = "Cannot create a Cubemap from a null bottom.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (front == null) {
			String errorMsg = "Cannot create a Cubemap from a null front.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (back == null) {
			String errorMsg = "Cannot create a Cubemap from a null back.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		this.images[0] = right;
		this.images[1] = left;
		this.images[2] = top;
		this.images[3] = bottom;
		this.images[4] = front;
		this.images[5] = back;

		init();
	}

	private void init() {
		GL11.glBindTexture(target.getValue(), tbo);

		for (int i = 0; i < images.length; i++) {
			Image image = images[i];

			Buffer data = image.data();
			int width = image.width();
			int height = image.height();
			int channels = image.channels();

			// TODO : Add support for other numbers of channels?
			if (channels != 3) {
				String errorMsg = "Cannot create a Cubemap from an Image that does not have 3 channels.";
				Zenith.getLogger().error(errorMsg);
				throw new IllegalArgumentException(errorMsg);
			}

			// TODO : Don't assume images are byte images

			// Cubemap images start at the top left instead of bottom left. Have to flip
			// them. ¯\_(ツ)_/¯
			byte[] bytes = new byte[data.capacity()];
			((ByteBuffer) data).get(bytes).flip();

			byte[] copy = bytes.clone();
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < width; k++) {
					for (int l = 0; l < channels; l++) {
						bytes[j * width * channels + k * channels + l] = copy[(height - 1 - j) * width * channels
								+ k * channels + l];
					}
				}
			}

			ByteBuffer flipped = MemoryUtil.memAlloc(data.capacity());
			flipped.put(bytes).flip();

			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL21.GL_SRGB, width, height, 0, GL11.GL_RGB,
					GL11.GL_UNSIGNED_BYTE, flipped);

			this.buffers[i] = flipped;
		}

		GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(target.getValue(), GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(target.getValue(), GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL11.glBindTexture(target.getValue(), 0);
	}

	@Override
	public Cubemap copy() {
		return new Cubemap(images[0], images[1], images[2], images[3], images[4], images[5]);
	}

	@Override
	public void dispose() {
		super.dispose();

		for (Image image : images) {
			image.dispose();
		}

		for (Buffer buffer : buffers) {
			MemoryUtil.memFree(buffer);
		}
	}

	/**
	 * Gets the right {@link Image}.
	 *
	 * @return The right {@link Image}.
	 */
	public Image getRight() {
		return images[0];
	}

	/**
	 * Gets the left {@link Image}.
	 *
	 * @return The left {@link Image}.
	 */
	public Image getLeft() {
		return images[1];
	}

	/**
	 * Gets the top {@link Image}.
	 *
	 * @return The top {@link Image}.
	 */
	public Image getTop() {
		return images[2];
	}

	/**
	 * Gets the bottom {@link Image}.
	 *
	 * @return The bottom {@link Image}.
	 */
	public Image getBottom() {
		return images[3];
	}

	/**
	 * Gets the front {@link Image}.
	 *
	 * @return The front {@link Image}.
	 */
	public Image getFront() {
		return images[4];
	}

	/**
	 * Gets the back {@link Image}.
	 *
	 * @return The back {@link Image}.
	 */
	public Image getBack() {
		return images[5];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(buffers);
		result = prime * result + Arrays.hashCode(images);
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
		Cubemap other = (Cubemap) obj;
		if (!Arrays.equals(buffers, other.buffers))
			return false;
		return Arrays.equals(images, other.images);
	}
}
