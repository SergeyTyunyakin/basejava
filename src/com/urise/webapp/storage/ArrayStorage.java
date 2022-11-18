package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    protected int getIndex(String uuid) {
        for (int index = 0; index < size; index++) {
            if (Objects.equals(storage[index].getUuid(), uuid)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    protected void saveByIndex(Resume r, int index) {
        storage[size] = r;
    }

    @Override
    protected void deleteByIndex(int index) {
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
    }

}
