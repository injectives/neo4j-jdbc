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
package org.neo4j.jdbc.translator.sparkcleaner;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SparkSubqueryCleaningTranslatorFactoryTests {

	private static final int DEFAULT_ORDER = Integer.MAX_VALUE - 5;

	@Test
	void shouldCreateTranslatorWithoutConfig() {
		assertThat(new SparkSubqueryCleaningTranslatorFactory().create(null).getOrder()).isEqualTo(DEFAULT_ORDER);
	}

	@Test
	void shouldCreateTranslatorWithEmptyConfig() {
		assertThat(new SparkSubqueryCleaningTranslatorFactory().create(Map.of()).getOrder()).isEqualTo(DEFAULT_ORDER);
	}

	@Test
	void shouldCreateTranslator() {
		assertThat(new SparkSubqueryCleaningTranslatorFactory().create(Map.of("s2c.precedence", 10)).getOrder())
			.isEqualTo(9);
	}

}
