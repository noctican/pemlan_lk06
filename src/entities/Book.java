package src.entities;

public class Book extends BaseEntity {
    private String kode, judul, jenis;

    // Empty constructor sebagai pancingan awal saat DataListManager membaca file txt.
    public Book() {}
    public Book(String kode, String judul, String jenis) {
        this.kode = kode;
        this.judul = judul;
        this.jenis = jenis;
    }
    
    //Getter
    public String getId() {
        return kode;
    }
    public String getJudul() {
        return judul;
    }
    public String getJenis() {
        return jenis;
    }

    //Untuk menggabungkan data menjadi sebaris teks agar mudah disimpan ke dalam file txt.
    public String toDataString() {
        return kode + DELIMITER + judul + DELIMITER + jenis;
    }

    //Untuk memecah baris teks dari file txt untuk dimasukkan kembali ke dalam variabel objek.
    public void fromDataString(String data) {
        String[] parts = data.split(DELIMITER);
        if (parts.length >= 3) {
            this.kode = parts[0];
            this.judul = parts[1];
            this.jenis = parts[2];
        }
    }
}

