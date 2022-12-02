package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStorage extends AbstractStorage {
    protected List<Resume> list = new ArrayList<>();

    @Override
    protected Object getObjectKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(list.get(i).getUuid(), uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected void updateObject(Resume r, Object objectKey) {
        list.set((Integer) objectKey, r);
    }

    @Override
    protected boolean objectExist(Object objectKey) {
        return objectKey != null;
    }

    @Override
    protected void saveObject(Resume r, Object objectKey) {
        list.add(r);
    }

    @Override
    protected Resume getObject(Object objectKey) {
        return list.get((Integer) objectKey);
    }

    @Override
    protected void deleteObject(Object objectKey) {
        list.remove(((Integer) objectKey).intValue());
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Resume[] getAll() {
        return list.toArray(new Resume[size()]);
    }

    @Override
    public int size() {
        return list.size();
    }
}
