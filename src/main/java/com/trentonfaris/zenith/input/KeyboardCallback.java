package com.trentonfaris.zenith.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public final class KeyboardCallback extends GLFWKeyCallback {
	private static final int NUM_KEYS = 65536;

	private final boolean[] keys = new boolean[NUM_KEYS];

	@Override
	public void invoke(long handle, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW.GLFW_RELEASE;
	}

	boolean[] getKeys() {
		return keys;
	}
}
