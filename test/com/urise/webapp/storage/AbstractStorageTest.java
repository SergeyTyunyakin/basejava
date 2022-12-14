package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractStorageTest {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final String DUMMY_UUID = "dummy";
    protected static final Resume RESUME_1;
    protected static final Resume RESUME_2;
    protected static final Resume RESUME_3;
    protected static final Resume RESUME_4;

    static {
        RESUME_1 = ResumeTestData.createResume(UUID_1, "Name1");
        RESUME_2 = ResumeTestData.createResume(UUID_2, "Name2");
        RESUME_3 = ResumeTestData.createResume(UUID_3, "Name3");
        RESUME_4 = ResumeTestData.createResume(UUID_4, "Name4");
    }

    protected final Storage storage;
    protected int initialSize;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void initStorage() {
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
        initialSize = 3;
    }

    @Test
    public void size() {
        assertSize(initialSize);
    }

    void assertSize(int testSize) {
        assertEquals(testSize, storage.size());
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    public void assertGet(Resume r) {
        assertEquals(storage.get(r.getUuid()), r);
    }

    @Test
    public void getAll() {
        assertArrayEquals(new Resume[]{RESUME_1, RESUME_2, RESUME_3}, storage.getAllSorted().toArray());
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(new Resume[]{}, storage.getAllSorted().toArray());
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_2, "Name2");
        storage.update(newResume);
        assertSame(storage.get(UUID_2), newResume);
    }

    @Test
    public void updateNotExist() {
        Resume newResume = new Resume("New Name");
        assertThrows(NotExistStorageException.class, () -> storage.update(newResume));
    }

    @Test
    public void save() {
        Resume newResume = new Resume(UUID_4);
        storage.save(newResume);
        assertGet(newResume);
        assertSize(4);
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_2));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete(DUMMY_UUID));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get(DUMMY_UUID));
    }
}