/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        int n = storage.length;
        for (int i = 0; i < n; i++) {
            if (storage[i] == null) {
                break;
            }
            storage[i] = null;
        }
    }

    void save(Resume r) {
        int n = storage.length;
        for (int i = 0; i < n; i++) {
            if (storage[i] == null) {
                // found an empty slot
                storage[i] = r;
                return;
            }
            if (storage[i].toString() == r.toString()) {
                // found a duplicate
                System.out.println("ERROR: Resume with " + storage[i].toString() + " is already exist!");
                return;
            }
        }

        // storage full
        System.out.println("ERROR: Storage full");
    }

    Resume get(String uuid) {
        int n = storage.length;
        for (int i = 0; i < n; i++) {
            if (storage[i] == null) {
                break;
            }
            if (storage[i].toString() == uuid) {
                // found uuid
                return storage[i];
            }
        }

        //not found
        System.out.println("ERROR: Uuid " + uuid + " not found");
        return null;
    }

    void delete(String uuid) {
        int n = storage.length;
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (storage[i] == null) {
                // uuid not found
                System.out.println("ERROR: Uuid " + uuid + " not found");
                return;
            }
            if (storage[i].toString() == uuid) {
                // found uuid
                storage[i] = null;
                j = i + 1;
                break;
            }
        }

        if (j == 0) {
            // uuid not found
            System.out.println("ERROR: Uuid " + uuid + " not found");
            return;
        }

        for (int k = j; k < n; k++) {
            if (storage[k] == null) {
                return;
            }
            storage[k - 1] = storage[k];
            storage[k] = null;
        }
        System.out.println("Uuid " + uuid + " deleted");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        int n = size();
        Resume[] storagenew = new Resume[n];
        for (int i = 0; i < n; i++) {
            storagenew[i] = storage[i];
        }
        return storagenew;
    }

    int size() {
        int len = 0;
        int n = storage.length;
        for (int i = 0; i < n; i++) {
            if (storage[i] == null) {
                break;
            }
            len++;
        }
        return len;
    }
}
