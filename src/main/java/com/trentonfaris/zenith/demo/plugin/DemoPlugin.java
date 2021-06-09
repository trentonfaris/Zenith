package com.trentonfaris.zenith.demo.plugin;

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
