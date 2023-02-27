package com.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlTemplate<T> {
    T execute(PreparedStatement statement) throws SQLException;
}
