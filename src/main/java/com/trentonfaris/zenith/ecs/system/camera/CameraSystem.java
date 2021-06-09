package com.trentonfaris.zenith.ecs.system.camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.trentonfaris.zenith.ecs.component.camera.Camera;
import com.trentonfaris.zenith.ecs.component.misc.State;

public class CameraSystem extends IteratingSystem {
	ComponentMapper<State> mState;
	ComponentMapper<Camera> mCamera;

	private final List<Integer> sorted = new ArrayList<>();

	public CameraSystem() {
		super(Aspect.all(Camera.class));
	}

	@Override
	protected void begin() {
		sorted.clear();
	}

	@Override
	protected void process(int i) {
		State state = mState.create(i);
		if (!state.active) {
			return;
		}

		Camera camera = mCamera.get(i);

		// TODO : Do this with a binary search for faster compute time
		for (int j = 0; j < sorted.size(); j++) {
			Camera other = mCamera.get(sorted.get(j));

			if (camera.depth > other.depth) {
				sorted.add(j, i);
				return;
			}
		}

		sorted.add(sorted.size(), i);
	}

	public List<Integer> getSorted() {
		return Collections.unmodifiableList(sorted);
	}
}
