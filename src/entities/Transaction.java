package src.entities;
import java.time.LocalDate;

public class Transaction extends BaseEntity {
    private String kodeTransaksi, nis, kodeBuku;
    private LocalDate tglPinjam, tglKembali;
    private int status; // 0: belum dikembalikan, 1: dikembalikan

    public Transaction() {}
}
