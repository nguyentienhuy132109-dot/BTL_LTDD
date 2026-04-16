package Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BanSach.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng KhachHang
        db.execSQL("CREATE TABLE KhachHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ten TEXT," +
                "email TEXT UNIQUE," +
                "matKhau TEXT," +
                "sdt TEXT)");

        // Tạo bảng Sach
        db.execSQL("CREATE TABLE Sach (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ten TEXT," +
                "tacGia TEXT," +
                "theLoai TEXT," +
                "gia REAL," +
                "moTa TEXT," +
                "hinhAnh TEXT)");

        // Tạo bảng DonHang
        db.execSQL("CREATE TABLE DonHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "khachHangId INTEGER," +
                "ngayDat TEXT," +
                "tongTien REAL," +
                "trangThai TEXT," +
                "diaChiGiao TEXT," +
                "tenNguoiNhan TEXT," +
                "sdtNguoiNhan TEXT," +
                "FOREIGN KEY(khachHangId) REFERENCES KhachHang(id))");

        // Tạo bảng ChiTietDonHang
        db.execSQL("CREATE TABLE ChiTietDonHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "donHangId INTEGER," +
                "sachId INTEGER," +
                "soLuong INTEGER," +
                "giaBan REAL," +
                "FOREIGN KEY(donHangId) REFERENCES DonHang(id)," +
                "FOREIGN KEY(sachId) REFERENCES Sach(id))");

        // Thêm dữ liệu mẫu
        themDuLieuMau(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ChiTietDonHang");
        db.execSQL("DROP TABLE IF EXISTS DonHang");
        db.execSQL("DROP TABLE IF EXISTS Sach");
        db.execSQL("DROP TABLE IF EXISTS KhachHang");
        onCreate(db);
    }

    // ==================== DỮ LIỆU MẪU ====================
    private void themDuLieuMau(SQLiteDatabase db) {

        // ---- KHÁCH HÀNG ----
        db.execSQL("INSERT INTO KhachHang (ten, email, matKhau, sdt) VALUES " +
                "('Nguyen Van A', 'a@gmail.com', '123456', '0901234567')");
        db.execSQL("INSERT INTO KhachHang (ten, email, matKhau, sdt) VALUES " +
                "('Tran Thi B', 'b@gmail.com', '123456', '0912345678')");

        // ---- SÁCH ----
        // ⚠️ Điền tên file ảnh vào chỗ trống (không cần đuôi .png/.jpg)
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES " +
                "('Dế Mèn Phiêu Lưu Ký', 'Tô Hoài', 'Thiếu nhi', 85000, " +
                "'Truyện thiếu nhi kinh điển của văn học Việt Nam', " +
                "'          ')");  // ← điền tên drawable vào đây

        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES " +
                "('Sherlock Holmes', 'Arthur Conan Doyle', 'Trinh thám', 120000, " +
                "'Tuyển tập truyện trinh thám nổi tiếng thế giới', " +
                "'          ')");  // ← điền tên drawable vào đây

        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES " +
                "('Lịch Sử Thế Giới', 'Nhiều tác giả', 'Lịch sử', 210000, " +
                "'Tổng hợp lịch sử thế giới từ cổ đại đến hiện đại', " +
                "'          ')");  // ← điền tên drawable vào đây

        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES " +
                "('Kinh Doanh 4.0', 'Nguyen Van B', 'Kinh doanh', 155000, " +
                "'Kinh doanh trong thời đại công nghệ số', " +
                "'          ')");  // ← điền tên drawable vào đây

        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES " +
                "('Nhà Giả Kim', 'Paulo Coelho', 'Tiểu thuyết', 95000, " +
                "'Hành trình tìm kiếm kho báu và ý nghĩa cuộc sống', " +
                "'          ')");  // ← điền tên drawable vào đây

        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES " +
                "('Đắc Nhân Tâm', 'Dale Carnegie', 'Kỹ năng sống', 110000, " +
                "'Nghệ thuật thu phục lòng người', " +
                "'          ')");  // ← điền tên drawable vào đây
    }

    // ==================== KHÁCH HÀNG ====================
    public long themKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", kh.getTen());
        values.put("email", kh.getEmail());
        values.put("matKhau", kh.getMatKhau());
        values.put("sdt", kh.getSdt());
        return db.insert("KhachHang", null, values);
    }

    public KhachHang dangNhap(String email, String matKhau) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM KhachHang WHERE email=? AND matKhau=?",
                new String[]{email, matKhau});
        if (cursor.moveToFirst()) {
            return new KhachHang(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        cursor.close();
        return null;
    }

    public KhachHang getKhachHangById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM KhachHang WHERE id=?",
                new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            return new KhachHang(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        cursor.close();
        return null;
    }

    // ==================== SÁCH ====================
    public Sach getSachById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Sach WHERE id=?",
                new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            return new Sach(
                    cursor.getInt(0),    // id
                    cursor.getString(1), // ten
                    cursor.getString(2), // tacGia
                    cursor.getString(3), // theLoai
                    cursor.getString(5), // moTa
                    cursor.getString(6), // hinhAnh
                    cursor.getDouble(4)  // gia
            );
        }
        cursor.close();
        return null;
    }

    public List<Sach> getAllSach() {
        List<Sach> dsSach = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Sach", null);
        while (cursor.moveToNext()) {
            dsSach.add(new Sach(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(4)
            ));
        }
        cursor.close();
        return dsSach;
    }

    public List<Sach> getSachTheoTheLoai(String theLoai) {
        List<Sach> dsSach = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Sach WHERE theLoai=?",
                new String[]{theLoai});
        while (cursor.moveToNext()) {
            dsSach.add(new Sach(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(4)
            ));
        }
        cursor.close();
        return dsSach;
    }

    // ==================== ĐƠN HÀNG ====================
    public long themDonHang(DonHang dh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("khachHangId", dh.getKhachHangId());
        values.put("ngayDat", dh.getNgayDat());
        values.put("tongTien", dh.getTongTien());
        values.put("trangThai", dh.getTrangThai());
        values.put("diaChiGiao", dh.getDiaChiGiao());
        values.put("tenNguoiNhan", dh.getTenNguoiNhan());
        values.put("sdtNguoiNhan", dh.getSdtNguoiNhan());
        return db.insert("DonHang", null, values);
    }

    public List<DonHang> getDonHangByKhachHang(int khachHangId) {
        List<DonHang> dsDonHang = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM DonHang WHERE khachHangId=? ORDER BY id DESC",
                new String[]{String.valueOf(khachHangId)});
        while (cursor.moveToNext()) {
            dsDonHang.add(new DonHang(
                    cursor.getInt(0),    // id
                    cursor.getInt(1),    // khachHangId
                    cursor.getString(2), // ngayDat
                    cursor.getDouble(3), // tongTien
                    cursor.getString(4), // trangThai
                    cursor.getString(5), // diaChiGiao
                    cursor.getString(6), // tenNguoiNhan
                    cursor.getString(7)  // sdtNguoiNhan
            ));
        }
        cursor.close();
        return dsDonHang;
    }

    // ==================== CHI TIẾT ĐƠN HÀNG ====================
    public long themChiTietDonHang(ChiTietDonHang ct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("donHangId", ct.getDonHangId());
        values.put("sachId", ct.getSachId());
        values.put("soLuong", ct.getSoLuong());
        values.put("giaBan", ct.getGiaBan());
        return db.insert("ChiTietDonHang", null, values);
    }

    // Lấy chi tiết đơn hàng kèm tên sách (dùng cho giỏ hàng)
    public List<ChiTietDonHang> getChiTietDonHang(int donHangId) {
        List<ChiTietDonHang> dsChiTiet = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM ChiTietDonHang WHERE donHangId=?",
                new String[]{String.valueOf(donHangId)});
        while (cursor.moveToNext()) {
            dsChiTiet.add(new ChiTietDonHang(
                    cursor.getInt(0),    // id
                    cursor.getInt(1),    // donHangId
                    cursor.getInt(2),    // sachId
                    cursor.getInt(3),    // soLuong
                    cursor.getDouble(4)  // giaBan
            ));
        }
        cursor.close();
        return dsChiTiet;
    }

    // Lấy tên sách theo sachId (dùng để hiển thị trong giỏ hàng)
    public String getTenSachById(int sachId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ten FROM Sach WHERE id=?",
                new String[]{String.valueOf(sachId)});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return "";
    }
}