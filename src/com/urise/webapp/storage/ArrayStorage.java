package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    static final int STORAGE_LIMIT = 10000;
    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size - 1, null);
        size = 0;
    }

    public int findIndex(String uuid) {
        for (int index = 0; index < size; index++) {
            if (Objects.equals(storage[index].getUuid(), uuid)) {
                return index;
            }
        }
        return -1;
    }

    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        if (index == -1) {
            System.out.println("ERROR: Uuid " + r.getUuid() + " not found");
        } else {
            storage[index] = r;
            System.out.println("Resume " + r.getUuid() + " updated");
        }
    }

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("ERROR: Storage full");
        } else if (findIndex(r.getUuid()) == -1) {
            storage[size] = r;
            size++;
            System.out.println("Resume " + r.getUuid() + " saved");
        } else {
            System.out.println("ERROR: Resume with uuid " + r.getUuid() + " is already exist!");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("ERROR: Uuid " + uuid + " not found");
            return null;
        } else {
            return storage[index];
        }
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("ERROR: Uuid " + uuid + " not found");
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
            System.out.println("Uuid " + uuid + " deleted");
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}
