package src.entities;
import java.time.LocalDate;

public class Transaction extends BaseEntity {
    private String kodeTransaksi, nis, kodeBuku;
    private LocalDate tglPinjam, tglKembali;
    private int status; // 0: belum dikembalikan, 1: dikembalikan

    public Transaction() {}
    public Transaction(String kodeTransaksi, String nis, String kodeBuku, LocalDate tglPinjam) {
        this.kodeTransaksi = kodeTransaksi; this.nis = nis; this.kodeBuku = kodeBuku;
        this.tglPinjam = tglPinjam; this.tglKembali = null; this.status = 0;
    }

    public String getId() { return kodeTransaksi; }
    public String getNis() { return nis; }
    public String getKodeBuku() { return kodeBuku; }
    public int getStatus() { return status; }
    public LocalDate getTglPinjam() { return tglPinjam; }

    public void kembalikanBuku() {
        this.status = 1;
        this.tglKembali = LocalDate.now();
    }

    public String toDataString() {
        String tglKembaliStr = (tglKembali == null) ? "null" : tglKembali.toString();
        return kodeTransaksi + DELIMITER + nis + DELIMITER + kodeBuku + DELIMITER + tglPinjam.toString() + DELIMITER + tglKembaliStr + DELIMITER + status;
    }

    public void fromDataString(String data) {
        String[] parts = data.split(DELIMITER);
        if (parts.length >= 6) {
            kodeTransaksi = parts[0]; nis = parts[1]; kodeBuku = parts[2];
            tglPinjam = LocalDate.parse(parts[3]);
            tglKembali = parts[4].equals("null") ? null : LocalDate.parse(parts[4]);
            status = Integer.parseInt(parts[5]);
        }
    }
}
