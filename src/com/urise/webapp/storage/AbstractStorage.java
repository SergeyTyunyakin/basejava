package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected abstract Object getObjectKey(String uuid);

    protected abstract void updateObject(Resume r, Object objectKey);

    protected abstract boolean objectExist(Object objectKey);

    protected abstract void saveObject(Resume r, Object objectKey);

    protected abstract Resume getObject(Object objectKey);

    protected abstract void deleteObject(Object objectKey);

    public void update(Resume r) {
        Object objectKey = getExistedObjectKey(r.getUuid());
        updateObject(r, objectKey);
    }

    public void save(Resume r) {
        Object objectKey = getNotExistedObjectKey(r.getUuid());
        saveObject(r, objectKey);
    }

    public void delete(String uuid) {
        Object objectKey = getExistedObjectKey(uuid);
        deleteObject(objectKey);
    }

    public Resume get(String uuid) {
        Object objectKey = getExistedObjectKey(uuid);
        return getObject(objectKey);
    }
    private Object getExistedObjectKey(String uuid) {
        Object objectKey = getObjectKey(uuid);
        if (!objectExist(objectKey)) {
            throw new NotExistStorageException(uuid);
        }
        return objectKey;
    }

    private Object getNotExistedObjectKey(String uuid) {
        Object objectKey = getObjectKey(uuid);
        if (objectExist(objectKey)) {
            throw new ExistStorageException(uuid);
        }
        return objectKey;
    }
}
