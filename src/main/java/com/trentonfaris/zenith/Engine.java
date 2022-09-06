package com.trentonfaris.zenith;

import com.trentonfaris.zenith.demo.plugin.DemoPlugin;
import com.trentonfaris.zenith.graphics.Graphics;
import com.trentonfaris.zenith.graphics.shader.Shader;
import com.trentonfaris.zenith.input.Input;
import com.trentonfaris.zenith.resource.ImageLoader;
import com.trentonfaris.zenith.resource.ModelLoader;
import com.trentonfaris.zenith.resource.ResourceManager;
import com.trentonfaris.zenith.resource.ShaderLoader;
import com.trentonfaris.zenith.scene.SceneManager;
import com.trentonfaris.zenith.scheduler.Scheduler;
import com.trentonfaris.zenith.window.Window;
import org.apache.logging.log4j.Level;

public final class Engine implements Runnable {
    private static final int TICKS_PER_SEC = 60;

    private Timer timer;
    private final Graphics graphics = new Graphics();
    private final Input input = new Input();
    private final Scheduler scheduler = new Scheduler();
    private final Window window = new Window();

    private final ResourceManager resourceManager = new ResourceManager();
    private final SceneManager sceneManager = new SceneManager();

    private volatile boolean running;
    private double accumulator = 0.0;

    Engine() {
    }

    @Override
    public void run() {
        init();
        loop();
		dispose();
    }

    private void init() {
        Zenith.getLogger().log(Level.INFO, "Engine initializing...");

        this.timer = Timer.getInstance();

        window.init();
        input.init();
        graphics.init();

        resourceManager.registerResourceLoader(ImageLoader.class);
        resourceManager.registerResourceLoader(ModelLoader.class);
        resourceManager.registerResourceLoader(ShaderLoader.class);

        this.running = true;

        Zenith.getEngine().getSceneManager().loadScene(DemoPlugin.class);
    }

    private void loop() {
        while (running) {
            timer.update();

            this.accumulator += timer.getDeltaTime();

            while (accumulator >= timer.getFixedTimeStep()) {
                timer.fixedUpdate();
                // Do fixed time step updates here
                this.accumulator -= timer.getFixedTimeStep();
            }

            input.update();

            scheduler.update();
            sceneManager.update();

            window.update();
            graphics.update();
        }
    }

    private void dispose() {
        sceneManager.unloadScene();

        resourceManager.removeResourceLoader(ShaderLoader.class);
        resourceManager.removeResourceLoader(ModelLoader.class);
        resourceManager.removeResourceLoader(ImageLoader.class);

        // TODO : Implement this function
        resourceManager.dispose();

        graphics.dispose();
        window.dispose();
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Input getInput() {
        return input;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public Timer getTimer() {
        return timer;
    }

    public Window getWindow() {
        return window;
    }

    public boolean isRunning() {
        return running;
    }

    void setRunning(boolean running) {
        this.running = running;
    }
}
