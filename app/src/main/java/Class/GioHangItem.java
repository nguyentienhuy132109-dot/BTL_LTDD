package Class;

public class GioHangItem {
    private int id, sachId, soLuong;
    private String tenSach, hinhAnh;
    private double gia;

    // Thêm vào class GioHangItem
    private boolean isChon = false;

    public boolean isChon() { return isChon; }
    public void setChon(boolean chon) { isChon = chon; }

    public GioHangItem(int id, int sachId, String tenSach,
                       String hinhAnh, double gia, int soLuong) {
        this.id = id;
        this.sachId = sachId;
        this.tenSach = tenSach;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public int getId() { return id; }
    public int getSachId() { return sachId; }
    public String getTenSach() { return tenSach; }
    public String getHinhAnh() { return hinhAnh; }
    public double getGia() { return gia; }
    public int getSoLuong() { return soLuong; }
    public double getThanhTien() { return gia * soLuong; }

    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}