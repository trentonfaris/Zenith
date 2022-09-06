package com.trentonfaris.zenith.demo.ecs.system;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.demo.ecs.component.DemoFPSController;
import com.trentonfaris.zenith.ecs.component.camera.Camera;
import com.trentonfaris.zenith.ecs.component.camera.Projection;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Rotation;
import com.trentonfaris.zenith.input.Input;
import com.trentonfaris.zenith.window.Window;

public class DemoCameraSystem extends BaseSystem {
	ComponentMapper<Position> mPosition;
	ComponentMapper<Rotation> mRotation;
	ComponentMapper<Camera> mCamera;
	ComponentMapper<Projection> mProjection;
	ComponentMapper<DemoFPSController> mDemoFPSController;

	DemoFPSControllerSystem demoFPSControllerSystem;

	@Override
	protected void initialize() {
		int entity = world.create();

		Position position = mPosition.create(entity);
		position.xyz = new Vector3f(0, 0.5f, 3);

		mRotation.create(entity);

		mCamera.create(entity);
		mProjection.create(entity);

//		mRenderEffectLayer.create(entity);

		DemoFPSController demoFPSController = mDemoFPSController.create(entity);
		demoFPSController.turnSensitivity = 10;
	}

	@Override
	protected void processSystem() {
		Input input = Zenith.getEngine().getInput();
		Window window = Zenith.getEngine().getWindow();

		if (input.isButton(GLFW.GLFW_MOUSE_BUTTON_2)) {
			demoFPSControllerSystem.setEnabled(true);
			GLFW.glfwSetInputMode(window.getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		} else {
			demoFPSControllerSystem.setEnabled(false);
			GLFW.glfwSetInputMode(window.getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}

}
