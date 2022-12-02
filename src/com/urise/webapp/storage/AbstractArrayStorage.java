package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected Object getObjectKey(String uuid) {
        return getIndex(uuid);
    }

    @Override
    protected void updateObject(Resume r, Object objectKey) {
        storage[(Integer) objectKey] = r;
    }

    @Override
    protected boolean objectExist(Object objectKey) {
        return (Integer) objectKey >= 0;
    }

    @Override
    protected void saveObject(Resume r, Object objectKey) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            saveByIndex(r, (Integer) objectKey);
            size++;
        }
    }

    @Override
    protected Resume getObject(Object objectKey) {
        return storage[(Integer) objectKey];
    }

    @Override
    protected void deleteObject(Object objectKey) {
        deleteByIndex((Integer) objectKey);
        storage[size - 1] = null;
        size--;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void saveByIndex(Resume r, int index);

    protected abstract void deleteByIndex(int index);
}
