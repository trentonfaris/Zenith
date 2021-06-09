package com.trentonfaris.zenith.ecs.system.transformation;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Rotation;
import com.trentonfaris.zenith.ecs.component.transform.Scale;
import com.trentonfaris.zenith.ecs.component.transformation.ModelMatrix;

import org.joml.Matrix4f;

public class ModelMatrixSystem extends IteratingSystem {
	ComponentMapper<ModelMatrix> mModelMatrix;
	ComponentMapper<Position> mPosition;
	ComponentMapper<Rotation> mRotation;
	ComponentMapper<Scale> mScale;

	public ModelMatrixSystem() {
		super(Aspect.one(Position.class, Rotation.class, Scale.class));
	}

	@Override
	protected void process(int i) {
		Position position = mPosition.create(i);
		Rotation rotation = mRotation.create(i);
		Scale scale = mScale.create(i);
		ModelMatrix modelMatrix = mModelMatrix.create(i);

		modelMatrix.mat = new Matrix4f().translate(position.xyz).rotate(rotation.xyzw).scale(scale.xyz);
	}
}
