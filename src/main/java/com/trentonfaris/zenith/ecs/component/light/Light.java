package com.trentonfaris.zenith.ecs.component.light;

import com.artemis.Component;
import org.joml.Vector3f;

public class Light extends Component {
	public Priority priority = Priority.AUTO;
	public Vector3f color = new Vector3f(1, 244f / 255f, 214f / 255f);
	public float intensity = 1;

	public enum Priority {
		AUTO, IMPORTANT, NOT_IMPORTANT
	}
}
