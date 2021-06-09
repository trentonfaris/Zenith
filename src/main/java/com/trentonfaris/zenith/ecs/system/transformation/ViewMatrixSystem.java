package com.trentonfaris.zenith.ecs.system.transformation;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.trentonfaris.zenith.ecs.component.camera.Camera;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Rotation;
import com.trentonfaris.zenith.ecs.component.transformation.ViewMatrix;

public class ViewMatrixSystem extends IteratingSystem {
	ComponentMapper<Position> mPosition;
	ComponentMapper<Rotation> mRotation;
	ComponentMapper<ViewMatrix> mViewMatrix;

	public ViewMatrixSystem() {
		super(Aspect.all(Camera.class).one(Position.class, Rotation.class));
	}

	@Override
	protected void process(int i) {
		Position position = mPosition.create(i);
		Rotation rotation = mRotation.create(i);
		ViewMatrix viewMatrix = mViewMatrix.create(i);

		Vector3f eye = position.xyz;
		Vector3f forward = new Vector3f(0, 0, -1).rotate(rotation.xyzw).normalize();
		Vector3f up = new Vector3f(0, 1, 0).rotate(rotation.xyzw).normalize();

		viewMatrix.mat = new Matrix4f().lookAt(eye, eye.add(forward, new Vector3f()), up);
	}
}
