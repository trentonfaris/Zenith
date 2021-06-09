package com.trentonfaris.zenith.ecs.component.camera;

import com.artemis.Component;

public class Projection extends Component {
	public ProjectionType projectionType = ProjectionType.PERSPECTIVE;
	public float near = 0.1f;
	public float far = 1000;
	public float fov = 60;
	public float hSize = 5;
	public float vSize = 5;

	public enum ProjectionType {
		PERSPECTIVE, ORTHOGRAPHIC
	}
}
