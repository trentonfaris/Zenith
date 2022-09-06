package com.trentonfaris.zenith.demo;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.demo.plugin.DemoPlugin;

public class DemoApp {

    public static void main(String[] args) {
        Zenith.start();

        // TODO : Need to protect engine access from the mainThread
//        Zenith.getEngine().getSceneManager().loadScene(DemoPlugin.class);
    }
}
