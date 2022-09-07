package com.trentonfaris.zenith.graphics.shader;

import java.net.URI;
import java.nio.FloatBuffer;
import java.util.Set;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.ecs.component.light.Caster;
import com.trentonfaris.zenith.ecs.component.light.Caster.CasterType;
import com.trentonfaris.zenith.ecs.component.light.Light;
import com.trentonfaris.zenith.ecs.component.transform.Position;
import com.trentonfaris.zenith.ecs.component.transform.Rotation;
import com.trentonfaris.zenith.graphics.shader.uniform.IntUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec3Uniform;

/**
 * A {@link ForwardLitShader} draws objects sequentially from the perspective of
 * various lights.
 *
 * @author Trenton Faris
 */
public abstract class ForwardLitShader extends Shader {
	/**
	 * The maximum number of vertex lights that can be sent to a
	 * {@link ForwardLitShader}.
	 */
	public static final int MAX_NUM_VERT_LIGHTS = 10;

	public static final String AMBIENT_COLOR = "ambientColor";

	private static final String NUM_VERT_DIRECTIONAL_LIGHTS = "numVertDirectionalLights";
	private static final String NUM_VERT_POINT_LIGHTS = "numVertPointLights";
	private static final String NUM_VERT_SPOT_LIGHTS = "numVertSpotLights";

	private static final String NUM_FRAG_DIRECTIONAL_LIGHTS = "numFragDirectionalLights";
	private static final String NUM_FRAG_POINT_LIGHTS = "numFragPointLights";
	private static final String NUM_FRAG_SPOT_LIGHTS = "numFragSpotLights";

	private final IntUniform numVertDirectionalLightsUniform;
	private final IntUniform numVertPointLightsUniform;
	private final IntUniform numVertSpotLightsUniform;

	private final IntUniform numFragDirectionalLightsUniform;
	private final IntUniform numFragPointLightsUniform;
	private final IntUniform numFragSpotLightsUniform;

	/**
	 * Creates a new {@link ForwardLitShader} from the specified {@code String} URI.
	 *
	 * @param uri The {@link URI} of the shader to load
	 */
	ForwardLitShader(String uri) {
		super(uri);

		registerUniform(new Vec3Uniform(program, AMBIENT_COLOR, UniformType.AMBIENT_COLOR));

		this.numVertDirectionalLightsUniform = new IntUniform(program, NUM_VERT_DIRECTIONAL_LIGHTS,
				UniformType.MATERIAL);
		this.numVertPointLightsUniform = new IntUniform(program, NUM_VERT_POINT_LIGHTS, UniformType.MATERIAL);
		this.numVertSpotLightsUniform = new IntUniform(program, NUM_VERT_SPOT_LIGHTS, UniformType.MATERIAL);

		this.numFragDirectionalLightsUniform = new IntUniform(program, NUM_FRAG_DIRECTIONAL_LIGHTS,
				UniformType.MATERIAL);
		this.numFragPointLightsUniform = new IntUniform(program, NUM_FRAG_POINT_LIGHTS, UniformType.MATERIAL);
		this.numFragSpotLightsUniform = new IntUniform(program, NUM_FRAG_SPOT_LIGHTS, UniformType.MATERIAL);
	}

	/**
	 * Sets the per-vertex lights. Only the first {@link #MAX_NUM_VERT_LIGHTS} will
	 * be considered. Any others will be ignored.
	 *
	 * @param world The target {@link World}
	 * @param vertLights The set of per-vertex lights to be drawn
	 */
	public final void setVertLights(World world, Set<Entity> vertLights) {
		if (vertLights == null) {
			String errorMsg = "Cannot assign a set of per-vertex lights to uniform locations with null vertLights.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (vertLights.size() > MAX_NUM_VERT_LIGHTS) {
			Zenith.getLogger().warn("Cannot assign more than " + MAX_NUM_VERT_LIGHTS
					+ " per-vertex lights. Additional lights will be ignored.");
		}

		ComponentMapper<Caster> mCaster = world.getMapper(Caster.class);
		ComponentMapper<Light> mLight = world.getMapper(Light.class);
		ComponentMapper<Position> mPosition = world.getMapper(Position.class);
		ComponentMapper<Rotation> mRotation = world.getMapper(Rotation.class);

		int iDL = 0;
		int iPL = 0;
		int iSL = 0;

		int numVertLights = Math.min(MAX_NUM_VERT_LIGHTS, vertLights.size());

		for (Entity vertexLight : vertLights) {
			if ((iDL + iPL + iSL) == numVertLights) {
				break;
			}

			Caster caster = mCaster.create(vertexLight.getId());
			Light light = mLight.create(vertexLight.getId());

			// Set the uniforms dynamically using OpenGL setters.
			if (caster.casterType == CasterType.SPOT) {
				try (MemoryStack stack = MemoryStack.stackPush()) {
					Position position = mPosition.create(vertexLight.getId());
					Rotation rotation = mRotation.create(vertexLight.getId());

					FloatBuffer pLightColor = stack.mallocFloat(3);
					GL20.glUniform3fv(
							GL20.glGetUniformLocation(program, "vertSpotLights[" + iSL + "].pointLight.light.color"),
							light.color.get(pLightColor));

					FloatBuffer pLightPosition = stack.mallocFloat(3);
					GL20.glUniform3fv(
							GL20.glGetUniformLocation(program, "vertSpotLights[" + iSL + "].pointLight.position"),
							position.xyz.get(pLightPosition));

					GL20.glUniform1f(
							GL20.glGetUniformLocation(program, "vertSpotLights[" + iSL + "].pointLight.radius"),
							caster.radius);

					FloatBuffer pLightRotation = stack.mallocFloat(3);
					GL20.glUniform3fv(GL20.glGetUniformLocation(program, "vertSpotLights[" + iSL + "].direction"),
							new Vector3f(0, 0, -1).rotate(rotation.xyzw).normalize().get(pLightRotation));

					GL20.glUniform1f(GL20.glGetUniformLocation(program, "vertSpotLights[" + iSL + "].innerCutoffAngle"),
							(float) Math.toRadians(caster.innerCutoffAngle));

					GL20.glUniform1f(GL20.glGetUniformLocation(program, "vertSpotLights[" + iSL + "].outerCutoffAngle"),
							(float) Math.toRadians(caster.outerCutoffAngle));

					iSL++;
				}
			} else if (caster.casterType == CasterType.POINT) {
				try (MemoryStack stack = MemoryStack.stackPush()) {
					Position position = mPosition.create(vertexLight.getId());

					FloatBuffer pLightColor = stack.mallocFloat(3);
					GL20.glUniform3fv(GL20.glGetUniformLocation(program, "vertPointLights[" + iPL + "].light.color"),
							light.color.get(pLightColor));

					FloatBuffer pLightPosition = stack.mallocFloat(3);
					GL20.glUniform3fv(GL20.glGetUniformLocation(program, "vertPointLights[" + iPL + "].position"),
							position.xyz.get(pLightPosition));

					GL20.glUniform1f(GL20.glGetUniformLocation(program, "vertPointLights[" + iPL + "].radius"),
							caster.radius);

					iPL++;
				}
			} else if (caster.casterType == CasterType.DIRECTIONAL) {
				try (MemoryStack stack = MemoryStack.stackPush()) {
					Rotation rotation = mRotation.create(vertexLight.getId());

					FloatBuffer pLightColor = stack.mallocFloat(3);
					GL20.glUniform3fv(
							GL20.glGetUniformLocation(program, "vertDrectionalLights[" + iDL + "].light.color"),
							light.color.get(pLightColor));

					FloatBuffer pLightDirection = stack.mallocFloat(3);
					GL20.glUniform3fv(GL20.glGetUniformLocation(program, "vertDrectionalLights[" + iDL + "].direction"),
							new Vector3f(0, 0, -1).rotate(rotation.xyzw).normalize().get(pLightDirection));

					iDL++;
				}
			}
		}

		numVertDirectionalLightsUniform.set(iDL);
		numVertPointLightsUniform.set(iPL);
		numVertSpotLightsUniform.set(iSL);
	}

