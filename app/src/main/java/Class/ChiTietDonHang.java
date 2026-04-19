package Class;

public class ChiTietDonHang {
    private int id, donHangId, sachId, soLuong;
    private double giaBan;

    // Constructor đầy đủ
    public ChiTietDonHang(int id, int donHangId, int sachId,
                          int soLuong, double giaBan) {
        this.id = id;
        this.donHangId = donHangId;
        this.sachId = sachId;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    // Constructor không id (khi thêm mới)
    public ChiTietDonHang(int donHangId, int sachId,
                          int soLuong, double giaBan) {
        this.donHangId = donHangId;
        this.sachId = sachId;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    // Getters
    public int getId() { return id; }
    public int getDonHangId() { return donHangId; }
    public int getSachId() { return sachId; }
    public int getSoLuong() { return soLuong; }
    public double getGiaBan() { return giaBan; }

    // Tính thành tiền
    public double getThanhTien() { return soLuong * giaBan; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}