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
package org.neo4j.jdbc.internal.bolt.internal.messaging.protocol;

import org.neo4j.jdbc.internal.bolt.internal.messaging.MessageFormat;
import org.neo4j.jdbc.internal.bolt.internal.packstream.PackInput;
import org.neo4j.jdbc.internal.bolt.internal.packstream.PackOutput;

public final class MessageFormatV51 implements MessageFormat {

	@Override
	public Writer newWriter(PackOutput output) {
		return new MessageWriterV51(output);
	}

	@Override
	public Reader newReader(PackInput input) {
		return new MessageReaderV51(input);
	}

}
