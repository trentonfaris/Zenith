package com.trentonfaris.zenith.scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.artemis.ArtemisPlugin;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.ecs.system.camera.CameraSystem;
import com.trentonfaris.zenith.ecs.system.core.LayerSystem;
import com.trentonfaris.zenith.ecs.system.core.TagSystem;
import com.trentonfaris.zenith.ecs.system.light.LightSystem;
import com.trentonfaris.zenith.ecs.system.render.RenderSystem;
import com.trentonfaris.zenith.ecs.system.transformation.ModelMatrixSystem;
import com.trentonfaris.zenith.ecs.system.transformation.ProjectionMatrixSystem;
import com.trentonfaris.zenith.ecs.system.transformation.ViewMatrixSystem;
import com.trentonfaris.zenith.utility.Updatable;

public final class Scene implements Updatable {
	private final ArtemisPlugin artemisPlugin;
	private World world;

	<T extends ArtemisPlugin> Scene(Class<T> artemisPluginType) {
		if (artemisPluginType == null) {
			String errorMsg = "Cannot load a scene from a null artemisPluginClass.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		try {
			Constructor<T> constructor = artemisPluginType.getConstructor();
			constructor.setAccessible(true);
			this.artemisPlugin = constructor.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			String errorMsg = "Cannot create an ArtemisPlugin from type: " + artemisPluginType
					+ ". Must contain a constructor with no arguments.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
	}

	@Override
	public void update() {
		if (isLoaded()) {
			world.setDelta((float) Zenith.getEngine().getTime().getDeltaTime());
			world.process();
		}
	}

	void load() {
		WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder().with(new TagSystem())
				.with(new LayerSystem()).with(artemisPlugin).with(new ModelMatrixSystem()).with(new ViewMatrixSystem())
				.with(new ProjectionMatrixSystem()).with(new LightSystem()).with(new CameraSystem())
				.with(new RenderSystem());

		this.world = new World(worldConfigurationBuilder.build());
	}

	void unload() {
		if (isLoaded()) {
			world.dispose();

			this.world = null;
		}
	}

	public boolean isLoaded() {
		return world != null;
	}
}
