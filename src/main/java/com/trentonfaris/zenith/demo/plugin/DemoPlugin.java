package com.trentonfaris.zenith.demo.plugin;

import com.trentonfaris.zenith.scene.Scene;
import com.trentonfaris.zenith.scene.SceneManager;
import org.lwjgl.glfw.GLFW;

import com.artemis.ArtemisPlugin;
import com.artemis.BaseSystem;
import com.artemis.WorldConfigurationBuilder;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.demo.ecs.system.DemoAssetSystem;
import com.trentonfaris.zenith.demo.ecs.system.DemoCameraSystem;
import com.trentonfaris.zenith.demo.ecs.system.DemoFPSControllerSystem;
import com.trentonfaris.zenith.demo.ecs.system.DemoLightSystem;
import com.trentonfaris.zenith.ecs.system.misc.FPSNotifierSystem;

/**
 * The {@link DemoPlugin} is an example class that shows common use of the Zenith game engine.
 * It is an {@link ArtemisPlugin} loaded as a {@link Scene} via the {@link SceneManager}. It contains several demo
 * systems that can also be used as an example in how to use the Zenith game engine.
 *
 * @author Trenton Faris
 */
public class DemoPlugin implements ArtemisPlugin {

	@Override
	public void setup(WorldConfigurationBuilder worldConfigurationBuilder) {
		worldConfigurationBuilder.with(new FPSNotifierSystem()).with(new DemoCameraSystem()).with(new DemoLightSystem())
				.with(new DemoAssetSystem()).with(new DemoFPSControllerSystem()).with(new BaseSystem() {
					@Override
					protected void processSystem() {
						if (Zenith.getEngine().getInput().isKey(GLFW.GLFW_KEY_ESCAPE)) {
							Zenith.stop();
						}
					}
				});
	}
}
