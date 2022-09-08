package com.trentonfaris.zenith.window;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.utility.Disposable;
import org.apache.logging.log4j.Level;
import org.joml.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public final class Window implements Disposable {
    private long handle;

    public void init() {
        Zenith.getLogger().log(Level.INFO, "Window initializing...");

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to init GLFW.");
        }

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (vidMode == null) {
            String errorMsg = "Could not find the GLFW primary monitor.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        int width = vidMode.width();
        int height = vidMode.height();

        // TODO : Move initial window sizes to configuration
        this.handle = GLFW.glfwCreateWindow(1920, 1080, "Zenith", /*GLFW.glfwGetPrimaryMonitor()*/MemoryUtil.NULL, MemoryUtil.NULL);
        if (handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window.");
        }

        GLFW.glfwSetWindowPos(handle, 100, 100);

        GLFW.glfwSetWindowCloseCallback(handle, (window) -> Zenith.stop());
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
    }

    @Override
    public void dispose() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public long getHandle() {
        return handle;
    }

    public Vector2i getSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(handle, pWidth, pHeight);

            return new Vector2i(pWidth.get(), pHeight.get());
        }
    }
}
