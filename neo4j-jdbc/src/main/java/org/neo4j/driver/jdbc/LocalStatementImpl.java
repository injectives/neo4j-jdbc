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
package org.neo4j.driver.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * This is intended as almost a no-op implementation of a statement that we mainly return
 * from metadata calls.
 *
 * @author Conor Watson
 */
final class LocalStatementImpl extends StatementImpl {

	LocalStatementImpl() {
		super();
	}

	private boolean closeOnCompletion;

	private boolean closed;

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void close() {
		this.closed = true;
	}

	@Override
	public void cancel() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean getMoreResults() {
		return false;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int getFetchSize() {
		return -1;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public Connection getConnection() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean isClosed() {
		return this.closed;
	}

	// While this statement or more specific its result set does not complete as it is
	// always "complete" and therefor
	// closing on completion can't happen, we decided to provide these implementations
	// essentially as no-ops for better
	// quality of life for people implementing any flow that sets {@code
	// closeOnCompletion}.
	@Override
	public void closeOnCompletion() {
		this.closeOnCompletion = true;
	}

	@Override
	public boolean isCloseOnCompletion() {
		return this.closeOnCompletion;
	}

}
