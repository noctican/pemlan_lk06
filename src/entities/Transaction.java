package src.entities;
import java.time.LocalDate;

public class Transaction extends BaseEntity {
    // Atribut
    private String kodeTransaksi, nis, kodeBuku;
    private LocalDate tglPinjam, tglKembali;
    private int status; // 0: belum dikembalikan, 1: dikembalikan

    // Cosntructor kosong sebagai syarat untuk membaca file.
    public Transaction() {}
    public Transaction(String kodeTransaksi, String nis, String kodeBuku, LocalDate tglPinjam) {
        this.kodeTransaksi = kodeTransaksi; 
        this.nis = nis; 
        this.kodeBuku = kodeBuku;
        this.tglPinjam = tglPinjam; 
        this.tglKembali = null; 
        this.status = 0;
    }

    // Getter
    public String getId() {
        return kodeTransaksi;
    }
    public String getNis() { 
        return nis; 
    }
    public String getKodeBuku() { 
        return kodeBuku; 
    }
    public int getStatus() { 
        return status; 
    }
    public LocalDate getTglPinjam() { 
        return tglPinjam; 
    }

    // Method untuk mengubah status menjadi sudah dikembalikan dan langsung mencatat tanggal hari ini.
    public void kembalikanBuku() {
        this.status = 1;
        this.tglKembali = LocalDate.now();
    }

    //Untuk menyusun data agar siap disimpan ke dalam file txt.
    public String toDataString() {
        String tglKembaliStr = (tglKembali == null) ? "null" : tglKembali.toString();
        return kodeTransaksi + DELIMITER + nis + DELIMITER + kodeBuku + DELIMITER + tglPinjam.toString() + DELIMITER + tglKembaliStr + DELIMITER + status;
    }

    //Untuk membaca teks sebaris dari file txt lalu memotong-motongnya.
    public void fromDataString(String data) {
        String[] parts = data.split(DELIMITER);
        if (parts.length >= 6) {
            this.kodeTransaksi = parts[0]; 
            this.nis = parts[1]; 
            this.kodeBuku = parts[2];
            this.tglPinjam = LocalDate.parse(parts[3]);
            // Jika di dalam file terdapat tulisan "null", berarti tanggal kembalinya dibiarkan kosong (bernilai null).
            this.tglKembali = parts[4].equals("null") ? null : LocalDate.parse(parts[4]);
            this.status = Integer.parseInt(parts[5]);
        }
    }
}
