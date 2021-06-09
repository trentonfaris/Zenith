package com.trentonfaris.zenith.ecs;

public enum Layers {
	DEFAULT(0, "Default");

	private final int id;
	private final String name;

	Layers(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
