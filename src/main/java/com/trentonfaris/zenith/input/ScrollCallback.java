package com.trentonfaris.zenith.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public final class ScrollCallback extends GLFWScrollCallback {
	private volatile double xoffset;
	private volatile double yoffset;

	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		this.xoffset = xoffset;
		this.yoffset = yoffset;
	}

	double getXOffset() {
		return xoffset;
	}

	double getYOffset() {
		return yoffset;
	}

}
