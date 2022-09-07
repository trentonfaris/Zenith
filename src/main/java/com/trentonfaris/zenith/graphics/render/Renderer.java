package com.trentonfaris.zenith.graphics.render;

import com.artemis.World;
import com.trentonfaris.zenith.ecs.component.camera.Camera;

public abstract class Renderer {

	/**
	 * Draws the {@link World} from the perspective of the {@link Camera}.
	 * 
	 * @param world The target {@link World}
	 * @param cameraId The id of the target {@link Camera}
	 */
	public abstract void render(World world, int cameraId);
}
