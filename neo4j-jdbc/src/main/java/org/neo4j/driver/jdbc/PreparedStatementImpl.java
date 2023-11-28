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
package org.neo4j.driver.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.jdbc.internal.bolt.BoltConnection;
import org.neo4j.driver.jdbc.values.Values;

final class PreparedStatementImpl extends StatementImpl implements PreparedStatement {

	private final String query;

	private final Map<String, Object> parameters = new HashMap<>();

	PreparedStatementImpl(Connection connection, BoltConnection boltConnection, boolean autoCommit, String query) {
		super(connection, boltConnection, autoCommit);
		this.query = query;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return super.executeQuery(this.query);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		throw new SQLException("Called on PreparedStatement");
	}

	@Override
	public int executeUpdate() throws SQLException {
		return 0;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		throw new SQLException("Called on PreparedStatement");
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {

	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {

	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

	}

	@Override
	@SuppressWarnings("deprecation")
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

	}

	@Override
	public void clearParameters() throws SQLException {
		this.parameters.clear();
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		this.parameters.put(String.valueOf(parameterIndex), Values.value(x));
	}

	@Override
	public boolean execute() throws SQLException {
		return false;
	}

	@Override
	public void addBatch() throws SQLException {

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {

	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {

	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {

	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return null;
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	protected Map<String, Object> parameters() {
		return this.parameters;
	}

}
