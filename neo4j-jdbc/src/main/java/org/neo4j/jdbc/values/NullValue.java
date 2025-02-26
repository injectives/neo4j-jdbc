/*
 * Copyright (c) 2023-2025 "Neo4j,"
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
package org.neo4j.jdbc.values;

/**
 * Representation of the Cypher {@code NULL} value.
 *
 * @author Neo4j Drivers Team
 * @since 6.0.0
 */
public final class NullValue extends AbstractValue {

	/**
	 * A {@link BooleanValue} representing null value.
	 */
	public static final Value NULL = new NullValue();

	private NullValue() {
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public Object asObject() {
		return null;
	}

	@Override
	public String asString() {
		return "null";
	}

	@Override
	public Type type() {
		return Type.NULL;
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) {
		return obj == NULL;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "NULL";
	}

}
