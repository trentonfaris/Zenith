package com.trentonfaris.zenith.ecs.system.core;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.ecs.Layers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LayerSystem extends BaseSystem {
	private static final AtomicInteger NEXT_LAYER_ID = new AtomicInteger(0);

	private final Map<Integer, Layer> layersById = new HashMap<>();
	private final Map<String, Layer> layersByName = new HashMap<>();
	private final Map<Layer, Set<Integer>> entitiesByLayer = new HashMap<>();
	private final Map<Integer, Set<Layer>> layersByEntity = new HashMap<>();

	@Override
	protected void initialize() {
		Layer layer = new Layer(NEXT_LAYER_ID.getAndIncrement(), Layers.DEFAULT.toString());

		layersById.put(layer.getId(), layer);
		layersByName.put(layer.getName(), layer);
		entitiesByLayer.put(layer, new HashSet<>());

		world.getAspectSubscriptionManager().get(Aspect.all())
				.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
					@Override
					public void inserted(IntBag intBag) {
						added(intBag);
					}

					@Override
					public void removed(IntBag intBag) {
						deleted(intBag);
					}
				});
	}

	private void added(IntBag intBag) {
		Layer layer = layersByName.get(Layers.DEFAULT.toString());

		for (int i : intBag.getData()) {
			addEntityToLayer(i, layer);
		}
	}

	private void deleted(IntBag intBag) {
		for (int i : intBag.getData()) {
			removeEntity(i);
		}
	}

	@Override
	protected void processSystem() {
	}

	public void addEntityToLayer(int entity, String layerName) {
		if (layerName == null || layerName.isEmpty()) {
			String errorMsg = "Cannot add an entity to a layer with a null or empty layerName.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!layersByName.containsKey(layerName)) {
			Layer layer = new Layer(NEXT_LAYER_ID.getAndIncrement(), layerName);

			layersById.put(layer.getId(), layer);
			layersByName.put(layer.getName(), layer);
		}

		addEntityToLayer(entity, layersByName.get(layerName));
	}

	private void addEntityToLayer(int entity, Layer layer) {
		if (!entitiesByLayer.containsKey(layer)) {
			entitiesByLayer.put(layer, new HashSet<>());
		}

		entitiesByLayer.get(layer).add(entity);

		if (!layersByEntity.containsKey(entity)) {
			layersByEntity.put(entity, new HashSet<>());
		}

		layersByEntity.get(entity).add(layer);
	}

	public void removeEntityFromLayer(int entity, String layerName) {
		if (layerName == null || layerName.isEmpty()) {
			String errorMsg = "Cannot remove an entity from a layer with a null or empty layerName.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!layersByName.containsKey(layerName)) {
			return;
		}

		removeEntityFromLayer(entity, layersByName.get(layerName));
	}

	private void removeEntityFromLayer(int entity, Layer layer) {
		if (!entitiesByLayer.containsKey(layer) || !layersByEntity.containsKey(entity)) {
			return;
		}

		entitiesByLayer.get(layer).remove(entity);

		if (entitiesByLayer.get(layer).isEmpty() && !layer.getName().equals(Layers.DEFAULT.toString())) {
			layersById.remove(layer.getId());
			layersByName.remove(layer.getName());
			entitiesByLayer.remove(layer);
		}

		layersByEntity.get(entity).remove(layer);

		if (layersByEntity.get(entity).isEmpty()) {
			layersByEntity.remove(entity);
		}
	}

	public void removeLayer(int layerId) {
		if (!layersById.containsKey(layerId)) {
			return;
		}

		Layer layer = layersById.get(layerId);

		if (layer.getName().equals(Layers.DEFAULT.toString())) {
			Zenith.getLogger().warn("Cannot remove the default layer.");
			return;
		}

		Set<Integer> entities = entitiesByLayer.get(layer);
		for (int entity : entities) {
			removeEntityFromLayer(entity, layer);
		}
	}

	public void removeLayer(String layerName) {
		if (layerName == null || layerName.isEmpty()) {
			String errorMsg = "Cannot remove a layer from a null or empty layerName.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!layersByName.containsKey(layerName)) {
			return;
		}

		Layer layer = layersByName.get(layerName);

		if (layer.getName().equals(Layers.DEFAULT.toString())) {
			Zenith.getLogger().warn("Cannot remove the default layer.");
			return;
		}

		Set<Integer> entities = entitiesByLayer.get(layer);
		for (int entity : entities) {
			removeEntityFromLayer(entity, layer);
		}
	}

	public void removeEntity(int entity) {
		if (!layersByEntity.containsKey(entity)) {
			return;
		}

		Set<Layer> layers = layersByEntity.get(entity);
		for (Layer layer : layers) {
			removeEntityFromLayer(entity, layer);
		}
	}

	public Layer getLayer(int layerId) {
		return layersById.get(layerId);
	}

	public Layer getLayer(String layerName) {
		if (layerName == null || layerName.isEmpty()) {
			String errorMsg = "Cannot get a layer from a null or empty layerName.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		return layersByName.get(layerName);
	}

	public Set<Integer> getEntities(int layerId) {
		if (!layersById.containsKey(layerId)) {
			return new HashSet<>();
		}

		return entitiesByLayer.get(layersById.get(layerId));
	}

	public Set<Integer> getEntities(String layerName) {
		if (layerName == null || layerName.isEmpty()) {
			String errorMsg = "Cannot get entities from a null or empty layerName.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!layersByName.containsKey(layerName)) {
			return new HashSet<>();
		}

		return entitiesByLayer.get(layersByName.get(layerName));
	}

	public Set<Layer> getLayers(Integer entity) {
		if (entity == null) {
			String errorMsg = "Cannot get layers from a null entity.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		return layersByEntity.get(entity);
	}

	public final class Layer {

		private final int id;
		private final String name;

		private Layer(int id, String name) {
			if (name == null || name.isEmpty()) {
				String errorMsg = "Cannot create a Layer from a null or empty name.";
				Zenith.getLogger().error(errorMsg);
				throw new IllegalArgumentException(errorMsg);
			}

			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Layer layer = (Layer) o;

			if (id != layer.id) {
				return false;
			}
			return name.equals(layer.name);
		}

		@Override
		public int hashCode() {
			int result = id;
			result = 31 * result + name.hashCode();
			return result;
		}
	}
}
