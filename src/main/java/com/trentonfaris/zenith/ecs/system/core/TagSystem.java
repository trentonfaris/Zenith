package com.trentonfaris.zenith.ecs.system.core;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.EntitySubscription.SubscriptionListener;
import com.artemis.utils.IntBag;
import com.trentonfaris.zenith.Zenith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TagSystem extends BaseSystem {
	private final Map<String, Set<Integer>> entitiesByTag = new HashMap<>();
	private final Map<Integer, Set<String>> tagsByEntity = new HashMap<>();

	@Override
	protected void initialize() {
		world.getAspectSubscriptionManager().get(Aspect.all()).addSubscriptionListener(new SubscriptionListener() {
			@Override
			public void inserted(IntBag intBag) {
			}

			@Override
			public void removed(IntBag intBag) {
				deleted(intBag);
			}
		});
	}

	private void deleted(IntBag intBag) {
		for (int i : intBag.getData()) {
			removeEntity(i);
		}
	}

	@Override
	protected void processSystem() {
	}

	public void tagEntity(int entity, String tag) {
		if (tag == null || tag.isEmpty()) {
			String errorMsg = "Cannot tag an entity with a null or empty tag.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!entitiesByTag.containsKey(tag)) {
			entitiesByTag.put(tag, new HashSet<>());
		}

		entitiesByTag.get(tag).add(entity);

		if (!tagsByEntity.containsKey(entity)) {
			tagsByEntity.put(entity, new HashSet<>());
		}

		tagsByEntity.get(entity).add(tag);
	}

	public void untagEntity(int entity, String tag) {
		if (tag == null || tag.isEmpty()) {
			String errorMsg = "Cannot untag an entity with a null or empty tag.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!entitiesByTag.containsKey(tag) || !tagsByEntity.containsKey(entity)) {
			return;
		}

		entitiesByTag.get(tag).remove(entity);

		if (entitiesByTag.get(tag).isEmpty()) {
			entitiesByTag.remove(tag);
		}

		tagsByEntity.get(entity).remove(tag);

		if (tagsByEntity.get(entity).isEmpty()) {
			tagsByEntity.remove(entity);
		}
	}

	public void removeTag(String tag) {
		if (tag == null || tag.isEmpty()) {
			String errorMsg = "Cannot remove a null or empty tag.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!entitiesByTag.containsKey(tag)) {
			return;
		}

		Set<Integer> entities = entitiesByTag.get(tag);
		for (int entity : entities) {
			untagEntity(entity, tag);
		}
	}

	public void removeEntity(int entity) {
		if (!tagsByEntity.containsKey(entity)) {
			return;
		}

		Set<String> tags = tagsByEntity.get(entity);
		for (String tag : tags) {
			untagEntity(entity, tag);
		}
	}

	public Set<Integer> getEntities(String tag) {
		if (tag == null || tag.isEmpty()) {
			String errorMsg = "Cannot get entities from a null or empty tag.";
			Zenith.getLogger().error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		if (!entitiesByTag.containsKey(tag)) {
			return new HashSet<>();
		}

		return entitiesByTag.get(tag);
	}

	public Set<String> getTags(int entity) {
		if (!tagsByEntity.containsKey(entity)) {
			return new HashSet<>();
		}

		return tagsByEntity.get(entity);
	}
}
