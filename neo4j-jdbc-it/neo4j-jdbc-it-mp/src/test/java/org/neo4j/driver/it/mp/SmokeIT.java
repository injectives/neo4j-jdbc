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
package org.neo4j.driver.it.mp;

import java.sql.DriverManager;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Just making sure the driver can be loaded on the module path.
 * <p>
 * Not using Testcontainers here as it is quite some pain doing it on the module path.
 */
public class SmokeIT {

	@Test
	@EnabledIf("boltPortIsReachable")
	void driverShouldBeLoaded() {

		var url = "jdbc:neo4j:onlyfortesting://%s:%d".formatted(getHost(), getPort());
		assertThatNoException().isThrownBy(() -> DriverManager.getDriver(url));
	}

	static boolean boltPortIsReachable() {

		return new BoltHandshaker(getHost(), getPort()).isBoltPortReachable(Duration.ofSeconds(10));
	}

	private static int getPort() {
		return Integer.parseInt(System.getProperty("it-database-port", "7687"));
	}

	private static String getHost() {
		return "localhost";
	}

}
