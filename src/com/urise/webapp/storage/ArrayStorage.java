package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size - 1, null);
        size = 0;
    }

    public int findUuid(String uuid) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(storage[i].getUuid(), uuid)) {
                // found uuid
                return i;
            }
        }
        // not found uuid
        return -1;
    }

    public void update(Resume r) {
        int i = findUuid(r.getUuid());
        if (i >= 0) {
            storage[i] = r;
            System.out.println("Resume " + r.getUuid() + " updated");
        } else {
            System.out.println("ERROR: Uuid " + r.getUuid() + " not found");
        }
    }

    public void save(Resume r) {
        if (size == storage.length) {
            // storage full
            System.out.println("ERROR: Storage full");
        } else {
            int i = findUuid(r.getUuid());
            if (i >= 0) {
                System.out.println("ERROR: Resume with uuid " + r.getUuid() + " is already exist!");
            } else {
                storage[size] = r;
                size++;
                System.out.println("Resume " + r.getUuid() + " saved");
            }
        }
    }

    public Resume get(String uuid) {
        int i = findUuid(uuid);
        if (i >= 0) {
            // found uuid
            return storage[i];
        } else {
            //not found
            System.out.println("ERROR: Uuid " + uuid + " not found");
            return null;
        }
    }

    public void delete(String uuid) {
        int i = findUuid(uuid);
        if (i >= 0) {
            // found uuid
            storage[i] = storage[size - 1];
            storage[size - 1] = null;
            size--;
            System.out.println("Uuid " + uuid + " deleted");
        } else {
            //not found
            System.out.println("ERROR: Uuid " + uuid + " not found");
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
