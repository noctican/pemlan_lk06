package src.entities;
import exceptions.DataNotFoundException;
import exceptions.DuplicateDataException;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;


public class DataListManager<T extends BaseEntity> {
    private List<T> list;
    private String filePath;
    private Supplier<T> factory;

    public DataListManager(String filePath, Supplier<T> factory) {
        this.filePath = filePath;
        this.factory = factory;
        this.list = new ArrayList<>();
        loadData();
    }

    public List<T> getAll() { return list; }

    public T getSingle(String id) {
        return list.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    public void create(T item) throws DuplicateDataException {
        if (getSingle(item.getId()) != null) {
            throw new DuplicateDataException("Gagal: Data dengan ID " + item.getId() + " sudah terdaftar!");
        }
        list.add(item);
        saveData();
    }

    public void update(T updatedItem) throws DataNotFoundException {
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(updatedItem.getId())) {
                list.set(i, updatedItem);
                found = true;
                break;
            }
        }
        if(!found) throw new DataNotFoundException("Gagal: Data dengan ID " + updatedItem.getId() + " tidak ditemukan.");
        saveData();
    }

    public void delete(String id) throws DataNotFoundException {
        T item = getSingle(id);
        if (item == null) {
            throw new DataNotFoundException("Gagal: Tidak bisa menghapus, data tidak ditemukan.");
        }
        list.remove(item);
        saveData();
        System.out.println("Data berhasil dihapus.");
    }

    private void loadData() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            list.clear();
            while ((line = br.readLine()) != null) {
                T item = factory.get();
                item.fromDataString(line);
                list.add(item);
            }
        } catch (IOException e) {
            System.out.println("Exception saat membaca file " + filePath + ": " + e.getMessage());
        }
    }

    public void saveData() {
        try {
            File dir = new File("./src/data");
            if (!dir.exists()) dir.mkdirs();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (T item : list) {
                    bw.write(item.toDataString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Exception saat menyimpan file " + filePath + ": " + e.getMessage());
        }
    }
}

