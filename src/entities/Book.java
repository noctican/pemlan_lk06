package src.entities;

public class Book extends BaseEntity {
    private String kode, judul, jenis;

    public Book() {}
    public Book(String kode, String judul, String jenis) {
        this.kode = kode; this.judul = judul; this.jenis = jenis;
    }

    public String getId() { return kode; }
    public String getJudul() { return judul; }
    public String getJenis() { return jenis; }

    public String toDataString() { return kode + DELIMITER + judul + DELIMITER + jenis; }
    public void fromDataString(String data) {
        String[] parts = data.split(DELIMITER);
        if (parts.length >= 3) { kode = parts[0]; judul = parts[1]; jenis = parts[2]; }
    }
}

