package com.urise.webapp.storage;

import com.urise.webapp.storage.serialization.XmlStreamSerializer;

public class XmlPathStorageTest extends com.urise.webapp.storage.AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerializer()));
    }
}
