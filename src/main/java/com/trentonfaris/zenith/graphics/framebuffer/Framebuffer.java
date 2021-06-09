package com.trentonfaris.zenith.graphics.framebuffer;

import java.nio.IntBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.texture.Texture2D;
import com.trentonfaris.zenith.utility.Disposable;

/**
 * A {@link Framebuffer} is an off-screen combination of color, depth, and
 * stencil buffers that can be drawn to.
 *
 * @author Trenton Faris
 */
public final class Framebuffer implements Disposable {
	/** The render targets attached to this {@link Framebuffer}. */
	private final Map<Attachment, RenderTarget> renderTargets = new HashMap<>();

	/** The framebuffer object handle. */
	private final int fbo;

	/** Creates a new {@link Framebuffer}. */
	public Framebuffer() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pFbo = stack.mallocInt(1);
			GL30.glGenFramebuffers(pFbo);

			this.fbo = pFbo.get();
		}
	}

	@Override
	public void dispose() {
		GL30.glDeleteFramebuffers(fbo);

		for (Entry<Attachment, RenderTarget> entry : renderTargets.entrySet()) {
			RenderTarget renderTarget = entry.getValue();

			if (renderTarget instanceof Texture2D) {
				((Texture2D) renderTarget).dispose();
			} else if (renderTarget instanceof Renderbuffer) {
				((Renderbuffer) renderTarget).dispose();
			}
		}
	}

	/**
	 * Adds an {@link Attachment} to this {@link Framebuffer}. Removes and disposes
	 * of the previous {@link Attachment}, if one exists.
	 * 
	 * @param attachment
	 * @param renderTarget
	 */
	public void addAttachment(Attachment attachment, RenderTarget renderTarget) {
		if (attachment == null) {
			String errorMsg = "Cannot attach a renderTarget to a framebuffer with a null attachment.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (renderTarget == null) {
			String errorMsg = "Cannot attach a null renderTarget to a framebuffer.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!(renderTarget instanceof Texture2D) && !(renderTarget instanceof Renderbuffer)) {
			String errorMsg = "Cannot attach an unsupported renderTarget to a framebuffer.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		RenderTarget prevAttachable = renderTargets.put(attachment, renderTarget);

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

		if (renderTarget instanceof Texture2D) {
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.getValue(), GL11.GL_TEXTURE_2D,
					((Texture2D) renderTarget).getTbo(), 0);
		} else if (renderTarget instanceof Renderbuffer) {
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment.getValue(), GL30.GL_RENDERBUFFER,
					((Renderbuffer) renderTarget).getRbo());
		}

		if (prevAttachable instanceof Texture2D) {
			((Texture2D) prevAttachable).dispose();
		} else if (prevAttachable instanceof Renderbuffer) {
			((Renderbuffer) prevAttachable).dispose();
		}
	}

	/**
	 * Removes and disposes of an {@link Attachment} from this {@link Framebuffer}.
	 * 
	 * @param attachment
	 */
	public void removeAttachment(Attachment attachment) {
		if (attachment == null) {
			String errorMsg = "Cannot remove a texture or renderbuffer from a framebuffer from a null attachment.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!renderTargets.containsKey(attachment)) {
			return;
		}

		RenderTarget renderTarget = renderTargets.remove(attachment);

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

		if (renderTarget instanceof Texture2D) {
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.getValue(), GL11.GL_TEXTURE_2D, 0, 0);
			((Texture2D) renderTarget).dispose();
		} else if (renderTarget instanceof Renderbuffer) {
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment.getValue(), GL30.GL_RENDERBUFFER, 0);
			((Renderbuffer) renderTarget).dispose();
		}

	}

	/**
	 * Gets an unmodifiable map of the {@link #renderTargets}.
	 * 
	 * @return An unmodifiable map of the {@link #renderTargets}.
	 */
	public Map<Attachment, RenderTarget> getRenderTargets() {
		return Collections.unmodifiableMap(renderTargets);
	}

	/**
	 * Gets the framebuffer object handle.
	 *
	 * @return The {@link #fbo} value.
	 */
	public int getFbo() {
		return fbo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((renderTargets == null) ? 0 : renderTargets.hashCode());
		result = prime * result + fbo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Framebuffer other = (Framebuffer) obj;
		if (renderTargets == null) {
			if (other.renderTargets != null)
				return false;
		} else if (!renderTargets.equals(other.renderTargets))
			return false;
		if (fbo != other.fbo)
			return false;
		return true;
	}
}
