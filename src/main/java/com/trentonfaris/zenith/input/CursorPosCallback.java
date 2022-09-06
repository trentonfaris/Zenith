package com.trentonfaris.zenith.input;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public final class CursorPosCallback extends GLFWCursorPosCallback {
	private final Vector2d position = new Vector2d();

	@Override
	public void invoke(long handle, double x, double y) {
		position.x = x;
		position.y = y;
	}

	Vector2d getPosition() {
		return position;
	}
}
