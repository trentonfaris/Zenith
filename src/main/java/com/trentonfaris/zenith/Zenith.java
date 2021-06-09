package com.trentonfaris.zenith;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Zenith {
	private static final Logger LOGGER = LogManager.getLogger(Zenith.class);

	private static Engine engine;
	private static Thread thread;

	private Zenith() {
	}

	public static void start() {
		if (engine == null) {
			engine = new Engine();
		}

		if (engine.isRunning()) {
			return;
		}

		thread = new Thread(engine);
		thread.start();
	}

	public static void stop() {
		if (engine == null) {
			return;
		}

		engine.setRunning(false);

		new Thread(() -> {
			try {
				thread.join();
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
