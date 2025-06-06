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
package org.neo4j.jdbc.internal.bolt;

import org.neo4j.bolt.connection.SummaryCounters;

record SummaryCountersImpl(int nodesCreated, int nodesDeleted, int relationshipsCreated, int relationshipsDeleted,
		int propertiesSet, int labelsAdded, int labelsRemoved, int indexesAdded, int indexesRemoved,
		int constraintsAdded, int constraintsRemoved, int systemUpdates) implements SummaryCounters {

	public static final SummaryCountersImpl EMPTY_STATS = new SummaryCountersImpl(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

	@Override
	public int totalCount() {

		int sum = this.nodesCreated;
		sum += this.nodesDeleted;
		sum += this.relationshipsCreated;
		sum += this.relationshipsDeleted;
		sum += this.propertiesSet;
		sum += this.labelsAdded;
		sum += this.labelsRemoved;
		sum += this.indexesAdded;
		sum += this.indexesRemoved;
		sum += this.constraintsAdded;
		sum += this.constraintsRemoved;
		return sum;
	}

	@Override
	public boolean containsUpdates() {
		return isPositive(this.totalCount());
	}

	@Override
	public boolean containsSystemUpdates() {
		return isPositive(this.systemUpdates);
	}

	private static boolean isPositive(int value) {
		return value > 0;
	}
}
