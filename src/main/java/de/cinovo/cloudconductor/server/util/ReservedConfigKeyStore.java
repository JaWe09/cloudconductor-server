package de.cinovo.cloudconductor.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;

import de.cinovo.cloudconductor.api.ICCReservedConfigNames;

/**
 * Copyright 2014 Cinovo AG<br>
 * <br>
 *
 * @author psigloch
 *
 */
public class ReservedConfigKeyStore {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservedConfigKeyStore.class);
	
	/**
	 * the ReservedConfigKeyStore singleton
	 */
	public static ReservedConfigKeyStore instance = new ReservedConfigKeyStore();

	private Map<String, String> reserved = new HashMap<>();


	private ReservedConfigKeyStore() {
		this.initReserverdKeys();
		this.initPropertyValues();
	}
	
	private void initReserverdKeys() {
		Field[] declaredFields = ICCReservedConfigNames.class.getDeclaredFields();
		for (Field field : declaredFields) {
			if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(String.class)) {
				try {
					this.reserved.put((String) field.get(null), null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					ReservedConfigKeyStore.LOGGER.warn("Failed to reserve key for " + field.getName());
				}
			}
		}
	}

	private void initPropertyValues() {
		this.registerReserverdKey(ICCReservedConfigNames.C2_SERVER_NAME, (String) System.getProperties().get(ICCProperties.CC_NAME));
	}
	
	/**
	 * @param key the new key to register
	 * @param value the value of the key
	 */
	public void registerReserverdKey(String key, String value) {
		if (this.reserved.get(key) == null) {
			this.reserved.put(key, value);
		}
	}
	
	/**
	 * @param key the key to check
	 * @return existing or not
	 */
	public boolean isReserved(String key) {
		return this.reserved.containsKey(key);
	}
	
	/**
	 * @return set of reserved keys
	 */
	public Set<String> getReservedKeys() {
		return ImmutableSet.copyOf(this.reserved.keySet());
	}
	
	/**
	 * @param key the key
	 * @return the value
	 */
	public String getValue(String key) {
		return this.reserved.get(key);
	}

	/**
	 * @return the map containing keys and values
	 */
	public Map<String, String> getReservedKeysWithValues() {
		Builder<String, String> result = ImmutableMap.builder();
		for (Entry<String, String> entry : this.reserved.entrySet()) {
			if ((entry.getValue() != null) && !entry.getValue().isEmpty()) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result.build();
	}
}
