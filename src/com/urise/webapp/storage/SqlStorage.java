package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::executeUpdate);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionExecute((conn) -> {
            try (PreparedStatement ps = conn.prepareStatement("" + "    SELECT * FROM resume r " + " LEFT JOIN contact c " + "        ON r.uuid = c.resume_uuid " + "     WHERE r.uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                Resume r = new Resume(uuid, rs.getString("full_name"));
                do {
                    String value = rs.getString("value");
                    ContactType type = ContactType.valueOf(rs.getString("type"));
                    r.addContact(type, value);
                } while (rs.next());
                return r;
            }
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionExecute((conn) -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume r SET full_name = ? WHERE r.uuid =?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(conn, r);
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionExecute((conn) -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.transactionExecute((conn) -> {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM resume r WHERE r.uuid =?")) {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionExecute((conn) -> {
            Map<String, Resume> resumeMap = new LinkedHashMap<>();

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumeMap.put(rs.getString("uuid"), new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT type, value, resume_uuid FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumeMap.get(rs.getString("resume_uuid")).addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }

            return resumeMap.values().stream().toList();
        });
    }

    @Override
    public int size() {
        return sqlHelper.transactionExecute((conn) -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM resume")) {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        });
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid =?")) {
            st.setString(1, r.getUuid());
            st.executeUpdate();
        }
    }
}
