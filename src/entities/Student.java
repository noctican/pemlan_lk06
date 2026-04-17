package src.entities;

public class Student extends BaseEntity {
    private String nis, nama, alamat;

    public Student() {}
    public Student(String nis, String nama, String alamat) {
        this.nis = nis; this.nama = nama; this.alamat = alamat;
    }

    public String getId() { return nis; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }

    public String toDataString() { return nis + DELIMITER + nama + DELIMITER + alamat; }
    public void fromDataString(String data) {
        String[] parts = data.split(DELIMITER);
        if (parts.length >= 3) { nis = parts[0]; nama = parts[1]; alamat = parts[2]; }
    }
}