	/**
	 * Sets the per-fragment light.
	 *
	 * @param world The target {@link World}
	 * @param fragLight The set of per-fragment lights to be drawn
	 */
	public final void setFragLight(World world, Entity fragLight) {
		if (fragLight == null) {
			String errorMsg = "Cannot assign a per-fragment light to uniform location with null fragLight.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		ComponentMapper<Caster> mCaster = world.getMapper(Caster.class);
		ComponentMapper<Light> mLight = world.getMapper(Light.class);
		ComponentMapper<Position> mPosition = world.getMapper(Position.class);
		ComponentMapper<Rotation> mRotation = world.getMapper(Rotation.class);

		int iDL = 0;
		int iPL = 0;
		int iSL = 0;

		Caster caster = mCaster.create(fragLight.getId());
		Light light = mLight.get(fragLight.getId());

		// Set the uniforms dynamically using OpenGL setters.
		if (caster.casterType == CasterType.SPOT) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				Position position = mPosition.create(fragLight.getId());
				Rotation rotation = mRotation.create(fragLight.getId());

				FloatBuffer pLightColor = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragSpotLights[0].pointLight.light.color"),
						light.color.get(pLightColor));

				FloatBuffer pLightPosition = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragSpotLights[0].pointLight.position"),
						position.xyz.get(pLightPosition));

				GL20.glUniform1f(GL20.glGetUniformLocation(program, "fragSpotLights[0].pointLight.radius"),
						caster.radius);

				FloatBuffer pLightRotation = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragSpotLights[0].direction"),
						new Vector3f(0, 0, -1).rotate(rotation.xyzw).normalize().get(pLightRotation));

				GL20.glUniform1f(GL20.glGetUniformLocation(program, "fragSpotLights[0].innerCutoffAngle"),
						(float) Math.toRadians(caster.innerCutoffAngle));

				GL20.glUniform1f(GL20.glGetUniformLocation(program, "fragSpotLights[0].outerCutoffAngle"),
						(float) Math.toRadians(caster.outerCutoffAngle));

				iSL++;
			}
		} else if (caster.casterType == CasterType.POINT) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				Position position = mPosition.create(fragLight.getId());

				FloatBuffer pLightColor = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragPointLights[0].light.color"),
						light.color.get(pLightColor));

				FloatBuffer pLightPosition = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragPointLights[0].position"),
						position.xyz.get(pLightPosition));

				GL20.glUniform1f(GL20.glGetUniformLocation(program, "fragPointLights[0].radius"), caster.radius);

				iPL++;
			}
		} else if (caster.casterType == CasterType.DIRECTIONAL) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				Rotation rotation = mRotation.create(fragLight.getId());

				FloatBuffer pLightColor = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragDirectionalLights[0].light.color"),
						light.color.get(pLightColor));

				FloatBuffer pLightDirection = stack.mallocFloat(3);
				GL20.glUniform3fv(GL20.glGetUniformLocation(program, "fragDirectionalLights[0].direction"),
						new Vector3f(0, 0, -1).rotate(rotation.xyzw).normalize().get(pLightDirection));

				iDL++;
			}
		}

		numFragDirectionalLightsUniform.set(iDL);
		numFragPointLightsUniform.set(iPL);
		numFragSpotLightsUniform.set(iSL);
	}
}
