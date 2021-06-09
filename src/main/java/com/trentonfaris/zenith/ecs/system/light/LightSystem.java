package com.trentonfaris.zenith.ecs.system.light;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.trentonfaris.zenith.ecs.component.light.Light;
import com.trentonfaris.zenith.ecs.component.light.Light.Priority;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.shader.SkyboxShader;

public class LightSystem extends IteratingSystem {
	ComponentMapper<Light> mLight;

	private Material skyboxMaterial;
	private Vector3f ambientColor = new Vector3f(0.1f);

	private final Set<Entity> autoLights = new HashSet<>();
	private final Set<Entity> importantLights = new HashSet<>();
	private final Set<Entity> notImportantLights = new HashSet<>();

	public LightSystem() {
		super(Aspect.all(Light.class));
	}

	@Override
	protected void initialize() {
		this.skyboxMaterial = new Material(SkyboxShader.class);
	}

	@Override
	protected void dispose() {
		skyboxMaterial.dispose();
	}

	@Override
	protected void begin() {
		autoLights.clear();
		importantLights.clear();
		notImportantLights.clear();
	}

	@Override
	protected void process(int i) {
		Light light = mLight.get(i);

		Entity entity = world.getEntity(i);

		if (light.priority == Priority.AUTO) {
			autoLights.add(entity);
		} else if (light.priority == Priority.IMPORTANT) {
			importantLights.add(entity);
		} else if (light.priority == Priority.NOT_IMPORTANT) {
			notImportantLights.add(entity);
		}
	}

	public Set<Entity> getAutoLights() {
		return Collections.unmodifiableSet(autoLights);
	}

	public Set<Entity> getImportantLights() {
		return Collections.unmodifiableSet(importantLights);
	}

	public Set<Entity> getNotImportantLights() {
		return Collections.unmodifiableSet(notImportantLights);
	}

	public Material getSkyboxMaterial() {
		return skyboxMaterial;
	}

	public Vector3f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector3f ambientColor) {
		this.ambientColor = ambientColor;
	}
}
