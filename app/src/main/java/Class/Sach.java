package Class;

public class Sach {
    private int id;
    private String ten, tacGia, theLoai, moTa, hinhAnh;
    private double gia;

    // Constructor đầy đủ
    public Sach(int id, String ten, String tacGia, String theLoai,
                String moTa, String hinhAnh, double gia) {
        this.id = id;
        this.ten = ten;
        this.tacGia = tacGia;
        this.theLoai = theLoai;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
    }

    // Constructor không id (khi thêm mới vào DB)
    public Sach(String ten, String tacGia, String theLoai,
                String moTa, String hinhAnh, double gia) {
        this.ten = ten;
        this.tacGia = tacGia;
        this.theLoai = theLoai;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
    }

    // Getters
    public int getId() { return id; }
    public String getTen() { return ten; }
    public String getTacGia() { return tacGia; }
    public String getTheLoai() { return theLoai; }
    public String getMoTa() { return moTa; }
    public String getHinhAnh() { return hinhAnh; }
    public double getGia() { return gia; }

    // Setters (chỉ những gì khách có thể cần)
    public void setId(int id) { this.id = id; }
}