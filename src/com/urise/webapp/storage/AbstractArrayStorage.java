package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    @Override
    protected List<Resume> doCopyList() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        return getIndex(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Integer objectKey) {
        storage[objectKey] = r;
    }

    @Override
    protected boolean isExist(Integer objectKey) {
        return objectKey >= 0;
    }

    @Override
    protected void doSave(Resume r, Integer objectKey) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            saveByIndex(r, objectKey);
            size++;
        }
    }

    @Override
    protected Resume doGet(Integer objectKey) {
        return storage[objectKey];
    }

    @Override
    protected void doDelete(Integer objectKey) {
        deleteByIndex(objectKey);
        storage[size - 1] = null;
        size--;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void saveByIndex(Resume r, int index);

    protected abstract void deleteByIndex(int index);
}
