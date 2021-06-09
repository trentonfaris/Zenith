package com.trentonfaris.zenith.ecs.system.render;

import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.ecs.component.camera.Camera;
import com.trentonfaris.zenith.ecs.component.camera.Camera.ClearType;
import com.trentonfaris.zenith.ecs.component.transformation.ProjectionMatrix;
import com.trentonfaris.zenith.ecs.component.transformation.ViewMatrix;
import com.trentonfaris.zenith.ecs.component.ui.Rect;
import com.trentonfaris.zenith.ecs.system.camera.CameraSystem;
import com.trentonfaris.zenith.ecs.system.light.LightSystem;
import com.trentonfaris.zenith.graphics.Graphics;
import com.trentonfaris.zenith.graphics.framebuffer.Attachment;
import com.trentonfaris.zenith.graphics.framebuffer.Framebuffer;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.model.Mesh;
import com.trentonfaris.zenith.graphics.model.Model;
import com.trentonfaris.zenith.graphics.model.Vertex;
import com.trentonfaris.zenith.graphics.render.ForwardRenderer;
import com.trentonfaris.zenith.graphics.shader.Shader;
import com.trentonfaris.zenith.graphics.shader.uniform.Mat4Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.Uniform;
import com.trentonfaris.zenith.graphics.shader.uniform.UniformType;
import com.trentonfaris.zenith.graphics.texture.FilteringMode;
import com.trentonfaris.zenith.graphics.texture.InternalFormat;
import com.trentonfaris.zenith.graphics.texture.PixelFormat;
import com.trentonfaris.zenith.graphics.texture.PixelType;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.graphics.texture.WrappingMode;
import com.trentonfaris.zenith.resource.resources.Models;
import com.trentonfaris.zenith.window.Window;

public final class RenderSystem extends BaseSystem {
	ComponentMapper<Camera> mCamera;
	ComponentMapper<Rect> mRect;
	ComponentMapper<ViewMatrix> mViewMatrix;
	ComponentMapper<ProjectionMatrix> mProjectionMatrix;

	CameraSystem cameraSystem;
	LightSystem lightSystem;

	/** The {@link Framebuffer} to which each camera frame is drawn. */
	private final Framebuffer cameraFb = new Framebuffer();

	/** The skybox {@link Mesh}. */
	private Mesh skybox;

	@Override
	public void initialize() {
		Window window = Zenith.getEngine().getWindow();
		Vector2i size = window.getSize();

		// The camera framebuffer has two attachments, a color buffer and a depth buffer
		cameraFb.addAttachment(Attachment.COLOR0, new Texture2D(InternalFormat.RGB32F, size.x, size.y, PixelFormat.RGB,
				PixelType.FLOAT, null, WrappingMode.REPEAT, FilteringMode.LINEAR));
		cameraFb.addAttachment(Attachment.DEPTH, new Texture2D(InternalFormat.DEPTH, size.x, size.y, PixelFormat.DEPTH,
				PixelType.FLOAT, null, WrappingMode.REPEAT, FilteringMode.NEAREST));

		this.skybox = Model.loadModel(Models.CUBE.getURI()).getMeshes().get(0);

		// We only use the position attribute of each vertex.
		for (Vertex vertex : skybox.getVertices()) {
			vertex.getAttributes().retainAll(vertex.getAttributes().subList(0, 1));
		}

		skybox.update();
		skybox.setMaterial(lightSystem.getSkyboxMaterial());
	}

	@Override
	protected void dispose() {
		cameraFb.dispose();
		skybox.dispose();
	}

	@Override
	protected void begin() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

