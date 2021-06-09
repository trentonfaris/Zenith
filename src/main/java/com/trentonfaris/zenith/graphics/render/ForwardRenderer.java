package com.trentonfaris.zenith.graphics.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.ecs.component.camera.Camera;
import com.trentonfaris.zenith.ecs.component.light.Caster;
import com.trentonfaris.zenith.ecs.component.light.Caster.CasterType;
import com.trentonfaris.zenith.ecs.component.light.Light;
import com.trentonfaris.zenith.ecs.component.light.Light.Priority;
import com.trentonfaris.zenith.ecs.component.misc.State;
import com.trentonfaris.zenith.ecs.component.render.Renderable;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transformation.ModelMatrix;
import com.trentonfaris.zenith.ecs.component.transformation.ProjectionMatrix;
import com.trentonfaris.zenith.ecs.component.transformation.ViewMatrix;
import com.trentonfaris.zenith.ecs.system.core.LayerSystem;
import com.trentonfaris.zenith.ecs.system.light.LightSystem;
import com.trentonfaris.zenith.graphics.Graphics;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.model.Mesh;
import com.trentonfaris.zenith.graphics.shader.ForwardLitShader;
import com.trentonfaris.zenith.graphics.shader.Shader;
import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec3Uniform;

/**
 * The {@link ForwardRenderer} renders the scene by way of multiple forward
 * render passes. Forward rendering is quite expensive. It is recommended to
 * never use more per-fragment lights than necessary. If more per-fragment
 * lights are required than the {@link ForwardRenderer} will support, use the
 * {@link DeferredRenderPath}.
 *
 * <p>
 * Lights that have their {@link Priority} set to {@link Priority#NOT_IMPORTANT}
 * are always rendered per-vertex. Lights that have their {@link Priority} set
 * to {@link Priority#IMPORTANT} are always rendered per-fragment. If there are
 * still less per-fragment lights than {@link #numFragLights}, then more lights
 * are rendered per-fragment, in order of decreasing score, where a light's
 * score is equal to {@code (1 / distance to target) * intensity}.
 *
 * <p>
 * Multiple passes are rendered with batches of per-vertex and a per-fragment
 * lights. There is an <i>absolute max</i> of
 * {@link ForwardLitShader#MAX_NUM_VERT_LIGHTS} per-vertex lights sent in each
 * batch.
 *
 * @author Trenton Faris
 */
public final class ForwardRenderer extends Renderer {
	/** The {@link ForwardRenderer} instance. */
	private static ForwardRenderer instance = null;

	/** The number of lights rendered per-fragment. */
	public int numFragLights = 4;

	private ForwardRenderer() {
	}

