package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionTemplate<T> {
    T execute(Connection connection) throws SQLException;
}
