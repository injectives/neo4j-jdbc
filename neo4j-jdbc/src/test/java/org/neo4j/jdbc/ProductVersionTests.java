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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductVersionTests {

	@Test
	void getValueShouldWork() {
		Assertions.assertThat(ProductVersion.getValue()).isEqualTo("dev");
	}

	@Test
	void getMajorVersion() {

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(ProductVersion::getMajorVersion)
			.withMessage("Unsupported or unknown version 'dev'");
	}

	@Test
	void getMinorVersion() {

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(ProductVersion::getMinorVersion)
			.withMessage("Unsupported or unknown version 'dev'");
	}

}
