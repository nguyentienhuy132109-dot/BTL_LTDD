package Class;

public class DonHang {
    private int id, khachHangId;
    private String ngayDat, trangThai, diaChiGiao;
    private String tenNguoiNhan, sdtNguoiNhan; // ← thêm 2 trường này
    private double tongTien;

    public static final String TRANG_THAI_CHO = "Chờ xác nhận";
    public static final String TRANG_THAI_DANG_GIAO = "Đang giao";
    public static final String TRANG_THAI_HOAN_THANH = "Hoàn thành";
    public static final String TRANG_THAI_HUY = "Đã hủy";

    // Constructor đầy đủ (đọc từ DB)
    public DonHang(int id, int khachHangId, String ngayDat, double tongTien,
                   String trangThai, String diaChiGiao,
                   String tenNguoiNhan, String sdtNguoiNhan) {
        this.id = id;
        this.khachHangId = khachHangId;
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.diaChiGiao = diaChiGiao;
        this.tenNguoiNhan = tenNguoiNhan;
        this.sdtNguoiNhan = sdtNguoiNhan;
    }

    // Constructor không id (khi khách vừa đặt hàng)
    public DonHang(int khachHangId, String ngayDat, double tongTien,
                   String diaChiGiao, String tenNguoiNhan, String sdtNguoiNhan) {
        this.khachHangId = khachHangId;
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.diaChiGiao = diaChiGiao;
        this.tenNguoiNhan = tenNguoiNhan;
        this.sdtNguoiNhan = sdtNguoiNhan;
        this.trangThai = TRANG_THAI_CHO;
    }

    // Getters
    public int getId() { return id; }
    public int getKhachHangId() { return khachHangId; }
    public String getNgayDat() { return ngayDat; }
    public double getTongTien() { return tongTien; }
    public String getTrangThai() { return trangThai; }
    public String getDiaChiGiao() { return diaChiGiao; }
    public String getTenNguoiNhan() { return tenNguoiNhan; }
    public String getSdtNguoiNhan() { return sdtNguoiNhan; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public void setDiaChiGiao(String diaChiGiao) { this.diaChiGiao = diaChiGiao; }
    public void setTenNguoiNhan(String tenNguoiNhan) { this.tenNguoiNhan = tenNguoiNhan; }
    public void setSdtNguoiNhan(String sdtNguoiNhan) { this.sdtNguoiNhan = sdtNguoiNhan; }
}