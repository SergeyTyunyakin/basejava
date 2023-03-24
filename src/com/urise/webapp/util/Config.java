package com.urise.webapp.util;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.web.ResumeServlet;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final String CONFIG_LOCAL_PATH = "C:\\WORK\\Java\\Projects\\basejava\\config\\resumes.properties";
    private static final String CONFIG_SERVER_PATH = "/WEB-INF/classes/resumes.properties";
    private static final Config INSTANCE = new Config();

    private Properties props = new Properties();
    private File storageDir;
    private SqlStorage sqlStorage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = getContextInputStream()) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            sqlStorage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Config file not found");
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }

    private InputStream getContextInputStream() throws FileNotFoundException {
        return (ResumeServlet.getContext() == null) ? new FileInputStream(CONFIG_LOCAL_PATH) : ResumeServlet.getContext().getResourceAsStream(CONFIG_SERVER_PATH);
    }

}