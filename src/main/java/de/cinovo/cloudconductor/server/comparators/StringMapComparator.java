package de.cinovo.cloudconductor.server.comparators;

/*
 * #%L
 * cloudconductor-server
 * %%
 * Copyright (C) 2013 - 2014 Cinovo AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * #L%
 */

import java.util.Comparator;
import java.util.Map;

/**
 * Copyright 2013 Cinovo AG<br>
 * <br>
 *
 * @author psigloch
 *
 */
public class StringMapComparator implements Comparator<Map<String, Object>> {

	private String key;


	/**
	 * @param key the key
	 */
	public StringMapComparator(String key) {
		this.key = key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		if ((o1.get(this.key) == null) || (o2.get(this.key) == null)) {
			return -1;
		}
		if ((o1.get(this.key) instanceof String) && (o2.get(this.key) instanceof String)) {
			return ((String) o1.get(this.key)).compareToIgnoreCase(((String) o2.get(this.key)));
		}
		if ((o1.get(this.key) instanceof Comparable<?>) && (o2.get(this.key) instanceof Comparable<?>)) {
			return ((Comparable<Object>) o1.get(this.key)).compareTo((o2.get(this.key)));
		}
		return -1;
	}
}
