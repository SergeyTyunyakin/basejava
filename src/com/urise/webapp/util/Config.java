package com.urise.webapp.util;

import com.urise.webapp.storage.SqlStorage;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PATH = "config/resumes.properties";
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
            throw new IllegalStateException("Config file not found" + CONFIG_PATH);
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }

    private InputStream getContextInputStream() throws FileNotFoundException {
//        return (ResumeServlet.getContext() == null) ? new FileInputStream(CONFIG_PATH) : ResumeServlet.getContext().getResourceAsStream("/" + CONFIG_PATH);
        return (new FileInputStream("C:\\WORK\\Java\\Projects\\basejava\\/" + CONFIG_PATH));
    }

}