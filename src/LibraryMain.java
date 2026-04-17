package src;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import src.interfaces.*;
import src.entities.*;
import src.exceptions.*;


public class LibraryMain {
    private static DataListManager<Student> studentMgr = new DataListManager<>("./src/data/students.txt", Student::new);
    private static DataListManager<Book> bookMgr = new DataListManager<>("./src/data/books.txt", Book::new);
    private static DataListManager<Employee> employeeMgr = new DataListManager<>("./src/data/employees.txt", Employee::new);
    private static DataListManager<Transaction> transactionMgr = new DataListManager<>("./src/data/transactions.txt", Transaction::new);
    
    private static Scanner scanner = new Scanner(System.in);
    private static Employee loggedInUser = null;

    public static void main(String[] args) {
        initDefaultAdmin();
        System.out.println("=== SISTEM INFORMASI PERPUSTAKAAN SMP ===");
        
        while (loggedInUser == null) {
            handleLogin();
        }

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU UTAMA ---");
            System.out.println("1. Master Data (Siswa, Buku, Pegawai)");
            System.out.println("2. Transaksi (Pinjam & Kembali)");
            System.out.println("3. Laporan");
            System.out.println("0. Logout & Keluar");
            System.out.print("Pilih menu: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                menuMasterData();
            } else if (choice.equals("2")) {
                menuTransaksi();
            } else if (choice.equals("3")) {
                menuLaporan();
            } else if (choice.equals("0")) {
                running = false;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
        System.out.println("Keluar dari sistem. Terima kasih.");
    }

    private static void initDefaultAdmin() {
        if (employeeMgr.getAll().isEmpty()) {
            System.out.println("[INFO] Membuat akun admin default...");
            Employee admin = new Employee("111", "Pegawai kesatu", "2000-01-01", "password", false);
            try {
                employeeMgr.create(admin);
            } catch(DuplicateDataException e) {
                System.out.println("ALERT ERROR: " + e.getMessage());
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Masukkan NIP Pegawai: ");
        String nip = scanner.nextLine();
        System.out.print("Masukkan Password: ");
        String pass = scanner.nextLine();

        Employee emp = employeeMgr.getSingle(nip);
        if (emp != null && emp.getPasswordHash().equals(Employee.hashPassword(pass))) {
            loggedInUser = emp;
            System.out.println("\n[LOGIN BERHASIL] Selamat datang, " + emp.getNama());
        } else {
            System.out.println("[LOGIN GAGAL] NIP atau Password salah!\n");
        }
    }

    private static void menuMasterData() {
        System.out.println("\n-- Master Data --");
        System.out.println("1. Kelola Siswa\n2. Kelola Buku\n3. Kelola Pegawai");
        System.out.print("Pilih: ");
        String choice = scanner.nextLine();
        
        if (choice.equals("1")) {
            kelolaData("Siswa", studentMgr, getStudentFormHandler());
        } else if (choice.equals("2")) {
            kelolaData("Buku", bookMgr, getBookFormHandler());
        } else if (choice.equals("3")) {
            kelolaData("Pegawai", employeeMgr, getEmployeeFormHandler());
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    private static <T extends BaseEntity> void kelolaData(String namaEntitas, DataListManager<T> manager, DataFormHandler<T> handler) {
        System.out.println("\n--- Kelola Data " + namaEntitas + " ---");
        System.out.println("1. Tambah Data");
        System.out.println("2. Lihat Data");
        System.out.println("3. Ubah Data (Update)");
        System.out.println("4. Hapus Data");
        System.out.print("Pilih Aksi: ");
        String aksi = scanner.nextLine();

        if (aksi.equals("1")) {
            System.out.println("\n[Tambah " + namaEntitas + "]");
            T newData = handler.createInput(scanner);
            if (newData != null) {
                try {
                    manager.create(newData);
                    System.out.println("Data berhasil ditambahkan!");
                } catch (DuplicateDataException e) {
                    System.out.println("ALERT ERROR: " + e.getMessage());
                }
            }
            
        } else if (aksi.equals("2")) {
            System.out.println("\n[Daftar " + namaEntitas + "]");
            List<T> list = manager.getAll();
            if (list.isEmpty()) {
                System.out.println("-> Data " + namaEntitas + " masih kosong.");
            } else {
                for (T item : list) {
                    handler.displayItem(item);
                }
            }
            
        } else if (aksi.equals("3")) {
            System.out.print("\nMasukkan ID/Kode " + namaEntitas + " yang ingin diubah: ");
            String id = scanner.nextLine();
            T existingData = manager.getSingle(id);

            try {
                System.out.println("Masukkan data baru (Kosongi dan tekan Enter jika tidak ingin mengubah form tertentu):");
                T updatedData = handler.updateInput(scanner, existingData);
                manager.update(updatedData);
                System.out.println("Data berhasil diupdate!");
            } catch (DataNotFoundException e) {
                System.out.println("ALERT ERROR: " + e.getMessage());
            }
            
        } else if (aksi.equals("4")) {
            System.out.print("\nMasukkan ID/Kode " + namaEntitas + " yang dihapus: ");
            String id = scanner.nextLine();
            try {
                manager.delete(id);
            } catch (DataNotFoundException e) {
                System.out.println("ALERT ERROR: " + e.getMessage());
            }
            
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    private static DataFormHandler<Student> getStudentFormHandler() {
        return new DataFormHandler<Student>() {
            public Student createInput(Scanner sc) {
                System.out.print("NIS: "); String id = sc.nextLine();
                System.out.print("Nama: "); String nama = sc.nextLine();
                System.out.print("Alamat: "); String alamat = sc.nextLine();
                return new Student(id, nama, alamat);
            }
            public Student updateInput(Scanner sc, Student ex) {
                System.out.print("Nama [" + ex.getNama() + "]: "); String nama = sc.nextLine();
                System.out.print("Alamat [" + ex.getAlamat() + "]: "); String alamat = sc.nextLine();
                return new Student(
                    ex.getId(), 
                    nama.isEmpty() ? ex.getNama() : nama, 
                    alamat.isEmpty() ? ex.getAlamat() : alamat
                );
            }
            public void displayItem(Student item) {
                System.out.printf("NIS: %s | Nama: %-15s | Alamat: %s\n", item.getId(), item.getNama(), item.getAlamat());
            }
        };
    }

    private static DataFormHandler<Book> getBookFormHandler() {
        return new DataFormHandler<Book>() {
            public Book createInput(Scanner sc) {
                System.out.print("Kode Buku: "); String id = sc.nextLine();
                System.out.print("Judul: "); String judul = sc.nextLine();
                System.out.print("Jenis Buku: "); String jenis = sc.nextLine();
                return new Book(id, judul, jenis);
            }
            public Book updateInput(Scanner sc, Book ex) {
                System.out.print("Judul [" + ex.getJudul() + "]: "); String judul = sc.nextLine();
                System.out.print("Jenis Buku [" + ex.getJenis() + "]: "); String jenis = sc.nextLine();
                return new Book(
                    ex.getId(), 
                    judul.isEmpty() ? ex.getJudul() : judul, 
                    jenis.isEmpty() ? ex.getJenis() : jenis
                );
            }
            public void displayItem(Book item) {
                System.out.printf("Kode: %s | Judul: %-20s | Jenis: %s\n", item.getId(), item.getJudul(), item.getJenis());
            }
        };
    }

    private static DataFormHandler<Employee> getEmployeeFormHandler() {
        return new DataFormHandler<Employee>() {
            public Employee createInput(Scanner sc) {
                System.out.print("NIP: "); String id = sc.nextLine();
                System.out.print("Nama: "); String nama = sc.nextLine();
                System.out.print("Tgl Lahir (YYYY-MM-DD): "); String tgl = sc.nextLine();
                System.out.print("Password: "); String pass = sc.nextLine();
                return new Employee(id, nama, tgl, pass, false); // false artinya perlu di-hash
            }
            public Employee updateInput(Scanner sc, Employee ex) {
                System.out.print("Nama [" + ex.getNama() + "]: "); String nama = sc.nextLine();
                System.out.print("Tgl Lahir [" + ex.getTanggalLahir() + "]: "); String tgl = sc.nextLine();
                System.out.print("Password Baru (Kosongkan jika tidak diganti): "); String pass = sc.nextLine();
                
                boolean isPassKosong = pass.isEmpty();
                return new Employee(
                    ex.getId(),
                    nama.isEmpty() ? ex.getNama() : nama,
                    tgl.isEmpty() ? ex.getTanggalLahir() : tgl,
                    isPassKosong ? ex.getPasswordHash() : pass,
                    isPassKosong // Jika kosong, gunakan hash lama (true). Jika isi baru, hash lagi (false).
                );
            }
            public void displayItem(Employee item) {
                System.out.printf("NIP: %s | Nama: %-15s | Tgl Lahir: %s\n", item.getId(), item.getNama(), item.getTanggalLahir());
            }
        };
    }

    private static void menuTransaksi() {
        System.out.println("\n-- Transaksi --");
        System.out.println("1. Pinjam Buku");
        System.out.println("2. Kembalikan Buku");
        System.out.print("Pilih: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            prosesPeminjaman();
        } else if (choice.equals("2")) {
            System.out.print("Kode Transaksi: "); String txId = scanner.nextLine();
            Transaction tx = transactionMgr.getSingle(txId);
            try {
                tx.kembalikanBuku();
                transactionMgr.update(tx);
                System.out.println("Buku berhasil dikembalikan.");
            } catch(DataNotFoundException e) {
                System.out.println("ALERT ERROR: " + e.getMessage());
            }
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    private static void prosesPeminjaman() {
    try {
        System.out.print("NIS Siswa: "); 
        String nis = scanner.nextLine();
        if (studentMgr.getSingle(nis) == null) throw new DataNotFoundException("Siswa tidak ditemukan.");

        long pinjamAktif = transactionMgr.getAll().stream()
                .filter(t -> t.getNis().equals(nis) && t.getStatus() == 0)
                .count();

        if (pinjamAktif >= 2) {
            throw new BorrowLimitExceededException("Batas maksimal peminjaman (2 buku) telah tercapai untuk siswa ini.");
        }

        System.out.print("Kode Buku: "); 
        String kodeBuku = scanner.nextLine();
        if (bookMgr.getSingle(kodeBuku) == null) throw new DataNotFoundException("Buku tidak tersedia.");

        transactionMgr.create(new Transaction("TX"+System.currentTimeMillis(), nis, kodeBuku, LocalDate.now()));
        System.out.println("Peminjaman berhasil dicatat.");

    } catch (DataNotFoundException | BorrowLimitExceededException | DuplicateDataException e) {
        System.err.println("ALERT ERROR: " + e.getMessage());
    }
}

    private static void menuLaporan() {
        System.out.println("\n-- Laporan --");
        System.out.println("1. Buku Belum Dikembalikan");
        System.out.println("2. Siswa Terlambat Mengembalikan (Jatuh Tempo > 7 Hari)");
        System.out.println("3. Histori Semua Transaksi");
        System.out.print("Pilih: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.println("\n--- Buku Belum Dikembalikan ---");
            List<Transaction> belumKembali = transactionMgr.getAll().stream()
                    .filter(t -> t.getStatus() == 0)
                    .collect(Collectors.toList());
                    
            if (belumKembali.isEmpty()) {
                System.out.println("-> Saat ini tidak ada buku yang sedang dipinjam.");
            } else {
                belumKembali.forEach(t -> {
                    Book b = bookMgr.getSingle(t.getKodeBuku());
                    String judul = (b != null) ? b.getJudul() : "Buku Dihapus";
                    System.out.println("- TX: " + t.getId() + " | NIS: " + t.getNis() + " | Buku: " + judul);
                });
            }
            
        } else if (choice.equals("2")) {
            System.out.println("\n--- Siswa Terlambat / Jatuh Tempo ---");
            LocalDate hariIni = LocalDate.now();
            List<Transaction> terlambat = transactionMgr.getAll().stream()
                    .filter(t -> t.getStatus() == 0 && ChronoUnit.DAYS.between(t.getTglPinjam(), hariIni) > 7)
                    .collect(Collectors.toList());

            if (terlambat.isEmpty()) {
                System.out.println("-> Bagus! Tidak ada siswa yang terlambat mengembalikan buku.");
            } else {
                terlambat.forEach(t -> {
                    long hariPinjam = ChronoUnit.DAYS.between(t.getTglPinjam(), hariIni);
                    Student s = studentMgr.getSingle(t.getNis());
                    String nama = (s != null) ? s.getNama() : "Siswa Dihapus";
                    System.out.println("- NIS: " + t.getNis() + " (" + nama + ") | Telat: " + (hariPinjam - 7) + " hari");
                });
            }
            
        } else if (choice.equals("3")) {
            System.out.println("\n--- Histori Transaksi ---");
            List<Transaction> semuaTx = transactionMgr.getAll();
            
            if (semuaTx.isEmpty()) {
                System.out.println("-> Belum ada riwayat transaksi peminjaman di sistem.");
            } else {
                for(Transaction t : semuaTx) {
                    System.out.println(t.getId() + " | NIS: " + t.getNis() + " | Buku: " + t.getKodeBuku() + " | Status: " + (t.getStatus() == 0 ? "Dipinjam" : "Kembali"));
                }
            }
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }
}