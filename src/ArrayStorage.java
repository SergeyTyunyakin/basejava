import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    int size = 0;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        if (size == storage.length) {
            // storage full
            System.out.println("ERROR: Storage full");
        } else {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(storage[i].toString(), r.toString())) {
                    // found a duplicate
                    System.out.println("ERROR: Resume with uuid " + storage[i].toString() + " is already exist!");
                    return;
                }
            }
            storage[size] = r;
            size++;
            System.out.println("Resume " + r.toString() + " saved");
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                // found uuid
                return storage[i];
            }
        }

        //not found
        System.out.println("ERROR: Uuid " + uuid + " not found");
        return null;
    }

    void delete(String uuid) {
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
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

        for (int k = j; k < size; k++) {
            storage[k - 1] = storage[k];
            storage[k] = null;
        }

        size--;
        System.out.println("Uuid " + uuid + " deleted");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
