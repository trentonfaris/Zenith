package com.trentonfaris.zenith.demo.ecs.system;

import org.joml.Vector3f;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.trentonfaris.zenith.demo.resource.resources.DemoImages;
import com.trentonfaris.zenith.ecs.component.render.Renderable;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Scale;
import com.trentonfaris.zenith.ecs.system.light.LightSystem;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.material.MaterialProperty;
import com.trentonfaris.zenith.graphics.model.Model;
import com.trentonfaris.zenith.graphics.shader.SkyboxShader;
import com.trentonfaris.zenith.graphics.shader.StandardShader;
import com.trentonfaris.zenith.graphics.texture.Cubemap;
import com.trentonfaris.zenith.graphics.texture.FilteringMode;
import com.trentonfaris.zenith.graphics.texture.InternalFormat;
import com.trentonfaris.zenith.graphics.texture.PixelFormat;
import com.trentonfaris.zenith.graphics.texture.PixelType;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.graphics.texture.WrappingMode;
import com.trentonfaris.zenith.image.Image;
import com.trentonfaris.zenith.resource.resources.Images;
import com.trentonfaris.zenith.resource.resources.Models;

public class DemoAssetSystem extends BaseSystem {
	ComponentMapper<Position> mPosition;
	ComponentMapper<Scale> mScale;
	ComponentMapper<Renderable> mRenderable;

	LightSystem lightSystem;

	@Override
	protected void initialize() {
		loadGround();
		loadSky();
		loadCube();
	}

