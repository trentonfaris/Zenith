package com.trentonfaris.zenith.demo.ecs.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.trentonfaris.zenith.demo.resource.resources.DemoImages;
import com.trentonfaris.zenith.ecs.component.render.Renderable;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Scale;
import com.trentonfaris.zenith.ecs.system.light.LightSystem;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.material.property.*;
import com.trentonfaris.zenith.graphics.model.Model;
import com.trentonfaris.zenith.graphics.shader.SkyboxShader;
import com.trentonfaris.zenith.graphics.shader.StandardShader;
import com.trentonfaris.zenith.graphics.texture.*;
import com.trentonfaris.zenith.image.Image;
import com.trentonfaris.zenith.resource.resources.Images;
import com.trentonfaris.zenith.resource.resources.Models;
import org.joml.Vector3f;

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

    private void loadGround() {
        int entity = world.create();

        Scale scale = mScale.create(entity);
        scale.xyz = new Vector3f(50, 1, 50);

        Renderable renderable = mRenderable.create(entity);
        renderable.model = Model.loadModel(Models.PLANE.getURI());

        Material material = renderable.model.getMeshes().get(0).getMaterial();

        Property albedoColor = material.getProperties().get(StandardShader.ALBEDO_COLOR);
        if (albedoColor instanceof Vec3Property) {
            ((Vec3Property) albedoColor).value = new Vector3f(0.2f);
        }
    }

    private void loadSky() {
        Image right = Image.loadImage(Images.DEFAULT_SKYBOX_RIGHT.getURI());
        Image left = Image.loadImage(Images.DEFAULT_SKYBOX_LEFT.getURI());
        Image top = Image.loadImage(Images.DEFAULT_SKYBOX_TOP.getURI());
        Image bottom = Image.loadImage(Images.DEFAULT_SKYBOX_BOTTOM.getURI());
        Image front = Image.loadImage(Images.DEFAULT_SKYBOX_FRONT.getURI());
        Image back = Image.loadImage(Images.DEFAULT_SKYBOX_BACK.getURI());

        Material skyboxMaterial = lightSystem.getSkyboxMaterial();
        Property skybox = skyboxMaterial.getProperties().get(SkyboxShader.SKYBOX);
        if (skybox instanceof CubemapProperty) {
            ((CubemapProperty) skybox).value = new Cubemap(right, left, top, bottom, front, back);
        }
    }

    private void loadCube() {
        int entity = world.create();

        Position position = mPosition.create(entity);
        position.xyz = new Vector3f(0, 0.5f, 0);

        Renderable renderable = mRenderable.create(entity);
        renderable.model = Model.loadModel(Models.CUBE.getURI());

        Material material = renderable.model.getMeshes().get(0).getMaterial();

        // Albedo
        Image albedo = Image.loadImage(DemoImages.ROCK_ALBEDO.getURI());

        Property useAlbedoMap = material.getProperties().get(StandardShader.USE_ALBEDO_MAP);
        if (useAlbedoMap instanceof BoolProperty) {
            ((BoolProperty) useAlbedoMap).value = true;
        }

        Property albedoMap = material.getProperties().get(StandardShader.ALBEDO_MAP);
        if (albedoMap instanceof Texture2DProperty) {
            ((Texture2DProperty) albedoMap).value = new Texture2D(InternalFormat.SRGB, albedo.width(),
                    albedo.height(), PixelFormat.RGB, PixelType.UNSIGNED_SHORT, albedo.data(),
                    WrappingMode.REPEAT, FilteringMode.LINEAR);
        }

        // Roughness
        Image roughness = Image.loadImage(DemoImages.ROCK_ROUGHNESS.getURI());

        Property useRoughnessMap = material.getProperties().get(StandardShader.USE_ROUGHNESS_MAP);
        if (useRoughnessMap instanceof BoolProperty) {
            ((BoolProperty) useRoughnessMap).value = true;
        }

        Property roughnessMap = material.getProperties().get(StandardShader.ROUGHNESS_MAP);
        if (roughnessMap instanceof Texture2DProperty) {
            ((Texture2DProperty) roughnessMap).value = new Texture2D(InternalFormat.RED, roughness.width(),
                    roughness.height(), PixelFormat.RED, PixelType.UNSIGNED_BYTE, roughness.data(),
                    WrappingMode.REPEAT, FilteringMode.LINEAR);
        }

        // Ambient occlusion
        Image ao = Image.loadImage(DemoImages.ROCK_AO.getURI());

        Property useAoMap = material.getProperties().get(StandardShader.USE_AO_MAP);
        if (useAoMap instanceof BoolProperty) {
            ((BoolProperty) useAoMap).value = true;
        }

        Property aoMap = material.getProperties().get(StandardShader.AO_MAP);
        if (aoMap instanceof Texture2DProperty) {
            ((Texture2DProperty) aoMap).value = new Texture2D(InternalFormat.RED, ao.width(),
                    ao.height(), PixelFormat.RED, PixelType.UNSIGNED_BYTE, ao.data(), WrappingMode.REPEAT,
                    FilteringMode.LINEAR);
        }

        // Normal
        Image normal = Image.loadImage(DemoImages.ROCK_NORMAL.getURI());

        Property useNormalMap = material.getProperties().get(StandardShader.USE_NORMAL_MAP);
        if (useNormalMap instanceof BoolProperty) {
            ((BoolProperty) useNormalMap).value = true;
        }

        Property normalMap = material.getProperties().get(StandardShader.NORMAL_MAP);
        if (normalMap instanceof Texture2DProperty) {
            ((Texture2DProperty) normalMap).value = new Texture2D(InternalFormat.RGB, normal.width(),
                    normal.height(), PixelFormat.RGB, PixelType.UNSIGNED_SHORT, normal.data(),
                    WrappingMode.REPEAT, FilteringMode.LINEAR);
        }

        // Height
        Image height = Image.loadImage(DemoImages.ROCK_HEIGHT.getURI());

        Property useHeightMap = material.getProperties().get(StandardShader.USE_HEIGHT_MAP);
        if (useHeightMap instanceof BoolProperty) {
            ((BoolProperty) useHeightMap).value = true;
        }

        Property heightMap = material.getProperties().get(StandardShader.HEIGHT_MAP);
        if (heightMap instanceof Texture2DProperty) {
            ((Texture2DProperty) heightMap).value = new Texture2D(InternalFormat.RED, height.width(),
                    height.height(), PixelFormat.RED, PixelType.UNSIGNED_SHORT, height.data(),
                    WrappingMode.REPEAT, FilteringMode.LINEAR);
        }

        Property heightScale = material.getProperties().get(StandardShader.HEIGHT_SCALE);
        if (heightScale instanceof FloatProperty) {
            ((FloatProperty) heightScale).value = 0.1f;
        }
    }

    @Override
    protected void processSystem() {
        // TODO Auto-generated method stub

    }

}
