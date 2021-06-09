package com.trentonfaris.zenith.demo.ecs.system;

import java.awt.event.KeyEvent;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.demo.ecs.component.DemoFPSController;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Rotation;
import com.trentonfaris.zenith.input.Input;
import com.trentonfaris.zenith.input.KeyButtonAxis;
import com.trentonfaris.zenith.input.KeyButtonInput;
import com.trentonfaris.zenith.input.KeyButtonInput.KeyButtonType;
import com.trentonfaris.zenith.input.MouseMovementAxis;
import com.trentonfaris.zenith.input.MouseMovementAxis.MouseAxis;

public class DemoFPSControllerSystem extends IteratingSystem {
	// Constant values.
	private static final String HORIZONTAL_AXIS = "Horizontal";
	private static final String VERTICAL_AXIS = "Vertical";
	private static final String UP_DOWN_AXIS = "UpDown";
	private static final String MOUSE_X_AXIS = "Mouse X";
	private static final String MOUSE_Y_AXIS = "Mouse Y";

	ComponentMapper<DemoFPSController> mDemoFPSController;
	ComponentMapper<Position> mPosition;
	ComponentMapper<Rotation> mRotation;

	public DemoFPSControllerSystem() {
		super(Aspect.all(DemoFPSController.class));
	}

	@Override
	protected void initialize() {
		Input input = Zenith.getEngine().getInput();

		input.registerAxis(new KeyButtonAxis(HORIZONTAL_AXIS, new KeyButtonInput(KeyButtonType.KEY, KeyEvent.VK_D),
				new KeyButtonInput(KeyButtonType.KEY, KeyEvent.VK_A)));
		input.registerAxis(new KeyButtonAxis(VERTICAL_AXIS, new KeyButtonInput(KeyButtonType.KEY, KeyEvent.VK_W),
				new KeyButtonInput(KeyButtonType.KEY, KeyEvent.VK_S)));
		input.registerAxis(new KeyButtonAxis(UP_DOWN_AXIS, new KeyButtonInput(KeyButtonType.KEY, KeyEvent.VK_E),
				new KeyButtonInput(KeyButtonType.KEY, KeyEvent.VK_Q)));
		input.registerAxis(
				new MouseMovementAxis(MOUSE_X_AXIS, MouseAxis.X_AXIS, MouseMovementAxis.DEFAULT_SENSITIVITY));
		input.registerAxis(
				new MouseMovementAxis(MOUSE_Y_AXIS, MouseAxis.Y_AXIS, MouseMovementAxis.DEFAULT_SENSITIVITY));
	}

	@Override
	protected void dispose() {
		Input input = Zenith.getEngine().getInput();

		input.removeAxis(HORIZONTAL_AXIS);
		input.removeAxis(VERTICAL_AXIS);
		input.removeAxis(UP_DOWN_AXIS);
		input.removeAxis(MOUSE_X_AXIS);
		input.removeAxis(MOUSE_Y_AXIS);
	}

	@Override
	protected void process(int i) {
		Input input = Zenith.getEngine().getInput();

		Vector3f moveInput = new Vector3f(input.getAxis(HORIZONTAL_AXIS), input.getAxis(UP_DOWN_AXIS),
				-input.getAxis(VERTICAL_AXIS));
		Vector2f turnInput = new Vector2f(input.getAxis(MOUSE_X_AXIS), input.getAxis(MOUSE_Y_AXIS));

		Vector3f moveDirection = moveInput.normalize(new Vector3f());
		if (Float.isNaN(moveDirection.x) || Float.isNaN(moveDirection.y) || Float.isNaN(moveDirection.z)) {
			moveDirection = new Vector3f();
		}

		DemoFPSController demoFPSController = mDemoFPSController.get(i);
		Position position = mPosition.create(i);
		Rotation rotation = mRotation.create(i);

		position.xyz.add(moveDirection.mul(demoFPSController.moveSpeed * world.delta).rotate(rotation.xyzw));

		Quaternionf pitch = new Quaternionf().fromAxisAngleDeg(new Vector3f(1, 0, 0).rotate(rotation.xyzw).normalize(),
				-turnInput.y * demoFPSController.turnSpeed * demoFPSController.turnSensitivity * world.delta);
		Quaternionf yaw = new Quaternionf().fromAxisAngleDeg(new Vector3f(0, 1, 0),
				-turnInput.x * demoFPSController.turnSpeed * demoFPSController.turnSensitivity * world.delta);

		rotation.xyzw = yaw.mul(pitch.mul(rotation.xyzw));
	}
}
