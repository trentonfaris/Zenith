package com.trentonfaris.zenith.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryStack;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.graphics.shader.ShaderType;
import com.trentonfaris.zenith.utility.Utility;

/**
 * The {@link ShaderLoader} class is responsible for loading, compiling, and
 * linking vertex, fragment, and geometry shaders.
 *
 * @author Trenton Faris
 */
public final class ShaderLoader extends ResourceLoader<Integer> {
	/** The scheme for a {@link ShaderLoader}. */
	public static final String SCHEME = "shader";

	/** Creates a new {@link ShaderLoader}. */
	public ShaderLoader() {
		super(SCHEME);
	}

	@Override
	public Integer load(URI uri) throws ResourceIOException, ResourceNotFoundException {
		if (uri == null) {
			String errorMsg = "Cannot load a Shader from a null URI.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		List<Integer> compiledShaders = new ArrayList<>();
		for (ShaderType shaderType : ShaderType.values()) {
			String pathWithExt = uri.getPath() + shaderType.getExtension();

			String path = ResourceManager.RESOURCES_DIRECTORY.getPath() + pathWithExt;

			File file;
			if (uri.getHost().equalsIgnoreCase(Utility.PACKAGED_FILE_HOST)) {
				try {
					file = Utility.getPackagedFile(path);
				} catch (IOException e) {
					if (shaderType.getExtension().equalsIgnoreCase(ShaderType.VERTEX.getExtension())
							|| shaderType.getExtension().equalsIgnoreCase(ShaderType.FRAGMENT.getExtension())) {
						Zenith.getLogger().error("Cannot read the resource: " + pathWithExt);
						throw new ResourceIOException(pathWithExt);
					} else {
						continue;
					}
				}
			} else {
				try {
					file = Utility.getFile(path);
				} catch (FileNotFoundException e) {
					if (shaderType.getExtension().equalsIgnoreCase(ShaderType.VERTEX.getExtension())
							|| shaderType.getExtension().equalsIgnoreCase(ShaderType.FRAGMENT.getExtension())) {
						Zenith.getLogger().error("Cannot find the resource: " + pathWithExt);
						throw new ResourceNotFoundException(pathWithExt);
					} else {
						continue;
					}
				}
			}

			StringBuilder sb = new StringBuilder();

			try {
				List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
				for (String line : lines) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				Zenith.getLogger().error("Cannot read the resource: " + pathWithExt);
				throw new ResourceIOException(pathWithExt);
			}

			String source = sb.toString();

			int shader;
			try {
				shader = compileShader(shaderType.getValue(), source);
			} catch (IOException e) {
				Zenith.getLogger().error("Cannot read the resource: " + pathWithExt);
				throw new ResourceIOException(pathWithExt);
			}

			if (shader != 0) {
				compiledShaders.add(shader);
			}
		}

		int program = GL20.glCreateProgram();

		for (Integer compiledShader : compiledShaders) {
			GL20.glAttachShader(program, compiledShader);
		}

		GL20.glLinkProgram(program);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pStatus = stack.mallocInt(1);
			GL20.glGetProgramiv(program, GL20.GL_LINK_STATUS, pStatus);

			int status = pStatus.get();

			if (status == GL11.GL_FALSE) {
				IntBuffer pInfoLogLength = stack.mallocInt(1);
				GL20.glGetProgramiv(program, GL20.GL_INFO_LOG_LENGTH, pInfoLogLength);

				int infoLogLength = pInfoLogLength.get();

				ByteBuffer pInfoLog = stack.malloc(infoLogLength);
				GL20.glGetProgramInfoLog(program, pInfoLogLength, pInfoLog);

				byte[] bytes = new byte[infoLogLength];
				pInfoLog.get(bytes);

				String strInfoLog = new String(bytes);

				GL20.glDeleteProgram(program);

				for (Integer compiledShader : compiledShaders) {
					GL20.glDeleteShader(compiledShader);
				}

				Zenith.getLogger().error("Linker failure: " + strInfoLog);
				throw new ResourceIOException(uri.getPath());
			}

			for (Integer compiledShader : compiledShaders) {
				GL20.glDetachShader(program, compiledShader);
				GL20.glDeleteShader(compiledShader);
			}

			return program;
		}
	}

	/**
	 * Compiles a shader of specified type.
	 *
	 * @param type
	 * @param source
	 * @return A shader, which may or may not be compiled successfully.
	 * @throws ResourceIOException
	 */
	private int compileShader(int type, String source) throws IOException {
		int shader = GL20.glCreateShader(type);

		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pStatus = stack.mallocInt(1);
			GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, pStatus);

			int status = pStatus.get();

			if (status == GL11.GL_FALSE) {
				IntBuffer pInfoLogLength = stack.mallocInt(1);
				GL20.glGetShaderiv(shader, GL20.GL_INFO_LOG_LENGTH, pInfoLogLength);

				int infoLogLength = pInfoLogLength.get();

				ByteBuffer pInfoLog = stack.malloc(infoLogLength);
				GL20.glGetShaderInfoLog(shader, pInfoLogLength, pInfoLog);

				byte[] bytes = new byte[infoLogLength];
				pInfoLog.get(bytes);

				String strInfoLog = new String(bytes);

				String strShaderType = "";
				if (type == GL20.GL_VERTEX_SHADER) {
					strShaderType = "vertex";
				} else if (type == GL20.GL_FRAGMENT_SHADER) {
					strShaderType = "fragment";
				} else if (type == GL32.GL_GEOMETRY_SHADER) {
					strShaderType = "geometry";
				}

				GL20.glDeleteShader(shader);

				Zenith.getLogger().error("Compiler failure in " + strShaderType + " shader: " + strInfoLog);
				if (type == GL20.GL_VERTEX_SHADER || type == GL20.GL_FRAGMENT_SHADER) {
					throw new IOException();
				}

				return 0;
			}

			return shader;
		}
	}
}
