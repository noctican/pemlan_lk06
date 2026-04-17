package src.entities;

public class Employee extends BaseEntity {
    private String nip, nama, tanggalLahir, passwordHash;

    public Employee() {}
    public Employee(String nip, String nama, String tanggalLahir, String passwordHash, boolean isHash) {}
}