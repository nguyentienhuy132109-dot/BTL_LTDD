package Class;

public class ThongBao {
    private int id, khachHangId, daDoc, donHangId; // ← thêm donHangId
    private String tieuDe, noiDung, thoiGian, loai;

    // Constructor đầy đủ
    public ThongBao(int id, int khachHangId, String tieuDe,
                    String noiDung, String thoiGian,
                    int daDoc, String loai, int donHangId) { // ← thêm
        this.id = id;
        this.khachHangId = khachHangId;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
        this.daDoc = daDoc;
        this.loai = loai;
        this.donHangId = donHangId; // ← thêm
    }

    public int getId() { return id; }
    public int getKhachHangId() { return khachHangId; }
    public String getTieuDe() { return tieuDe; }
    public String getNoiDung() { return noiDung; }
    public String getThoiGian() { return thoiGian; }
    public boolean isDaDoc() { return daDoc == 1; }
    public String getLoai() { return loai; }
    public int getDonHangId() { return donHangId; } // ← thêm
    public void setDaDoc(int daDoc) { this.daDoc = daDoc; }
}