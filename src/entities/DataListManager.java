package src.entities;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import src.exceptions.DataNotFoundException;
import src.exceptions.DuplicateDataException;


public class DataListManager<T extends BaseEntity> {
    // Atribut
    private List<T> list;
    private String filePath;
    private Supplier<T> factory; // Supplier digunakan pada method loadData (create data dari txt)

    // Constructor
    public DataListManager(String filePath, Supplier<T> factory) {
        this.filePath = filePath;
        this.factory = factory;
        this.list = new ArrayList<>();
        loadData();
    }
    
    // Method
    // Mengembalikan semua item
    public List<T> getAll() { return list; }

    // mengembalikan satu item (atau null jika tidak ditemukan) sesuai id (menggunakan filter)
    public T getSingle(String id) {
        return list.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    // membuat item baru (jika id sudah ada, akan mengeluarkan exception)
    public void create(T item) throws DuplicateDataException {
        if (getSingle(item.getId()) != null) {
            throw new DuplicateDataException("Gagal: Data dengan ID " + item.getId() + " sudah terdaftar!");
        }
        list.add(item);
        saveData();
    }

    // memperbarui list item (jika id tidak ditemukan, akan mengeluarkan exception)
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

    // menghapus item (jika id tidak ditemukan, akan mengeluarkan exception)
    public void delete(String id) throws DataNotFoundException {
        T item = getSingle(id);
        if (item == null) {
            throw new DataNotFoundException("Gagal: Tidak bisa menghapus, data tidak ditemukan.");
        }
        list.remove(item);
        saveData();
        System.out.println("Data berhasil dihapus.");
    }

    // mengembalikan semua item dari file txt
    private void loadData() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            list.clear();
            while ((line = br.readLine()) != null) {
                // menggunakan supplier karena tidak bisa create object dari generic [new T()]
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
                int size = this.list.size();
                for(int i=0; i<size; i++){
                    bw.write(list.get(i).toDataString());

                    if(i != size-1){
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception saat menyimpan file " + filePath + ": " + e.getMessage());
        }
    }
}

