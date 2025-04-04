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
package org.neo4j.jdbc;

import java.io.Serial;
import java.sql.SQLException;

/**
 * A wrapper around {@link SQLException} modelled after
 * {@link java.io.UncheckedIOException}.
 *
 * @author Michael J. Simons
 */
final class UncheckedSQLException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 2069293673307885144L;

	UncheckedSQLException(SQLException cause) {
		super(cause);
	}

	@Override
	public synchronized SQLException getCause() {
		return (SQLException) super.getCause();
	}

}
