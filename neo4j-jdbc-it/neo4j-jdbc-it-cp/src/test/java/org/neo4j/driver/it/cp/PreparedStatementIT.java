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
package org.neo4j.driver.it.cp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PreparedStatementIT {

	@SuppressWarnings("resource") // On purpose to reuse this
	protected final Neo4jContainer<?> neo4j = new Neo4jContainer<>(System.getProperty("neo4j-jdbc.default-neo4j-image"))
		.withEnv("NEO4J_ACCEPT_LICENSE_AGREEMENT", "yes");

	@BeforeAll
	void startNeo4j() {
		this.neo4j.start();
	}

	@Test
	void shouldExecuteQuery() throws SQLException {
		var limit = 10_000;
		try (var connection = getConnection();
				var statement = connection.prepareStatement("UNWIND range(1, $1) AS x RETURN x")) {
			statement.setFetchSize(5);
			statement.setInt(1, limit);
			var resultSet = statement.executeQuery();
			for (var i = 1; i <= 17; i++) {
				Assertions.assertThat(resultSet.next()).isTrue();
				Assertions.assertThat(resultSet.getInt(1)).isEqualTo(i);
			}
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void shouldExecuteQueryWithoutAutoCommit(boolean commit) throws SQLException {
		var limit = 5;
		var testId = UUID.randomUUID().toString();
		try (var connection = getConnection()) {
			connection.setAutoCommit(false);
			try (var statement = connection.prepareStatement("UNWIND range(1, $1) AS x CREATE (n:Test {testId: $2})")) {
				statement.setInt(1, limit);
				statement.setString(2, testId);
				var resultSet = statement.executeQuery();
			}

			if (commit) {
				connection.commit();
			}
			else {
				connection.rollback();
			}

			try (var statement = connection.prepareStatement("MATCH (n:Test {testId: $1}) RETURN count(n)")) {
				statement.setString(1, testId);
				var resultSet = statement.executeQuery();
				resultSet.next();
				Assertions.assertThat(resultSet.getInt(1)).isEqualTo((commit) ? limit : 0);
			}
		}
	}

	private Connection getConnection() throws SQLException {
		var url = "jdbc:neo4j:onlyfortesting://%s:%d".formatted(this.neo4j.getHost(), this.neo4j.getMappedPort(7687));
		var driver = DriverManager.getDriver(url);
		var properties = new Properties();
		properties.put("user", "neo4j");
		properties.put("password", this.neo4j.getAdminPassword());
		return driver.connect(url, properties);
	}

}
