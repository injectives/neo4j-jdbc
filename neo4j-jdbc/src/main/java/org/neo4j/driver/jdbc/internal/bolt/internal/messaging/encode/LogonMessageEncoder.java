/*
 * Copyright (c) 2023-2024 "Neo4j,"
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
package org.neo4j.driver.jdbc.internal.bolt.internal.messaging.encode;

import java.io.IOException;

import org.neo4j.driver.jdbc.internal.bolt.internal.messaging.Message;
import org.neo4j.driver.jdbc.internal.bolt.internal.messaging.MessageEncoder;
import org.neo4j.driver.jdbc.internal.bolt.internal.messaging.ValuePacker;
import org.neo4j.driver.jdbc.internal.bolt.internal.messaging.request.LogonMessage;
import org.neo4j.driver.jdbc.internal.bolt.internal.util.Preconditions;

public final class LogonMessageEncoder implements MessageEncoder {

	@Override
	public void encode(Message message, ValuePacker packer) throws IOException {
		Preconditions.checkArgument(message, LogonMessage.class);
		var logonMessage = (LogonMessage) message;
		packer.packStructHeader(1, logonMessage.signature());
		packer.pack(logonMessage.metadata());
	}

}
