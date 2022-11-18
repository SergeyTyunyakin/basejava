package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR: Uuid " + uuid + " not found");
            return null;
        } else {
            return storage[index];
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            System.out.println("ERROR: Uuid " + r.getUuid() + " not found");
        } else {
            storage[index] = r;
            System.out.println("Resume " + r.getUuid() + " updated");
        }
    }

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("ERROR: Storage full");
            return;
        }
        int index = getIndex(r.getUuid());
        if (index < 0) {
            saveByIndex(r, index);
            size++;
            System.out.println("Resume " + r.getUuid() + " saved");
        } else {
            System.out.println("ERROR: Resume with uuid " + r.getUuid() + " is already exist!");
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR: Uuid " + uuid + " not found");
        } else {
            deleteByIndex(index);
            size--;
            System.out.println("Uuid " + uuid + " deleted");
        }
    }

    protected abstract int getIndex(String uuid);

    protected abstract void saveByIndex(Resume r, int index);

    protected abstract void deleteByIndex(int index);
}
