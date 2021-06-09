package com.trentonfaris.zenith.demo.ecs.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.trentonfaris.zenith.ecs.component.light.Light;
import com.trentonfaris.zenith.ecs.component.transform.Rotation;

public class DemoLightSystem extends BaseSystem {
	ComponentMapper<Rotation> mRotation;
	ComponentMapper<Light> mLight;

	@Override
	protected void initialize() {
		int entity = world.create();

		Rotation rotation = mRotation.create(entity);
		rotation.xyzw.rotateY((float) Math.toRadians(45)).rotateX((float) Math.toRadians(-50));

		mLight.create(entity);
	}

	@Override
	protected void processSystem() {
		// TODO Auto-generated method stub

	}

}
