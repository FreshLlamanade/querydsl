/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.ddl;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableClauseH2Test {
    private Connection conn;

    private Statement stmt;
    private Configuration configuration;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:./target/h2-gen";
        conn = DriverManager.getConnection(url, "sa", "");
        stmt = conn.createStatement();
        configuration = new Configuration(new H2Templates());
    }

    @After
    public void tearDown() throws SQLException {
        try {
            stmt.close();
        } finally {
            conn.close();
        }
    }

    @Test
    public void createTableTest() {
        CreateTableClause createTableClause = createTable("primitives")
                .column("id", int.class)
                .column("name", String.class)
                .column("birthday", java.sql.Date.class)
                .primaryKey("primitives_PK", "id");

        String stmnt = createTableClause.createSQLStatement();
        String epected = "create table primitives (\n" +
                "  id INTEGER,\n" +
                "  name VARCHAR,\n" +
                "  birthday DATE,\n" +
                "  CONSTRAINT primitives_PK PRIMARY KEY(id)\n" +
                ")\n";
        Assert.assertEquals(stmnt, epected);
        
        //DropTableClause dtc = new DropTableClause(conn, configuration, "primitives");
        //dtc.execute();
        //createTableClause.execute();
    }

    private CreateTableClause createTable(String tableName) {
        return new CreateTableClause(conn, configuration, tableName);
    }
}