/*
 * Copyright (c) 2023 "Neo4j,"
 * Neo4j Sweden AB [https://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.driver.jdbc.internal.bolt.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.neo4j.driver.jdbc.internal.bolt.Value;
import org.neo4j.driver.jdbc.internal.bolt.internal.value.NodeValue;
import org.neo4j.driver.jdbc.internal.bolt.types.Node;

public final class InternalNode extends InternalEntity implements Node {

	private final Collection<String> labels;

	public InternalNode(long id) {
		this(id, Collections.emptyList(), Collections.emptyMap());
	}

	public InternalNode(long id, Collection<String> labels, Map<String, Value> properties) {
		this(id, String.valueOf(id), labels, properties);
	}

	public InternalNode(long id, String elementId, Collection<String> labels, Map<String, Value> properties) {
		super(id, elementId, properties);
		this.labels = labels;
	}

	@Override
	public Collection<String> labels() {
		return this.labels;
	}

	@Override
	public boolean hasLabel(String label) {
		return this.labels.contains(label);
	}

	@Override
	public Value asValue() {
		return new NodeValue(this);
	}

	@Override
	@SuppressWarnings("deprecation")
	public String toString() {
		return String.format("node<%s>", id());
	}

}