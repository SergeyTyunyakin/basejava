package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import static java.util.Arrays.sort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MapStorageTest extends AbstractArrayStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    void checkStorageOverflow() {

    }

    @Override
    void getAll() {
        Resume[] testedArray = storage.getAll();
        sort(testedArray);
        assertArrayEquals(new Resume[]{RESUME_1, RESUME_2, RESUME_3}, testedArray);
    }
}