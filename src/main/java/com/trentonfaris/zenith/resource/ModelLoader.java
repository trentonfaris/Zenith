package com.trentonfaris.zenith.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.exception.ResourceIOException;
import com.trentonfaris.zenith.exception.ResourceNotFoundException;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.material.MaterialProperty;
import com.trentonfaris.zenith.graphics.model.Attribute;
import com.trentonfaris.zenith.graphics.model.Mesh;
import com.trentonfaris.zenith.graphics.model.Model;
import com.trentonfaris.zenith.graphics.model.PrimitiveType;
import com.trentonfaris.zenith.graphics.model.Vertex;
import com.trentonfaris.zenith.graphics.shader.StandardShader;
import com.trentonfaris.zenith.utility.Utility;

/**
 * The {@link ModelLoader} class is responsible for loading model data.
 *
 * @author Trenton Faris
 */
public final class ModelLoader extends ResourceLoader<Model> {
	/** The scheme for a {@link ModelLoader}. */
	public static final String SCHEME = "model";

	/** Creates a new {@link ModelLoader}. */
	public ModelLoader() {
		super(SCHEME);
	}

	@Override
	public Model load(URI uri) throws ResourceIOException, ResourceNotFoundException {
		if (uri == null) {
			String errorMsg = "Cannot load a Model from a null URI.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		String path = ResourceManager.RESOURCES_DIRECTORY.getPath() + uri.getPath();

		File file;
		if (uri.getHost().equalsIgnoreCase(Utility.PACKAGED_FILE_HOST)) {
			try {
				file = Utility.getPackagedFile(path);
			} catch (IOException e) {
				Zenith.getLogger().error("Cannot read the resource: " + uri.getPath());
				throw new ResourceIOException(uri.getPath());
			}
		} else {
			try {
				file = Utility.getFile(ResourceManager.RESOURCES_DIRECTORY.getPath() + uri.getPath());
			} catch (FileNotFoundException e) {
				Zenith.getLogger().error("Cannot find the resource: " + uri.getPath());
				throw new ResourceNotFoundException(uri.getPath());
			}
		}

		AIScene aiScene = Assimp.aiImportFile(file.getAbsolutePath(),
				Assimp.aiProcess_Triangulate | Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace);

		if (aiScene == null || aiScene.mRootNode() == null
				|| (aiScene.mFlags() & Assimp.AI_SCENE_FLAGS_INCOMPLETE) == Assimp.AI_TRUE) {
			Zenith.getLogger().error("Cannot read the resource: " + uri.getPath());
			throw new ResourceIOException(uri.getPath());
		}

		return new Model(processNode(aiScene.mRootNode(), aiScene));
	}

	/**
	 * Recursively process the nodes in the scene.
	 *
	 * @param aiNode
	 * @param aiScene
	 * @return A list of meshes.
	 */
	private List<Mesh> processNode(AINode aiNode, AIScene aiScene) {
		List<Mesh> meshes = new ArrayList<>();

		for (int i = 0; i < aiNode.mNumMeshes(); i++) {
			AIMesh aiMesh = AIMesh.create(aiScene.mMeshes().get(aiNode.mMeshes().get(i)));
			meshes.add(processMesh(aiMesh, aiScene));
		}

		for (int i = 0; i < aiNode.mNumChildren(); i++) {
			meshes.addAll(processNode(AINode.create(aiNode.mChildren().get(i)), aiScene));
		}

		return meshes;
	}

	/**
	 * Process a mesh.
	 *
	 * @param aiMesh
	 * @param aiScene
	 * @return The processed {@link Mesh}.
	 */
	@SuppressWarnings("unchecked")
	private Mesh processMesh(AIMesh aiMesh, AIScene aiScene) {
		List<Vertex> vertices = new ArrayList<>();
		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();

			// Position attribute
			AIVector3D aiPosition = aiMesh.mVertices().get(i);
			attributes.add(new Attribute<Vector3f>(new Vector3f(aiPosition.x(), aiPosition.y(), aiPosition.z())));

			// UV attribute
			if (aiMesh.mNumUVComponents().get(0) > 0) {
				AIVector3D aiTexCoord = aiMesh.mTextureCoords(0).get(i);
				attributes.add(new Attribute<Vector2f>(new Vector2f(aiTexCoord.x(), aiTexCoord.y())));
			}

			// Normal attribute
			AIVector3D aiNormal = aiMesh.mNormals().get(i);
			attributes.add(new Attribute<Vector3f>(new Vector3f(aiNormal.x(), aiNormal.y(), aiNormal.z()).normalize()));

			// Tangent attribute
			AIVector3D aiTangent = aiMesh.mTangents().get(i);
			attributes.add(
					new Attribute<Vector3f>(new Vector3f(aiTangent.x(), aiTangent.y(), aiTangent.z()).normalize()));

			// Bitangent attribute
			AIVector3D aiBitangent = aiMesh.mBitangents().get(i);
			attributes.add(new Attribute<Vector3f>(
					new Vector3f(aiBitangent.x(), aiBitangent.y(), aiBitangent.z()).normalize()));

			vertices.add(new Vertex(attributes));
		}

		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < aiMesh.mNumFaces(); i++) {
			AIFace aiFace = aiMesh.mFaces().get(i);
			for (int j = 0; j < aiFace.mNumIndices(); j++) {
				indices.add(aiFace.mIndices().get(j));
			}
		}

		// TODO : Import PBR material properties

		Material material = new Material(StandardShader.class);

		// Set default standard material properties.
		MaterialProperty<?> albedoColor = material.getMaterialProperties().get(StandardShader.ALBEDO_COLOR);
		if (albedoColor.value instanceof Vector3f) {
			((MaterialProperty<Vector3f>) albedoColor).value = new Vector3f(1);
		}

		MaterialProperty<?> roughnessValue = material.getMaterialProperties().get(StandardShader.ROUGHNESS_VALUE);
		if (roughnessValue.value instanceof Float) {
			((MaterialProperty<Float>) roughnessValue).value = 0.5f;
		}

		if (aiMesh.mMaterialIndex() >= 0) {
			AIMaterial aiMaterial = AIMaterial.create(aiScene.mMaterials().get(aiMesh.mMaterialIndex()));

			AIColor4D color = AIColor4D.create();

			int result;
			result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0,
					color);
			if (result == 0) {
				if (albedoColor.value instanceof Vector3f) {
					((MaterialProperty<Vector3f>) albedoColor).value = new Vector3f(color.r(), color.g(), color.b());
				}
			}
		}

		return new Mesh(vertices, indices, PrimitiveType.TRIANGLES, material);
	}
}