		Vector4f clearColor = Zenith.getEngine().getGraphics().getClearColor();
		GL11.glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	protected void processSystem() {
		for (int cameraId : cameraSystem.getSorted()) {
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, cameraFb.getFbo());

			resizeCameraFb(cameraId);
			clearCameraFb(cameraId);

			ForwardRenderer.getInstance().render(world, cameraId);

			drawSkybox(cameraId);
			applyEffects(cameraId);

			blitCameraFb(cameraId);
		}
	}

	private void resizeCameraFb(int cameraId) {
		Window window = Zenith.getEngine().getWindow();
		Vector2i size = window.getSize();

		Rect rect = mRect.create(cameraId);

		int width = (int) (size.x * rect.width);
		int height = (int) (size.y * rect.height);

		Texture2D colorBuffer = (Texture2D) cameraFb.getRenderTargets().get(Attachment.COLOR0);
		if (colorBuffer.getWidth() != width || colorBuffer.getHeight() != height) {
			colorBuffer.setWidth(width);
			colorBuffer.setHeight(height);
			colorBuffer.update();

			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, Attachment.COLOR0.getValue(), GL11.GL_TEXTURE_2D,
					colorBuffer.getTbo(), 0);
		}

		Texture2D depthBuffer = (Texture2D) cameraFb.getRenderTargets().get(Attachment.DEPTH);
		if (depthBuffer.getWidth() != width || depthBuffer.getHeight() != height) {
			depthBuffer.setWidth(width);
			depthBuffer.setHeight(height);
			depthBuffer.update();

			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, Attachment.DEPTH.getValue(), GL11.GL_TEXTURE_2D,
					depthBuffer.getTbo(), 0);
		}

		GL11.glViewport(0, 0, width, height);
	}

	private void clearCameraFb(int cameraId) {
		Camera camera = mCamera.create(cameraId);

		GL11.glClearColor(camera.clearColor.x, camera.clearColor.y, camera.clearColor.z, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	private void drawSkybox(int cameraId) {
		Camera camera = mCamera.create(cameraId);

		if (camera.clearType != ClearType.SKYBOX) {
			return;
		}

		Material material = skybox.getMaterial();
		Shader shader = Zenith.getEngine().getGraphics().getShaderManager().getShader(material.getShaderType());
		shader.use();

		ViewMatrix viewMatrix = mViewMatrix.create(cameraId);
		ProjectionMatrix projectionMatrix = mProjectionMatrix.create(cameraId);

		Matrix4f viewProjectionMatrix = projectionMatrix.mat
				.mul(viewMatrix.mat.get3x3(new Matrix3f()).get(new Matrix4f()), new Matrix4f());

		Map<String, Uniform> uniforms = shader.getUniforms();
		for (Entry<String, Uniform> entry : uniforms.entrySet()) {
			Uniform uniform = entry.getValue();

			if (!(uniform instanceof Mat4Uniform)) {
				continue;
			}

			Mat4Uniform mat4Uniform = (Mat4Uniform) uniform;

			if (uniform.getUniformType() == UniformType.VIEW_PROJECTION_MATRIX) {
				mat4Uniform.set(viewProjectionMatrix);
			}
		}

		// Cache graphics settings
		Graphics graphics = Zenith.getEngine().getGraphics();

		boolean depthTesting = graphics.isDepthTesting();
		int depthFunc = graphics.getDepthFunc();
		int frontFace = graphics.getFrontFace();

		graphics.setDepthTesting(true);
		graphics.setDepthFunc(GL11.GL_LEQUAL);
		graphics.setFrontFace(GL11.GL_CW);
		graphics.update();

		skybox.draw();

		graphics.setDepthTesting(depthTesting);
		graphics.setDepthFunc(depthFunc);
		graphics.setFrontFace(frontFace);
		graphics.update();
	}

	private void applyEffects(int cameraId) {
		// Get the pp effects for this camera
		// Ping-pong between A & B FBOs as you apply them
	}

	private void blitCameraFb(int cameraId) {
		Window window = Zenith.getEngine().getWindow();
		Vector2i size = window.getSize();

		Rect rect = mRect.create(cameraId);

		int x = (int) (size.x * rect.x);
		int y = (int) (size.y * rect.y);
		int width = (int) (size.x * rect.width);
		int height = (int) (size.y * rect.height);

		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, cameraFb.getFbo());
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBlitFramebuffer(0, 0, width, height, x, y, x + width, y + height, GL11.GL_COLOR_BUFFER_BIT,
				GL11.GL_NEAREST);
	}
}
