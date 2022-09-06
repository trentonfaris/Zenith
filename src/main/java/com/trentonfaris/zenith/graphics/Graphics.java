package com.trentonfaris.zenith.graphics;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.shader.ShaderManager;
import com.trentonfaris.zenith.utility.Disposable;
import org.apache.logging.log4j.Level;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * The {@link Graphics} class is responsible for managing {@link GL} properties.
 *
 * @author Trenton Faris
 */
public final class Graphics implements Disposable {
    /**
     * A flag which indicates if depth testing is enabled.
     */
    private boolean depthTesting = true;

    /**
     * A flag which masks the writing of depth values to the depth buffer.
     */
    private boolean depthMasking = true;

    /**
     * The type of comparison performed during depth testing.
     */
    private int depthFunc = GL11.GL_LEQUAL;

    /**
     * A flag which indicates if blending is enabled.
     */
    private boolean blending = true;

    /**
     * The type of weighting factor to use on the source during blending.
     */
    private int blendSrc = GL11.GL_SRC_ALPHA;

    /**
     * The type of weighting factor to use on the destination during blending.
     */
    private int blendDst = GL11.GL_ONE_MINUS_SRC_ALPHA;

    /**
     * A flag which indicates if culling is enabled.
     */
    private boolean culling = true;

    /**
     * The polygon face to be culled during culling.
     */
    private int cullFace = GL11.GL_BACK;

    /**
     * The winding order of a polygon face.
     */
    private int frontFace = GL11.GL_CCW;

    /**
     * A flag which indicates if anti-aliasing is enabled.
     */
    private boolean antiAliasing = true;

    /**
     * The color to use when clearing the color buffer.
     */
    private Vector4f clearColor = new Vector4f(1, 0, 1, 1);

    /**
     * A flag which indicates if {@link GL} properties have changed since the last
     * {@link #update}.
     */
    private boolean dirty;

    /**
     * The {@link ShaderManager} manages shaders.
     */
    private ShaderManager shaderManager = new ShaderManager();

    public void init() {
        Zenith.getLogger().log(Level.INFO, "Graphics initializing...");

        // TODO : This is problematic. It assumes Window has been initialized with no checks to verify.
        GLFW.glfwMakeContextCurrent(Zenith.getEngine().getWindow().getHandle());

        GLFW.glfwSwapInterval(0);

        GL.createCapabilities();

        setProps();

        if (this.shaderManager != null) {
            shaderManager.dispose();
        }

        this.shaderManager = new ShaderManager();
    }

    /**
     * Sets the graphics properties.
     */
    private void setProps() {
        if (depthTesting) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        GL11.glDepthMask(depthMasking);
        GL11.glDepthFunc(depthFunc);

        if (blending) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glBlendFunc(blendSrc, blendDst);

        if (culling) {
            GL11.glEnable(GL11.GL_CULL_FACE);
        } else {
            GL11.glDisable(GL11.GL_CULL_FACE);
        }

        GL11.glCullFace(cullFace);
        GL11.glFrontFace(frontFace);

        if (antiAliasing) {
            GL11.glEnable(GL13.GL_MULTISAMPLE);
        } else {
            GL11.glDisable(GL13.GL_MULTISAMPLE);
        }

        this.dirty = false;
    }

    public void update() {
        if (dirty) {
            setProps();
        }
    }

    public void dispose() {
        shaderManager.dispose();
    }

    /**
     * Gets the flag indicating if {@link #depthTesting} will be performed.
     *
     * @return The {@link #depthTesting} flag.
     */
    public boolean isDepthTesting() {
        return depthTesting;
    }

    /**
     * Sets the flag indicating if {@link #depthTesting} will be performed.
     */
    public void setDepthTesting(boolean depthTesting) {
        this.depthTesting = depthTesting;
        this.dirty = true;
    }

    /**
     * Gets the flag indicating if {@link #depthMasking} will be performed.
     *
     * @return The {@link #depthMasking} flag.
     */
    public boolean isDepthMasking() {
        return depthMasking;
    }

    /**
     * Sets the flag indicating if {@link #depthMasking} will be performed.
     */
    public void setDepthMasking(boolean depthMasking) {
        this.depthMasking = depthMasking;
        this.dirty = true;
    }

    /**
     * Gets the type of depth testing to perform.
     *
     * @return The {@link #depthFunc} value.
     */
    public int getDepthFunc() {
        return depthFunc;
    }

    /**
     * Sets the type of depth testing to perform.
     */
    public void setDepthFunc(int depthFunc) {
        this.depthFunc = depthFunc;
        this.dirty = true;
    }

    /**
     * Gets the flag indicating if {@link #blending} will be performed.
     *
     * @return The {@link #blending} flag.
     */
    public boolean isBlending() {
        return blending;
    }

    /**
     * Sets the flag indicating if {@link #blending} will be performed.
     */
    public void setBlending(boolean blending) {
        this.blending = blending;
        this.dirty = true;
    }

    /**
     * Gets the type of blending to perform on the source buffer.
     *
     * @return The {@link #blendSrc} value.
     */
    public int getBlendSrc() {
        return blendSrc;
    }

    /**
     * Sets the type of blending to perform on the source buffer.
     */
    public void setBlendSrc(int blendSrc) {
        this.blendSrc = blendSrc;
        this.dirty = true;
    }

    /**
     * Gets the type of blending to perform on the destination buffer.
     *
     * @return The {@link #blendDst} value.
     */
    public int getBlendDst() {
        return blendDst;
    }

    /**
     * Sets the type of blending to perform on the destination buffer.
     */
    public void setBlendDst(int blendDst) {
        this.blendDst = blendDst;
        this.dirty = true;
    }

    /**
     * Gets the flag indicating if {@link #culling} will be performed.
     *
     * @return The {@link #culling} flag.
     */
    public boolean isCulling() {
        return culling;
    }

    /**
     * Sets the flag indicating if {@link #culling} will be performed.
     */
    public void setCulling(boolean culling) {
        this.culling = culling;
        this.dirty = true;
    }

    /**
     * Gets the face to cull.
     *
     * @return The {@link #cullFace} value.
     */
    public int getCullFace() {
        return cullFace;
    }

    /**
     * Sets the face to cull.
     */
    public void setCullFace(int cullFace) {
        this.cullFace = cullFace;
        this.dirty = true;
    }

    /**
     * Gets the winding order of a face.
     *
     * @return The {@link #frontFace} value.
     */
    public int getFrontFace() {
        return frontFace;
    }

    /**
     * Sets the winding order a face.
     */
    public void setFrontFace(int frontFace) {
        this.frontFace = frontFace;
        this.dirty = true;
    }

    /**
     * Gets the flag indicating if {@link #antiAliasing} will be performed.
     *
     * @return The {@link #antiAliasing} flag.
     */
    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    /**
     * Sets the flag indicating if {@link #antiAliasing} will be performed.
     */
    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
        this.dirty = true;
    }

    /**
     * Gets the color to use when clearing the color buffer.
     *
     * @return The {@link #clearColor} value.
     */
    public Vector4f getClearColor() {
        return clearColor;
    }

    /**
     * Sets the color to use when clearing the color buffer.
     *
     * @param clearColor
     */
    public void setClearColor(Vector4f clearColor) {
        this.clearColor = clearColor;
    }

    /**
     * Gets the {@link ShaderManager}.
     *
     * @return The {@link ShaderManager}.
     */
    public ShaderManager getShaderManager() {
        return shaderManager;
    }
}
