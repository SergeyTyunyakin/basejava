package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.AbstractSection;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final String DELIMITER = "\n";
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
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT uuid, full_name FROM resume WHERE uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
            }

            try (PreparedStatement st = conn.prepareStatement("SELECT type, value  FROM contact WHERE resume_uuid =?")) {
                st.setString(1, r.getUuid());
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }

            try (PreparedStatement st = conn.prepareStatement("SELECT type, value  FROM section WHERE resume_uuid =?")) {
                st.setString(1, r.getUuid());
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    addSection(rs, r);
                }
            }
            return r;
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
            deleteSections(conn, r);
            insertContacts(conn, r);
            insertSections(conn, r);
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
            insertSections(conn, r);
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

            try (PreparedStatement ps = conn.prepareStatement("SELECT uuid, full_name FROM resume r ORDER BY full_name, uuid")) {

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

            try (PreparedStatement ps = conn.prepareStatement("SELECT type, value, resume_uuid FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(rs, resumeMap.get(rs.getString("resume_uuid")));
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

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                var jsonParser = new JsonParser();
                String sectionStrings = jsonParser.write(e.getValue(), AbstractSection.class);
                //                String sectionStrings = switch (e.getKey()) {
//                    case OBJECTIVE, PERSONAL -> ((TextSection) e.getValue()).getText();
//                    case ACHIEVEMENT, QUALIFICATIONS ->
//                            String.join(DELIMITER, ((ListTextSection) e.getValue()).getItems());
//                    default -> null;
//                };
                if (sectionStrings != null) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, sectionStrings);
                    ps.addBatch();
                }
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

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM section WHERE resume_uuid =?")) {
            st.setString(1, r.getUuid());
            st.executeUpdate();
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        var sectionType = SectionType.valueOf(rs.getString("type"));
        var jsonParser = new JsonParser();
        r.addSection(sectionType, jsonParser.read(rs.getString("value"), AbstractSection.class));
    }
}
