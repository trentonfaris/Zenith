package com.trentonfaris.zenith.ecs.component.camera;

import com.artemis.Component;
import com.artemis.utils.BitVector;
import com.trentonfaris.zenith.ecs.Layers;

import org.joml.Vector3f;

public class Camera extends Component {
	public ClearType clearType = ClearType.SKYBOX;
	public Vector3f clearColor = new Vector3f(49f / 255f, 77f / 255f, 121f / 255f);
	public BitVector layerMask = getDefaultBitVector();
	public float depth = 0;

	private static BitVector getDefaultBitVector() {
		BitVector bitVector = new BitVector();
		bitVector.set(Layers.DEFAULT.getId());

		return bitVector;
	}

	public enum ClearType {
		SKYBOX, SOLID_COLOR
	}
}