	@Override
	public void render(World world, int cameraId) {
		Camera camera = world.getMapper(Camera.class).create(cameraId);
		ViewMatrix viewMatrix = world.getMapper(ViewMatrix.class).create(cameraId);
		ProjectionMatrix projectionMatrix = world.getMapper(ProjectionMatrix.class).create(cameraId);

		Matrix4f viewProjectionMatrix = projectionMatrix.mat.mul(viewMatrix.mat, new Matrix4f());

		Set<Integer> rendered = new HashSet<>();

		int layerId = camera.layerMask.nextSetBit(0);
		while (layerId >= 0) {
			Set<Integer> entities = world.getSystem(LayerSystem.class).getEntities(layerId);

			for (Integer entityId : entities) {
				if (!world.getEntity(entityId).isActive()) {
					continue;
				}

				if (rendered.contains(entityId)) {
					continue;
				}

				ComponentMapper<State> mState = world.getMapper(State.class);
				ComponentMapper<ModelMatrix> mModelMatrix = world.getMapper(ModelMatrix.class);
				ComponentMapper<Renderable> mRenderable = world.getMapper(Renderable.class);

				if (!mModelMatrix.has(entityId) || !mRenderable.has(entityId)) {
					continue;
				}

				State state = mState.create(entityId);
				if (!state.active) {
					continue;
				}

//				System.out.println(entityId);

				ModelMatrix modelMatrix = mModelMatrix.get(entityId);

				Matrix4f modelViewMatrix = viewMatrix.mat.mul(modelMatrix.mat, new Matrix4f());
				Matrix4f modelViewProjectionMatrix = projectionMatrix.mat.mul(viewMatrix.mat, new Matrix4f())
						.mul(modelMatrix.mat, new Matrix4f());

				Renderable renderable = mRenderable.get(entityId);

				for (Mesh mesh : renderable.model.getMeshes()) {
					Material material = mesh.getMaterial();
					if (material == null) {
						continue;
					}

					Shader shader = Zenith.getEngine().getGraphics().getShaderManager()
							.getShader(material.getShaderType());
					shader.use();

					Map<String, Uniform> uniforms = shader.getUniforms();
					for (Entry<String, Uniform> entry : uniforms.entrySet()) {
						Uniform uniform = entry.getValue();

						if (!(uniform instanceof Mat4Uniform)) {
							continue;
						}

						Mat4Uniform mat4Uniform = (Mat4Uniform) uniform;

						if (uniform.getUniformType() == UniformType.MODEL_MATRIX) {
							mat4Uniform.set(modelMatrix.mat);
						} else if (uniform.getUniformType() == UniformType.VIEW_MATRIX) {
							mat4Uniform.set(viewMatrix.mat);
						} else if (uniform.getUniformType() == UniformType.PROJECTION_MATRIX) {
							mat4Uniform.set(projectionMatrix.mat);
						} else if (uniform.getUniformType() == UniformType.MODEL_VIEW_MATRIX) {
							mat4Uniform.set(modelViewMatrix);
						} else if (uniform.getUniformType() == UniformType.VIEW_PROJECTION_MATRIX) {
							mat4Uniform.set(viewProjectionMatrix);
						} else if (uniform.getUniformType() == UniformType.MODEL_VIEW_PROJECTION_MATRIX) {
							mat4Uniform.set(modelViewProjectionMatrix);
						}
					}

					if (shader instanceof ForwardLitShader) {
						ForwardLitShader forwardLitShader = (ForwardLitShader) shader;

						LightSystem lightSystem = world.getSystem(LightSystem.class);

						for (Entry<String, Uniform> entry : uniforms.entrySet()) {
							Uniform uniform = entry.getValue();

							if (!(uniform instanceof Vec3Uniform)) {
								continue;
							}

							Vec3Uniform vec3Uniform = (Vec3Uniform) uniform;

							if (uniform.getUniformType() == UniformType.AMBIENT_COLOR) {
								vec3Uniform.set(lightSystem.getAmbientColor());
							}
						}

						Set<Entity> vertLights = new HashSet<>(lightSystem.getNotImportantLights());
						Set<Entity> fragLights = new HashSet<>(lightSystem.getImportantLights());

						Entity[] autoLightsArr = lightSystem.getAutoLights().toArray(new Entity[0]);
						Entity[] sortedAutoLightsArr = sortLights(world, autoLightsArr,
								modelMatrix.mat.getTranslation(new Vector3f()));

						List<Entity> sortedAutoLights = Arrays.asList(sortedAutoLightsArr);

						int i = 0;
						while (fragLights.size() < numFragLights && i < sortedAutoLights.size()) {
							fragLights.add(sortedAutoLights.get(i));

							i++;
						}

						vertLights.addAll(sortedAutoLights.subList(i, sortedAutoLights.size()));

						drawForwardPasses(world, forwardLitShader, vertLights, fragLights, mesh);
					} else {
						mesh.draw();
					}
				}

				rendered.add(entityId);
			}

			layerId = camera.layerMask.nextSetBit(layerId + 1);
		}
	}

	private Entity[] sortLights(World world, Entity[] lights, Vector3f position) {
		if (lights.length <= 1) {
			return lights;
		}

		Entity[] left;
		Entity[] right;

		if (lights.length % 2 == 0) {
			left = new Entity[lights.length / 2];
			right = new Entity[lights.length / 2];
		} else {
			left = new Entity[(lights.length - 1) / 2];
			right = new Entity[(lights.length + 1) / 2];
		}

		for (int i = 0; i < lights.length; i++) {
			if (i < left.length) {
				left[i] = lights[i];
			} else {
				right[i - left.length] = lights[i];
			}
		}

		Entity[] sortedLeft = sortLights(world, left, position);
		Entity[] sortedRight = sortLights(world, right, position);

		return mergeLights(world, sortedLeft, sortedRight, position);
	}

