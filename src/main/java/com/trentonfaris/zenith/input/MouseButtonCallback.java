package com.trentonfaris.zenith.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public final class MouseButtonCallback extends GLFWMouseButtonCallback {
	private static final int NUM_BUTTONS = 8;

	private volatile boolean[] buttons = new boolean[NUM_BUTTONS];

	@Override
	public void invoke(long handle, int button, int action, int mods) {
		if (action == GLFW.GLFW_RELEASE) {
			buttons[button] = false;
		} else {
			buttons[button] = true;
		}
	}

	boolean[] getButtons() {
		return buttons;
	}
}
