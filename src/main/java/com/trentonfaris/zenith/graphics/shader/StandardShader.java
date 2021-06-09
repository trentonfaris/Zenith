package com.trentonfaris.zenith.graphics.shader;

import com.trentonfaris.zenith.graphics.shader.uniform.BoolUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.FloatUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Sampler2DUniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.shader.uniform.Vec3Uniform;
import com.trentonfaris.zenith.resource.resources.Shaders;

/**
 * A {@link StandardShader} draws objects using a physically-based shading
 * method.
 *
 * @author Trenton Faris
 */
public final class StandardShader extends ForwardLitShader {
	// Names of uniforms in shared shaders.
	public static final String USE_ALBEDO_MAP = "material.useAlbedoMap";
	public static final String ALBEDO_MAP = "material.albedoMap";
	public static final String ALBEDO_COLOR = "material.albedoColor";
	public static final String USE_METALLIC_MAP = "material.useMetallicMap";
	public static final String METALLIC_MAP = "material.metallicMap";
	public static final String METALLIC_VALUE = "material.metallicValue";
	public static final String USE_ROUGHNESS_MAP = "material.useRoughnessMap";
	public static final String ROUGHNESS_MAP = "material.roughnessMap";
	public static final String ROUGHNESS_VALUE = "material.roughnessValue";
	public static final String USE_AO_MAP = "material.useAoMap";
	public static final String AO_MAP = "material.aoMap";
	public static final String USE_NORMAL_MAP = "material.useNormalMap";
	public static final String NORMAL_MAP = "material.normalMap";
	public static final String USE_HEIGHT_MAP = "material.useHeightMap";
	public static final String HEIGHT_MAP = "material.heightMap";
	public static final String HEIGHT_SCALE = "material.heightScale";
	// Names of uniforms in the vertex shader.
	private static final String VIEW = "view";
	private static final String PROJECTION = "projection";
	private static final String MODEL_VIEW = "modelView";

	/** Creates a new {@link StandardShader}. */
	StandardShader() {
		super(Shaders.STANDARD.getURI());

		// Uniforms in the vertex shader.
		registerUniform(new Mat4Uniform(program, VIEW, UniformType.VIEW_MATRIX));
		registerUniform(new Mat4Uniform(program, PROJECTION, UniformType.PROJECTION_MATRIX));
		registerUniform(new Mat4Uniform(program, MODEL_VIEW, UniformType.MODEL_VIEW_MATRIX));

		// Uniforms in the fragment shader.
		registerUniform(new BoolUniform(program, USE_ALBEDO_MAP, UniformType.MATERIAL));
		registerUniform(new Sampler2DUniform(program, ALBEDO_MAP, UniformType.MATERIAL));
		registerUniform(new Vec3Uniform(program, ALBEDO_COLOR, UniformType.MATERIAL));

		registerUniform(new BoolUniform(program, USE_METALLIC_MAP, UniformType.MATERIAL));
		registerUniform(new Sampler2DUniform(program, METALLIC_MAP, UniformType.MATERIAL));
		registerUniform(new FloatUniform(program, METALLIC_VALUE, UniformType.MATERIAL));

		registerUniform(new BoolUniform(program, USE_ROUGHNESS_MAP, UniformType.MATERIAL));
		registerUniform(new Sampler2DUniform(program, ROUGHNESS_MAP, UniformType.MATERIAL));
		registerUniform(new FloatUniform(program, ROUGHNESS_VALUE, UniformType.MATERIAL));

		registerUniform(new BoolUniform(program, USE_AO_MAP, UniformType.MATERIAL));
		registerUniform(new Sampler2DUniform(program, AO_MAP, UniformType.MATERIAL));

		registerUniform(new BoolUniform(program, USE_NORMAL_MAP, UniformType.MATERIAL));
		registerUniform(new Sampler2DUniform(program, NORMAL_MAP, UniformType.MATERIAL));

		registerUniform(new BoolUniform(program, USE_HEIGHT_MAP, UniformType.MATERIAL));
		registerUniform(new Sampler2DUniform(program, HEIGHT_MAP, UniformType.MATERIAL));
		registerUniform(new FloatUniform(program, HEIGHT_SCALE, UniformType.MATERIAL));
	}
}