	private Entity[] mergeLights(World world, Entity[] left, Entity[] right, Vector3f position) {
		Entity[] result = new Entity[left.length + right.length];

		int i = 0;
		int j = 0;

		while (i < left.length && j < right.length) {
			float leftScore = scoreLight(world, left[i].getId(), position);
			float rightScore = scoreLight(world, right[i].getId(), position);

			if (leftScore >= rightScore) {
				result[i + j] = left[i];
				i++;
			} else {
				result[i + j] = right[j];
				j++;
			}
		}

		while (i < left.length) {
			result[i + j] = left[i];
			i++;
		}

		while (j < right.length) {
			result[i + j] = right[j];
			j++;
		}

		return result;
	}

	private float scoreLight(World world, int lightId, Vector3f position) {
		Caster caster = world.getMapper(Caster.class).create(lightId);
		Light light = world.getMapper(Light.class).get(lightId);

		float score = Float.MAX_VALUE;
		if (caster.casterType != CasterType.DIRECTIONAL) {
			Position lightPosition = world.getMapper(Position.class).create(lightId);

			float dist = lightPosition.xyz.distance(position);
			if (dist > 0) {
				score = (1 / dist) * light.intensity;
			}
		}

		return score;
	}

	private void drawForwardPasses(World world, ForwardLitShader forwardLitShader, Set<Entity> vertLights,
			Set<Entity> fragLights, Mesh mesh) {
		Entity[] vertLightsArr = vertLights.toArray(new Entity[0]);
		Entity[] fragLightsArr = fragLights.toArray(new Entity[0]);

		// Cache graphics settings
		Graphics graphics = Zenith.getEngine().getGraphics();

		boolean depthTesting = graphics.isDepthTesting();
		boolean depthMasking = graphics.isDepthMasking();
		int depthFunc = graphics.getDepthFunc();
		boolean blending = graphics.isBlending();
		int blendSrc = graphics.getBlendSrc();
		int blendDst = graphics.getBlendDst();

		// Batched forward passes.
		int i = 0;
		int j = 0;
		while (i < vertLightsArr.length || j < fragLightsArr.length) {
			Set<Entity> batchedVertLights = new HashSet<>();
			if (i < vertLightsArr.length) {
				int k = Math.min(vertLightsArr.length, ForwardLitShader.MAX_NUM_VERT_LIGHTS);
				batchedVertLights.addAll(Arrays.asList(vertLightsArr).subList(i, k));
			}

			// After the first pass
			if (i > 0 || j > 0) {
				// Enable blending without rewriting depth values.
				graphics.setDepthTesting(true);
				graphics.setDepthMasking(false);
				graphics.setDepthFunc(GL11.GL_EQUAL);
				graphics.setBlending(true);
				graphics.setBlendSrc(GL11.GL_ONE);
				graphics.setBlendDst(GL11.GL_ONE);
				graphics.update();
			}

			forwardLitShader.setVertLights(world, batchedVertLights);
			forwardLitShader.setFragLight(world, fragLightsArr[j]);

			mesh.draw();

			i += ForwardLitShader.MAX_NUM_VERT_LIGHTS;
			j++;
		}

		// Reset depth and blending properties.
		graphics.setDepthTesting(depthTesting);
		graphics.setDepthMasking(depthMasking);
		graphics.setDepthFunc(depthFunc);
		graphics.setBlending(blending);
		graphics.setBlendSrc(blendSrc);
		graphics.setBlendDst(blendDst);
		graphics.update();
	}

	/**
	 * Gets the {@link ForwardRenderer} instance. If the instance hasn't been
	 * instantiated, it will do so.
	 *
	 * @return The {@link ForwardRenderer}.
	 */
	public static synchronized ForwardRenderer getInstance() {
		if (instance == null) {
			instance = new ForwardRenderer();
		}

		return instance;
	}

	/**
	 * Gets the number of lights to render per-fragment.
	 * 
	 * @return The number of lights to render per-fragment.
	 */
	public int getNumFragLights() {
		return numFragLights;
	}

	/**
	 * Sets the number of lights to render per-fragment.
	 * 
	 * @param numFragLights
	 */
	public void setNumFragLights(int numFragLights) {
		this.numFragLights = numFragLights;
	}
}
