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
package org.neo4j.driver.jdbc.internal.bolt.internal.value;

import org.neo4j.driver.jdbc.internal.bolt.exception.LossyCoercion;
import org.neo4j.driver.jdbc.internal.bolt.internal.types.InternalTypeSystem;
import org.neo4j.driver.jdbc.internal.bolt.types.Type;

public final class IntegerValue extends NumberValueAdapter<Long> {

	private final long val;

	public IntegerValue(long val) {
		this.val = val;
	}

	@Override
	public Type type() {
		return InternalTypeSystem.TYPE_SYSTEM.INTEGER();
	}

	@Override
	public Long asNumber() {
		return this.val;
	}

	@Override
	public long asLong() {
		return this.val;
	}

	@Override
	public int asInt() {
		if (this.val > Integer.MAX_VALUE || this.val < Integer.MIN_VALUE) {
			throw new LossyCoercion(type().name(), "Java int");
		}
		return (int) this.val;
	}

	@Override
	public double asDouble() {
		var doubleVal = (double) this.val;
		if ((long) doubleVal != this.val) {
			throw new LossyCoercion(type().name(), "Java double");
		}

		return (double) this.val;
	}

	@Override
	public float asFloat() {
		return (float) this.val;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		var values = (IntegerValue) o;
		return this.val == values.val;
	}

	@Override
	public int hashCode() {
		return (int) (this.val ^ (this.val >>> 32));
	}

	@Override
	public String toString() {
		return Long.toString(this.val);
	}

}
