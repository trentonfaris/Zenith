package com.trentonfaris.zenith;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Zenith {
	private static final Logger LOGGER = LogManager.getLogger(Zenith.class);

	private static Engine engine;
	private static Thread engineThread;

	private Zenith() {
	}

	public static void start() {
		LOGGER.log(Level.INFO, "Zenith starting...");

		if (engine != null) {
			throw new IllegalStateException("Cannot call Zenith.start() if the Zenith engine is already running.");
		}

		engine = new Engine();

		engineThread = new Thread(engine);
		engineThread.setName("engineThread");
		engineThread.start();

		while (!engine.isRunning()) {
			// Block until Engine has initialized
		}

		LOGGER.log(Level.INFO, "Zenith started.");
	}

	public static void stop() {
		if (engine == null) {
			throw new IllegalStateException("Cannot call Zenith.stop() if the Zenith engine is not running.");
		}

		engine.setRunning(false);

		new Thread(() -> {
			try {
				engineThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			engine = null;
		}).start();
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public static Engine getEngine() {
		return engine;
	}
}
