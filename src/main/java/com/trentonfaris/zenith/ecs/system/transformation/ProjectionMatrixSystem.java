package com.trentonfaris.zenith.ecs.system.transformation;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.ecs.component.camera.Projection;
import com.trentonfaris.zenith.ecs.component.camera.Projection.ProjectionType;
import com.trentonfaris.zenith.ecs.component.transformation.ProjectionMatrix;
import com.trentonfaris.zenith.ecs.component.ui.Rect;
import com.trentonfaris.zenith.window.Window;

public class ProjectionMatrixSystem extends IteratingSystem {
	ComponentMapper<Projection> mProjection;
	ComponentMapper<ProjectionMatrix> mProjectionMatrix;
	ComponentMapper<Rect> mRect;

	public ProjectionMatrixSystem() {
		super(Aspect.all(Projection.class));
	}

	@Override
	protected void process(int i) {
		Projection projection = mProjection.get(i);
		ProjectionMatrix projectionMatrix = mProjectionMatrix.create(i);

		if (projection.projectionType == ProjectionType.PERSPECTIVE) {
			Window window = Zenith.getEngine().getWindow();
			Rect rect = mRect.create(i);

			Vector2i size = window.getSize();
			float width = size.x * rect.width;
			float height = size.y * rect.height;

			projectionMatrix.mat = new Matrix4f().perspective((float) Math.toRadians(projection.fov), width / height,
					projection.near, projection.far);
		} else if (projection.projectionType == ProjectionType.ORTHOGRAPHIC) {
			projectionMatrix.mat = new Matrix4f().ortho(-projection.hSize, projection.hSize, -projection.vSize,
					projection.vSize, projection.near, projection.far);
		}
	}
}
