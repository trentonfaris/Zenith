package com.trentonfaris.zenith.demo;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.demo.plugin.DemoPlugin;

public class DemoApp {

    public static void main(String[] args) {
        Zenith.start();

        // This doesn't work because this thread doesn't have a current GL context
//        Zenith.getEngine().getSceneManager().loadScene(DemoPlugin.class);
    }
}
