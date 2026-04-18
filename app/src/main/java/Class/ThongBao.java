package Class;

public class ThongBao {
    private int id, khachHangId, daDoc;
    private String tieuDe, noiDung, thoiGian, loai;

    public ThongBao(int id, int khachHangId, String tieuDe,
                    String noiDung, String thoiGian,
                    int daDoc, String loai) {
        this.id = id;
        this.khachHangId = khachHangId;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
        this.daDoc = daDoc;
        this.loai = loai;
    }

    public int getId() { return id; }
    public int getKhachHangId() { return khachHangId; }
    public String getTieuDe() { return tieuDe; }
    public String getNoiDung() { return noiDung; }
    public String getThoiGian() { return thoiGian; }
    public boolean isDaDoc() { return daDoc == 1; }
    public String getLoai() { return loai; }
    public void setDaDoc(int daDoc) { this.daDoc = daDoc; }
}