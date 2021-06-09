package com.trentonfaris.zenith.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.image.Image;
import com.trentonfaris.zenith.utility.Utility;

/**
 * The {@link ImageLoader} class is responsible for loading image data and
 * metadata.
 *
 * @author Trenton Faris
 */
public final class ImageLoader extends ResourceLoader<Image> {
	/** The scheme for a {@link ImageLoader}. */
	public static final String SCHEME = "image";

	/** Creates a new {@link ImageLoader}. */
	public ImageLoader() {
		super(SCHEME);
	}

	@Override
	public Image load(URI uri) throws ResourceIOException, ResourceNotFoundException {
		if (uri == null) {
			String errorMsg = "Cannot load an Image from a null URI.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		STBImage.stbi_set_flip_vertically_on_load(true);

		String path = ResourceManager.RESOURCES_DIRECTORY.getPath() + uri.getPath();

		File file;
		if (uri.getHost().equalsIgnoreCase(Utility.PACKAGED_FILE_HOST)) {
			try {
				file = Utility.getPackagedFile(path);
			} catch (IOException e) {
				Zenith.getLogger().error("Cannot read the resource: " + uri.getPath());
				throw new ResourceIOException(uri.getPath());
			}
		} else {
			try {
				file = Utility.getFile(path);
			} catch (FileNotFoundException e) {
				Zenith.getLogger().error("Cannot find the resource: " + uri.getPath());
				throw new ResourceNotFoundException(uri.getPath());
			}
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			IntBuffer pChannels = stack.mallocInt(1);

			Buffer data;
			if (STBImage.stbi_is_16_bit(file.getPath())) {
				ShortBuffer shortData = STBImage.stbi_load_16(file.getPath(), pWidth, pHeight, pChannels, 0);

				// TODO : Catch NPE on shortData

				data = MemoryUtil.memAllocShort(shortData.capacity());
				((ShortBuffer) data).put(shortData).flip();
				
				shortData.rewind();

				STBImage.stbi_image_free(shortData);
			} else {
				ByteBuffer byteData = STBImage.stbi_load(file.getPath(), pWidth, pHeight, pChannels, 0);

				// TODO : Catch NPE on byteData

				data = MemoryUtil.memAlloc(byteData.capacity());
				((ByteBuffer) data).put(byteData).flip();
				
				byteData.rewind();

				STBImage.stbi_image_free(byteData);
			}

			return new Image(data, pWidth.get(), pHeight.get(), pChannels.get());
		}
	}
}
