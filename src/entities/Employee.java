package entities;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Employee extends BaseEntity {
    private String nip, nama, tanggalLahir, passwordHash;

    public Employee() {}
    public Employee(String nip, String nama, String tanggalLahir, String passwordHash, boolean isHash) {
        this.nip = nip; this.nama = nama; this.tanggalLahir = tanggalLahir;
        this.passwordHash = isHash ? passwordHash : hashPassword(passwordHash);
    }

    public String getId() { return nip; }
    public String getNama() { return nama; }
    public String getTanggalLahir() { return tanggalLahir; }
    public String getPasswordHash() { return passwordHash; }

    public String toDataString() { return nip + DELIMITER + nama + DELIMITER + tanggalLahir + DELIMITER + passwordHash; }
    public void fromDataString(String data) {
        String[] parts = data.split(DELIMITER);
        if (parts.length >= 4) { nip = parts[0]; nama = parts[1]; tanggalLahir = parts[2]; passwordHash = parts[3]; }
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}