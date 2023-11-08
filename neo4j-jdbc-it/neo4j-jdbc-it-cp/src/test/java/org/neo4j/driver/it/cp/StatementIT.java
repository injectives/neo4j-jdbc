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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatementIT {

	@SuppressWarnings("resource") // On purpose to reuse this
	protected final Neo4jContainer<?> neo4j = TestUtils.getNeo4jContainer();

	@BeforeAll
	void startNeo4j() {
		this.neo4j.start();
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void shouldExecuteQuery(boolean getByLabel) throws SQLException {
		try (var connection = getConnection(); var statement = connection.createStatement()) {
			statement.setFetchSize(5);
			var resultSet = statement.executeQuery("UNWIND range(1, 10000) AS x RETURN x");
			for (var i = 1; i <= 17; i++) {
				assertThat(resultSet.next()).isTrue();
				var value = getByLabel ? resultSet.getInt("x") : resultSet.getInt(1);
				assertThat(value).isEqualTo(i);
			}
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void shouldExecuteQueryWithoutAutoCommit(boolean commit) throws SQLException {
		var testId = UUID.randomUUID().toString();
		try (var connection = getConnection()) {
			connection.setAutoCommit(false);
			try (var statement = connection.createStatement()) {
				var resultSet = statement
					.executeQuery(String.format("UNWIND range(1, 5) AS x CREATE (n:Test {testId: '%s'})", testId));
			}

			if (commit) {
				connection.commit();
			}
			else {
				connection.rollback();
			}

			try (var statement = connection.createStatement()) {
				var resultSet = statement
					.executeQuery(String.format("MATCH (n:Test {testId: '%s'}) RETURN count(n)", testId));
				resultSet.next();
				assertThat(resultSet.getInt(1)).isEqualTo((commit) ? 5 : 0);
			}
		}
	}

	@Test
	void shouldExecuteUpdate() throws SQLException {
		try (var connection = getConnection(); var statement = connection.createStatement()) {
			var num = statement.executeUpdate("UNWIND range(1, 5) AS x CREATE ()");
			assertThat(num).isEqualTo(5);
		}
	}

	@Test
	void closingOnCompletionShouldWork() throws SQLException {
		ResultSet rs;
		try (var connection = getConnection(); var stmt = connection.createStatement();) {
			stmt.closeOnCompletion();
			rs = stmt.executeQuery("MATCH (n) RETURN count(n)");
		}
		assertThat(rs).isNotNull();
		assertThat(rs.isClosed()).isTrue();
	}

	private Connection getConnection() throws SQLException {
		var url = "jdbc:neo4j://%s:%d".formatted(this.neo4j.getHost(), this.neo4j.getMappedPort(7687));
		var driver = DriverManager.getDriver(url);
		var properties = new Properties();
		properties.put("user", "neo4j");
		properties.put("password", this.neo4j.getAdminPassword());
		return driver.connect(url, properties);
	}

}
