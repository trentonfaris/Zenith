package com.trentonfaris.zenith.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Random;

public final class Utility {
	/** The {@link URI} host indicating that a file is packaged in the JAR. */
	public static final String PACKAGED_FILE_HOST = "zenith";

	private Utility() {
	}

	/**
	 * Gets a {@link File} from a location.
	 *
	 * @param path
	 * @return The {@link File}.
	 * @throws FileNotFoundException
	 */
	public static File getFile(String path) throws FileNotFoundException {
		File file = new File(path);

		if (!file.exists()) {
			throw new FileNotFoundException(path);
		}

		return file;
	}

	/**
	 * Gets a temporary {@link File} from a packaged location.
	 *
	 * @param path
	 * @return The temporary {@link File}.
	 * @throws IOException
	 */
	public static File getPackagedFile(String path) throws IOException {
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);

		if (in == null) {
			throw new FileNotFoundException(path);
		}

		File file = File.createTempFile(String.valueOf(Math.abs(new Random().nextInt())), Utility.PACKAGED_FILE_HOST);
		file.deleteOnExit();

		OutputStream out = new FileOutputStream(file);

		byte[] buffer = new byte[1024];

		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();

		return file;
	}
}
