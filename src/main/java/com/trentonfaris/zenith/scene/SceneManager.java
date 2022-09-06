package com.trentonfaris.zenith.scene;

import com.artemis.ArtemisPlugin;
import com.trentonfaris.zenith.Zenith;

public final class SceneManager {
    private Scene scene;

    private final Object lock = new Object();

    public void update() {
        synchronized (lock) {

            if (scene != null) {
                scene.update();
            }
        }
    }

    public <T extends ArtemisPlugin> void loadScene(Class<T> artemisPluginType) {
        synchronized (lock) {
            if (artemisPluginType == null) {
                String errorMsg = "Cannot load a scene from a null artemisPluginType.";
                Zenith.getLogger().error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            unloadScene();

            this.scene = new Scene(artemisPluginType);

            scene.load();
        }
    }

    public void unloadScene() {
        synchronized (lock) {
            if (scene != null) {
                scene.unload();

                this.scene = null;
            }
        }
    }

    public Scene getScene() {
        synchronized (lock) {
            return scene;
        }
    }
}
