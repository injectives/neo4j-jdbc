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
package org.neo4j.driver.jdbc.internal.bolt.internal.response;

import java.util.stream.IntStream;

import org.neo4j.driver.jdbc.internal.bolt.response.SummaryCounters;

public record InternalSummaryCounters(int nodesCreated, int nodesDeleted, int relationshipsCreated,
		int relationshipsDeleted, int propertiesSet, int labelsAdded, int labelsRemoved, int indexesAdded,
		int indexesRemoved, int constraintsAdded, int constraintsRemoved,
		int systemUpdates) implements SummaryCounters {
	@Override
	public int totalCount() {
		return IntStream
			.of(this.nodesCreated, this.nodesDeleted, this.relationshipsCreated, this.relationshipsDeleted,
					this.propertiesSet, this.labelsAdded, this.labelsRemoved, this.indexesAdded, this.indexesRemoved,
					this.constraintsAdded, this.constraintsRemoved)
			.sum();
	}

	@Override
	public boolean containsUpdates() {
		return IntStream
			.of(this.nodesCreated, this.nodesDeleted, this.relationshipsCreated, this.relationshipsDeleted,
					this.propertiesSet, this.labelsAdded, this.labelsRemoved, this.indexesAdded, this.indexesRemoved,
					this.constraintsAdded, this.constraintsRemoved)
			.anyMatch(this::isPositive);
	}

	@Override
	public boolean containsSystemUpdates() {
		return isPositive(this.systemUpdates);
	}

	private boolean isPositive(int value) {
		return value > 0;
	}
}
