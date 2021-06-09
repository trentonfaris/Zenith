package com.trentonfaris.zenith.graphics.shader.uniform;

import com.trentonfaris.zenith.graphics.shader.Shader;

/**
 * A {@link UniformType} describes the content of the data in a {@link Uniform}.
 * Commonly used types, such as various transformation matrices or lighting
 * information, allow other entities to safely pass data to a {@link Shader}.
 *
 * @author Trenton Faris
 */
public enum UniformType {
	/** The model matrix. */
	MODEL_MATRIX,

	/** The view matrix. */
	VIEW_MATRIX,

	/** The projection matrix. */
	PROJECTION_MATRIX,

	/** The model-view matrix. */
	MODEL_VIEW_MATRIX,

	/** The view-projection matrix. */
	VIEW_PROJECTION_MATRIX,

	/** The model-view-projection matrix. */
	MODEL_VIEW_PROJECTION_MATRIX,

	/** The normal matrix. */
	NORMAL_MATRIX,

	/** The ambient color of the environment lighting. */
	AMBIENT_COLOR,

	/** A uniform exposed to a material */
	MATERIAL
}
