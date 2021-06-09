package com.trentonfaris.zenith.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public final class KeyboardCallback extends GLFWKeyCallback {
	private static final int NUM_KEYS = 65536;

	private volatile boolean[] keys = new boolean[NUM_KEYS];

	@Override
	public void invoke(long handle, int key, int scancode, int action, int mods) {
		if (action == GLFW.GLFW_RELEASE) {
			keys[key] = false;
		} else {
			keys[key] = true;
		}
	}

	boolean[] getKeys() {
		return keys;
	}
}
