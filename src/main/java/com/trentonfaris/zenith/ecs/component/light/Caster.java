package com.trentonfaris.zenith.ecs.component.light;

import com.artemis.Component;

public class Caster extends Component {
	public CasterType casterType = CasterType.DIRECTIONAL;
	public float radius = 10;
	public float innerCutoffAngle = 25;
	public float outerCutoffAngle = 35;

	public enum CasterType {
		DIRECTIONAL, POINT, SPOT
	}
}