	@SuppressWarnings("unchecked")
	private void loadGround() {
		int entity = world.create();

		Scale scale = mScale.create(entity);
		scale.xyz = new Vector3f(50, 1, 50);

		Renderable renderable = mRenderable.create(entity);
		renderable.model = Model.loadModel(Models.PLANE.getURI());

		Material material = renderable.model.getMeshes().get(0).getMaterial();

		MaterialProperty<?> albedoColor = material.getMaterialProperties().get(StandardShader.ALBEDO_COLOR);
		if (albedoColor.value instanceof Vector3f) {
			((MaterialProperty<Vector3f>) albedoColor).value = new Vector3f(0.2f);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadSky() {
		Image right = Image.loadImage(Images.DEFAULT_SKYBOX_RIGHT.getURI());
		Image left = Image.loadImage(Images.DEFAULT_SKYBOX_LEFT.getURI());
		Image top = Image.loadImage(Images.DEFAULT_SKYBOX_TOP.getURI());
		Image bottom = Image.loadImage(Images.DEFAULT_SKYBOX_BOTTOM.getURI());
		Image front = Image.loadImage(Images.DEFAULT_SKYBOX_FRONT.getURI());
		Image back = Image.loadImage(Images.DEFAULT_SKYBOX_BACK.getURI());

		Material skyboxMaterial = lightSystem.getSkyboxMaterial();
		MaterialProperty<?> skybox = skyboxMaterial.getMaterialProperties().get(SkyboxShader.SKYBOX);
		if (skybox.getType().isAssignableFrom(Cubemap.class)) {
			((MaterialProperty<Cubemap>) skybox).value = new Cubemap(right, left, top, bottom, front, back);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadCube() {
		int entity = world.create();

		Position position = mPosition.create(entity);
		position.xyz = new Vector3f(0, 0.5f, 0);

		Renderable renderable = mRenderable.create(entity);
		renderable.model = Model.loadModel(Models.CUBE.getURI());

		Material material = renderable.model.getMeshes().get(0).getMaterial();

		// Albedo
		Image albedo = Image.loadImage(DemoImages.ROCK_ALBEDO.getURI());

		MaterialProperty<?> useAlbedoMap = material.getMaterialProperties().get(StandardShader.USE_ALBEDO_MAP);
		if (useAlbedoMap.getType().isAssignableFrom(Boolean.class)) {
			((MaterialProperty<Boolean>) useAlbedoMap).value = true;
		}

		MaterialProperty<?> albedoMap = material.getMaterialProperties().get(StandardShader.ALBEDO_MAP);
		if (albedoMap.getType().isAssignableFrom(Texture2D.class)) {
			((MaterialProperty<Texture2D>) albedoMap).value = new Texture2D(InternalFormat.SRGB, albedo.getWidth(),
					albedo.getHeight(), PixelFormat.RGB, PixelType.UNSIGNED_SHORT, albedo.getData(),
					WrappingMode.REPEAT, FilteringMode.LINEAR);
		}

		// Roughness
		Image roughness = Image.loadImage(DemoImages.ROCK_ROUGHNESS.getURI());

		MaterialProperty<?> useRoughnessMap = material.getMaterialProperties().get(StandardShader.USE_ROUGHNESS_MAP);
		if (useRoughnessMap.getType().isAssignableFrom(Boolean.class)) {
			((MaterialProperty<Boolean>) useRoughnessMap).value = true;
		}

		MaterialProperty<?> roughnessMap = material.getMaterialProperties().get(StandardShader.ROUGHNESS_MAP);
		if (roughnessMap.getType().isAssignableFrom(Texture2D.class)) {
			((MaterialProperty<Texture2D>) roughnessMap).value = new Texture2D(InternalFormat.RED, roughness.getWidth(),
					roughness.getHeight(), PixelFormat.RED, PixelType.UNSIGNED_BYTE, roughness.getData(),
					WrappingMode.REPEAT, FilteringMode.LINEAR);
		}

		// Ambient occlusion
		Image ao = Image.loadImage(DemoImages.ROCK_AO.getURI());

		MaterialProperty<?> useAoMap = material.getMaterialProperties().get(StandardShader.USE_AO_MAP);
		if (useAoMap.getType().isAssignableFrom(Boolean.class)) {
			((MaterialProperty<Boolean>) useAoMap).value = true;
		}

		MaterialProperty<?> aoMap = material.getMaterialProperties().get(StandardShader.AO_MAP);
		if (aoMap.getType().isAssignableFrom(Texture2D.class)) {
			((MaterialProperty<Texture2D>) aoMap).value = new Texture2D(InternalFormat.RED, ao.getWidth(),
					ao.getHeight(), PixelFormat.RED, PixelType.UNSIGNED_BYTE, ao.getData(), WrappingMode.REPEAT,
					FilteringMode.LINEAR);
		}

		// Normal
		Image normal = Image.loadImage(DemoImages.ROCK_NORMAL.getURI());

		MaterialProperty<?> useNormalMap = material.getMaterialProperties().get(StandardShader.USE_NORMAL_MAP);
		if (useNormalMap.getType().isAssignableFrom(Boolean.class)) {
			((MaterialProperty<Boolean>) useNormalMap).value = true;
		}

		MaterialProperty<?> normalMap = material.getMaterialProperties().get(StandardShader.NORMAL_MAP);
		if (normalMap.getType().isAssignableFrom(Texture2D.class)) {
			((MaterialProperty<Texture2D>) normalMap).value = new Texture2D(InternalFormat.RGB, normal.getWidth(),
					normal.getHeight(), PixelFormat.RGB, PixelType.UNSIGNED_SHORT, normal.getData(),
					WrappingMode.REPEAT, FilteringMode.LINEAR);
		}

		// Height
		Image height = Image.loadImage(DemoImages.ROCK_HEIGHT.getURI());

		MaterialProperty<?> useHeightMap = material.getMaterialProperties().get(StandardShader.USE_HEIGHT_MAP);
		if (useHeightMap.getType().isAssignableFrom(Boolean.class)) {
			((MaterialProperty<Boolean>) useHeightMap).value = true;
		}

		MaterialProperty<?> heightMap = material.getMaterialProperties().get(StandardShader.HEIGHT_MAP);
		if (heightMap.getType().isAssignableFrom(Texture2D.class)) {
			((MaterialProperty<Texture2D>) heightMap).value = new Texture2D(InternalFormat.RED, height.getWidth(),
					height.getHeight(), PixelFormat.RED, PixelType.UNSIGNED_SHORT, height.getData(),
					WrappingMode.REPEAT, FilteringMode.LINEAR);
		}

		MaterialProperty<?> heightScale = material.getMaterialProperties().get(StandardShader.HEIGHT_SCALE);
		if (heightScale.getType().isAssignableFrom(Float.class)) {
			((MaterialProperty<Float>) heightScale).value = 0.1f;
		}
	}

	@Override
	protected void processSystem() {
		// TODO Auto-generated method stub

	}

}
