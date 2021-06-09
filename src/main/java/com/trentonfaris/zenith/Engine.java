package com.trentonfaris.zenith;

import com.trentonfaris.zenith.demo.plugin.DemoPlugin;
import com.trentonfaris.zenith.graphics.Graphics;
import com.trentonfaris.zenith.input.Input;
import com.trentonfaris.zenith.resource.ImageLoader;
import com.trentonfaris.zenith.resource.ModelLoader;
import com.trentonfaris.zenith.resource.ResourceManager;
import com.trentonfaris.zenith.resource.ShaderLoader;
import com.trentonfaris.zenith.scene.SceneManager;
import com.trentonfaris.zenith.time.Time;
import com.trentonfaris.zenith.window.Window;

public final class Engine implements Runnable {
	private final Graphics graphics = new Graphics();
	private final Input input = new Input();
	private final Time time = new Time();
	private final Window window = new Window();

	private final ResourceManager resourceManager = new ResourceManager();
	private final SceneManager sceneManager = new SceneManager();

	private volatile boolean running;

	Engine() {
	}

	@Override
	public void run() {
		init();
		loop();
		dispose();
	}

	private void init() {
		time.init();
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
			time.update();
			window.update();
			input.update();
			graphics.update();
			sceneManager.update();
		}
	}

	private void dispose() {
		sceneManager.unloadScene();
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

	public Time getTime() {
		return time;
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
