package Class;

public class KhachHang {
    private int id;
    private String ten, email, matKhau, sdt, diaChi;

    // Constructor đầy đủ
    public KhachHang(int id, String ten, String email,
                     String matKhau, String sdt, String diaChi) {
        this.id = id;
        this.ten = ten;
        this.email = email;
        this.matKhau = matKhau;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    // Constructor không id (khi đăng ký)
    public KhachHang(String ten, String email,
                     String matKhau, String sdt, String diaChi) {
        this.ten = ten;
        this.email = email;
        this.matKhau = matKhau;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    // Getters
    public int getId() { return id; }
    public String getTen() { return ten; }
    public String getEmail() { return email; }
    public String getMatKhau() { return matKhau; }
    public String getSdt() { return sdt; }
    public String getDiaChi() { return diaChi; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}