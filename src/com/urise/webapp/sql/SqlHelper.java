package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.connectionFactory = connectionFactory;
    }

    public static StorageException sqlStorageException(SQLException e) {
        if (UNIQUE_VIOLATION.getState().equals(e.getSQLState())) {
            throw new ExistStorageException(e);
        }
        throw new StorageException(e);
    }

    public <T> T execute(String sql, SqlTemplate<T> sqlTemplate) {
        try (var conn = connectionFactory.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            return sqlTemplate.execute(statement);
        } catch (SQLException e) {
            throw sqlStorageException(e);
        }
    }

    public <T> T transactionExecute(TransactionTemplate<T> transactionTemplate) {
        try (var conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T returnValue = transactionTemplate.execute(conn);
                conn.commit();
                return returnValue;
            } catch (SQLException e) {
                conn.rollback();
                throw sqlStorageException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}
