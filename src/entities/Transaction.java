package src.entities;
import java.time.LocalDate;

public class Transaction extends BaseEntity {
    // Atribut
    private String kodeTransaksi, nis, kodeBuku;
    private LocalDate tglPinjam, tglKembali;
    private int status; // 0: belum dikembalikan, 1: dikembalikan

    // Cosntructor
    public Transaction() {}

    // Getter
    public String getId() {return kodeTransaksi;}

    // Method
    public String toDataString() { return kodeTransaksi + DELIMITER + nis + DELIMITER + kodeBuku + DELIMITER + tglPinjam;}
}
