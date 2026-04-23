package Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookShop.db";
    private static final int DATABASE_VERSION = 4;

    public static final String LOAI_GIO_HANG = "GIO_HANG";
    public static final String LOAI_HE_THONG = "HE_THONG";
    public static final String LOAI_DON_HANG = "DON_HANG";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng KhachHang
        db.execSQL("CREATE TABLE KhachHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT, " +
                "email TEXT UNIQUE, " +
                "sdt TEXT, " +
                "matKhau TEXT)");

        // Tạo bảng Sach
        db.execSQL("CREATE TABLE Sach (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT, " +
                "tacGia TEXT, " +
                "theLoai TEXT, " +
                "gia REAL, " +
                "hinhAnh TEXT, " +
                "moTa TEXT)");

        // Tạo bảng GioHang
        db.execSQL("CREATE TABLE GioHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "khachHangId INTEGER, " +
                "sachId INTEGER, " +
                "soLuong INTEGER, " +
                "FOREIGN KEY(khachHangId) REFERENCES KhachHang(id), " +
                "FOREIGN KEY(sachId) REFERENCES Sach(id))");

        // Tạo bảng DonHang
        db.execSQL("CREATE TABLE DonHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "khachHangId INTEGER, " +
                "ngayDat TEXT, " +
                "tongTien REAL, " +
                "trangThai TEXT, " +
                "diaChiGiao TEXT, " +
                "tenNguoiNhan TEXT, " +
                "sdtNguoiNhan TEXT, " +
                "FOREIGN KEY(khachHangId) REFERENCES KhachHang(id))");

        // Tạo bảng ChiTietDonHang
        db.execSQL("CREATE TABLE ChiTietDonHang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "donHangId INTEGER, " +
                "sachId INTEGER, " +
                "soLuong INTEGER, " +
                "giaBan REAL, " +
                "FOREIGN KEY(donHangId) REFERENCES DonHang(id), " +
                "FOREIGN KEY(sachId) REFERENCES Sach(id))");

        // Tạo bảng ThongBao
        db.execSQL("CREATE TABLE ThongBao (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "khachHangId INTEGER, " +
                "tieuDe TEXT, " +
                "noiDung TEXT, " +
                "thoiGian TEXT, " +
                "daDoc INTEGER DEFAULT 0, " +
                "loai TEXT, " +
                "donHangId INTEGER DEFAULT -1, " + // ← thêm dòng này
                "FOREIGN KEY(khachHangId) REFERENCES KhachHang(id))");

        // Thêm dữ liệu mẫu cho Sach
        // 1. Trinh thám
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Sherlock Holmes - Tuyển Tập'," +
                "'Arthur Conan Doyle'," +
                "'Trinh thám'," +
                "120000," +
                "'Tuyển tập đầy đủ những vụ án huyền thoại của thám tử lừng danh " +
                "Sherlock Holmes và trợ lý Watson. Kinh điển của dòng văn học trinh thám thế giới.'," +
                "'sherlock')");

        // 2. Thiếu nhi
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Dế Mèn Phiêu Lưu Ký'," +
                "'Tô Hoài'," +
                "'Thiếu nhi'," +
                "85000," +
                "'Cuộc phiêu lưu kỳ thú của chú dế mèn dũng cảm qua nhiều vùng đất lạ. " +
                "Tác phẩm kinh điển của văn học thiếu nhi Việt Nam.'," +
                "'de_men')");

        // 3. Tiểu thuyết
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Nhà Giả Kim'," +
                "'Paulo Coelho'," +
                "'Tiểu thuyết'," +
                "95000," +
                "'Hành trình của cậu bé chăn cừu Santiago đi tìm kho báu và khám phá " +
                "ý nghĩa cuộc sống. Cuốn sách truyền cảm hứng bán chạy nhất mọi thời đại.'," +
                "'nha_gia_kim')");

        // 4. Kinh doanh
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Nghĩ Giàu Làm Giàu'," +
                "'Napoleon Hill'," +
                "'Kinh doanh'," +
                "145000," +
                "'Bí quyết làm giàu từ việc nghiên cứu 500 triệu phú thành công nhất " +
                "nước Mỹ. Cuốn sách kinh doanh kinh điển được dịch ra hơn 70 ngôn ngữ.'," +
                "'nghi_giau_lam_giau')");

        // 5. Kinh dị
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Nó - It'," +
                "'Stephen King'," +
                "'Kinh dị'," +
                "210000," +
                "'Câu chuyện về nhóm trẻ em tại thị trấn Derry phải đối mặt với thực thể " +
                "quái dị mang hình dạng chú hề Pennywise đáng sợ. Kiệt tác kinh dị của Stephen King.'," +
                "'no_it')");

        // 6. Lịch sử
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Lược Sử Loài Người'," +
                "'Yuval Noah Harari'," +
                "'Lịch sử'," +
                "185000," +
                "'Hành trình 70.000 năm của loài người từ thời kỳ đồ đá đến kỷ nguyên " +
                "hiện đại. Cuốn sách lịch sử bán chạy nhất toàn cầu với góc nhìn hoàn toàn mới.'," +
                "'luoc_su_loai_nguoi')");

        // 7. Khoa học
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Vũ Trụ Trong Vỏ Hạt Dẻ'," +
                "'Stephen Hawking'," +
                "'Khoa học'," +
                "175000," +
                "'Stephen Hawking giải thích những lý thuyết vật lý phức tạp nhất về " +
                "vũ trụ bằng ngôn ngữ dễ hiểu. Từ thuyết tương đối đến cơ học lượng tử.'," +
                "'vu_tru_trong_vo_hat_de')");

        // 8. Văn học
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Truyện Kiều'," +
                "'Nguyễn Du'," +
                "'Văn học'," +
                "65000," +
                "'Kiệt tác văn học chữ Nôm của đại thi hào Nguyễn Du kể về cuộc đời " +
                "đầy bi kịch của Thúy Kiều. Đỉnh cao của văn học cổ điển Việt Nam.'," +
                "'truyen_kieu')");

        // 9. Ngoại ngữ
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'English Grammar In Use'," +
                "'Raymond Murphy'," +
                "'Ngoại ngữ'," +
                "195000," +
                "'Cuốn sách ngữ pháp tiếng Anh toàn diện và phổ biến nhất thế giới " +
                "với hơn 100 bài học kèm bài tập thực hành. Phù hợp cho trình độ trung cấp.'," +
                "'english_grammar_in_use')");

        // 1. Trinh thám - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Agatha Christie - Án Mạng Trên Sông Nile'," +
                "'Agatha Christie'," +
                "'Trinh thám'," +
                "110000," +
                "'Thám tử Hercule Poirot phải điều tra vụ án mạng xảy ra trên con tàu " +
                "du lịch sang trọng giữa dòng sông Nile huyền bí của Ai Cập.'," +
                "'an_mang_tren_song_nile')");

// 2. Thiếu nhi - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Hoàng Tử Bé'," +
                "'Antoine de Saint-Exupéry'," +
                "'Thiếu nhi'," +
                "75000," +
                "'Câu chuyện cảm động về cậu hoàng tử bé từ hành tinh nhỏ du hành " +
                "qua các hành tinh và học về tình yêu, tình bạn và ý nghĩa cuộc sống.'," +
                "'hoang_tu_be')");

// 3. Tiểu thuyết - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Bố Già'," +
                "'Mario Puzo'," +
                "'Tiểu thuyết'," +
                "135000," +
                "'Câu chuyện về gia tộc mafia Corleone quyền lực nhất nước Mỹ. " +
                "Kiệt tác văn học về quyền lực, lòng trung thành và sự phản bội trong thế giới ngầm.'," +
                "'bo_gia')");

// 4. Kinh doanh - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Khởi Nghiệp Tinh Gọn'," +
                "'Eric Ries'," +
                "'Kinh doanh'," +
                "160000," +
                "'Phương pháp khởi nghiệp hiện đại giúp các startup xây dựng sản phẩm " +
                "nhanh hơn, kiểm tra ý tưởng sớm hơn và tránh lãng phí nguồn lực.'," +
                "'khoi_nghiep_tinh_gon')");

// 5. Kinh dị - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Căn Phòng Kín'," +
                "'Keigo Higashino'," +
                "'Kinh dị'," +
                "125000," +
                "'Một vụ án bí ẩn xảy ra trong căn phòng khóa kín không lối thoát. " +
                "Thám tử tài ba phải tìm ra sự thật đằng sau những bí ẩn không thể lý giải.'," +
                "'can_phong_kin')");

// 6. Lịch sử - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Đất Rừng Phương Nam'," +
                "'Đoàn Giỏi'," +
                "'Lịch sử'," +
                "79000," +
                "'Câu chuyện về cậu bé An lưu lạc ở vùng đất phương Nam hoang dã " +
                "thời kháng chiến chống Pháp. Bức tranh sinh động về thiên nhiên và con người Nam Bộ.'," +
                "'dat_rung_phuong_nam')");

// 7. Khoa học - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Sapiens - Lược Sử Tư Tưởng'," +
                "'Yuval Noah Harari'," +
                "'Khoa học'," +
                "165000," +
                "'Khám phá cách tư duy của loài người đã định hình nền văn minh qua " +
                "các cuộc cách mạng nhận thức, nông nghiệp và khoa học trong suốt lịch sử.'," +
                "'sapiens_luoc_su_tu_tuong')");

// 8. Văn học - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Số Đỏ'," +
                "'Vũ Trọng Phụng'," +
                "'Văn học'," +
                "70000," +
                "'Tiểu thuyết trào phúng kinh điển của văn học hiện thực Việt Nam. " +
                "Câu chuyện về Xuân Tóc Đỏ từ kẻ bần hàn leo lên đỉnh cao xã hội thượng lưu.'," +
                "'so_do')");

// 9. Ngoại ngữ - bản ghi 2
        db.execSQL("INSERT INTO Sach (ten, tacGia, theLoai, gia, moTa, hinhAnh) VALUES (" +
                "'Tiếng Nhật Minna No Nihongo'," +
                "'3A Corporation'," +
                "'Ngoại ngữ'," +
                "220000," +
                "'Giáo trình tiếng Nhật chuẩn quốc tế được sử dụng rộng rãi nhất tại " +
                "Việt Nam và thế giới. Phù hợp cho người mới bắt đầu học tiếng Nhật.'," +
                "'minna_no_nihongo')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ThongBao");
        db.execSQL("DROP TABLE IF EXISTS ChiTietDonHang");
        db.execSQL("DROP TABLE IF EXISTS DonHang");
        db.execSQL("DROP TABLE IF EXISTS GioHang");
        db.execSQL("DROP TABLE IF EXISTS Sach");
        db.execSQL("DROP TABLE IF EXISTS KhachHang");
        onCreate(db);
    }

    // ==================== KHÁCH HÀNG ====================
    public long dangKy(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", kh.getTen());
        values.put("email", kh.getEmail());
        values.put("sdt", kh.getSdt());
        values.put("matKhau", kh.getMatKhau());
        return db.insert("KhachHang", null, values);
    }

    public long themKhachHang(KhachHang kh) {
        return dangKy(kh);
    }

    public KhachHang dangNhap(String email, String matKhau) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM KhachHang WHERE email=? AND matKhau=?", new String[]{email, matKhau});
        if (cursor.moveToFirst()) {
            // Thứ tự cột: 0:id, 1:ten, 2:email, 3:sdt, 4:matKhau (theo onCreate)
            KhachHang kh = new KhachHang(
                    cursor.getInt(0), 
                    cursor.getString(1), 
                    cursor.getString(2), 
                    cursor.getString(4), // matKhau
                    cursor.getString(3)  // sdt
            );
            cursor.close();
            return kh;
        }
        cursor.close();
        return null;
    }

    public KhachHang getKhachHangById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM KhachHang WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            // Thứ tự cột: 0:id, 1:ten, 2:email, 3:sdt, 4:matKhau
            KhachHang kh = new KhachHang(
                    cursor.getInt(0), 
                    cursor.getString(1), 
                    cursor.getString(2), 
                    cursor.getString(4), // matKhau
                    cursor.getString(3)  // sdt
            );
            cursor.close();
            return kh;
        }
        cursor.close();
        return null;
    }

    // ==================== SÁCH ====================
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
                    cursor.getString(6),
                    cursor.getString(5),
                    cursor.getDouble(4)
            ));
        }
        cursor.close();
        return dsSach;
    }

    public Sach getSachById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Sach WHERE id=?",
                new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Sach s = new Sach(
                    cursor.getInt(0),    // id
                    cursor.getString(1), // ten
                    cursor.getString(2), // tacGia
                    cursor.getString(3), // theLoai
                    cursor.getString(6), // moTa    ← đổi từ 5 thành 6
                    cursor.getString(5), // hinhAnh ← đổi từ 6 thành 5
                    cursor.getDouble(4)  // gia
            );
            cursor.close();
            return s;
        }
        cursor.close();
        return null;
    }

    public List<Sach> getSachTheoTheLoai(String theLoai) {
        List<Sach> dsSach = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Sach WHERE theLoai=?", new String[]{theLoai});
        while (cursor.moveToNext()) {
            dsSach.add(new Sach(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(6), // moTa    ← 6
                    cursor.getString(5), // hinhAnh ← 5
                    cursor.getDouble(4)
            ));
        }
        cursor.close();
        return dsSach;
    }

    public List<Sach> timKiemSach(String tuKhoa) {
        List<Sach> dsSach = new ArrayList<>();
        List<Sach> tatCa = getAllSach();

        String tuKhoaKhongDau = boGangDau(tuKhoa).toLowerCase();

        for (Sach sach : tatCa) {
            String tenKhongDau = boGangDau(sach.getTen()).toLowerCase();
            String tacGiaKhongDau = boGangDau(sach.getTacGia()).toLowerCase();

            if (tenKhongDau.contains(tuKhoaKhongDau) ||
                    tacGiaKhongDau.contains(tuKhoaKhongDau)) {
                dsSach.add(sach);
            }
        }
        return dsSach;
    }

    // Hàm bỏ dấu tiếng Việt
    private String boGangDau(String str) {
        if (str == null) return "";
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[ÌÍỊỈĨ]", "I");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("[ỲÝỴỶỸ]", "Y");
        str = str.replaceAll("[đ]", "d");
        str = str.replaceAll("[Đ]", "D");
        return str;
    }

    // ==================== GIỎ HÀNG ====================
    public void themVaoGioHang(int khachHangId, int sachId, int soLuong) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, soLuong FROM GioHang WHERE khachHangId=? AND sachId=?", new String[]{String.valueOf(khachHangId), String.valueOf(sachId)});
        if (cursor.moveToFirst()) {
            int soLuongCu = cursor.getInt(1);
            int idRow = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("soLuong", soLuongCu + soLuong);
            db.update("GioHang", values, "id=?", new String[]{String.valueOf(idRow)});
        } else {
            ContentValues values = new ContentValues();
            values.put("khachHangId", khachHangId);
            values.put("sachId", sachId);
            values.put("soLuong", soLuong);
            db.insert("GioHang", null, values);
        }
        cursor.close();
    }

    public List<GioHangItem> getGioHang(int khachHangId) {
        List<GioHangItem> ds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gh.id, gh.sachId, s.ten, s.hinhAnh, s.gia, gh.soLuong FROM GioHang gh JOIN Sach s ON gh.sachId = s.id WHERE gh.khachHangId = ?", new String[]{String.valueOf(khachHangId)});
        while (cursor.moveToNext()) {
            ds.add(new GioHangItem(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getInt(5)));
        }
        cursor.close();
        return ds;
    }

    public void capNhatSoLuong(int id, int soLuong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soLuong", soLuong);
        db.update("GioHang", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void xoaKhoiGioHang(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("GioHang", "id=?", new String[]{String.valueOf(id)});
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
        Cursor cursor = db.rawQuery("SELECT * FROM DonHang WHERE khachHangId=? ORDER BY id DESC", new String[]{String.valueOf(khachHangId)});
        while (cursor.moveToNext()) {
            dsDonHang.add(new DonHang(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
        }
        cursor.close();
        return dsDonHang;
    }

    public long themChiTietDonHang(ChiTietDonHang ct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("donHangId", ct.getDonHangId());
        values.put("sachId", ct.getSachId());
        values.put("soLuong", ct.getSoLuong());
        values.put("giaBan", ct.getGiaBan());
        return db.insert("ChiTietDonHang", null, values);
    }

    public List<ChiTietModel> getChiTietDonHangFull(int donHangId) {
        List<ChiTietModel> ds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT ct.*, s.ten, s.hinhAnh FROM ChiTietDonHang ct JOIN Sach s ON ct.sachId = s.id WHERE ct.donHangId = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(donHangId)});
        while (cursor.moveToNext()) {
            ds.add(new ChiTietModel(cursor.getString(cursor.getColumnIndexOrThrow("ten")), cursor.getString(cursor.getColumnIndexOrThrow("hinhAnh")), cursor.getInt(cursor.getColumnIndexOrThrow("soLuong")), cursor.getDouble(cursor.getColumnIndexOrThrow("giaBan"))));
        }
        cursor.close();
        return ds;
    }

    public static class ChiTietModel {
        public String tenSach, hinhAnh;
        public int soLuong;
        public double giaBan;
        public ChiTietModel(String tenSach, String hinhAnh, int soLuong, double giaBan) {
            this.tenSach = tenSach;
            this.hinhAnh = hinhAnh;
            this.soLuong = soLuong;
            this.giaBan = giaBan;
        }
    }

    // ==================== THÔNG BÁO ====================
    public void themThongBao(int khachHangId, String tieuDe, String noiDung,
                             String loai, int donHangId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("khachHangId", khachHangId);
        values.put("tieuDe", tieuDe);
        values.put("noiDung", noiDung);
        values.put("loai", loai);
        values.put("daDoc", 0);
        values.put("donHangId", donHangId); // ← THÊM DÒNG NÀY
        values.put("thoiGian", new java.text.SimpleDateFormat(
                "dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                .format(new java.util.Date()));
        db.insert("ThongBao", null, values);
    }

    public List<ThongBao> getThongBao(int khachHangId) {
        List<ThongBao> ds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ThongBao WHERE khachHangId=? ORDER BY id DESC", new String[]{String.valueOf(khachHangId)});
        while (cursor.moveToNext()) {
            ds.add(new ThongBao(
                    cursor.getInt(0),    // id
                    cursor.getInt(1),    // khachHangId
                    cursor.getString(2), // tieuDe
                    cursor.getString(3), // noiDung
                    cursor.getString(4), // thoiGian
                    cursor.getInt(5),    // daDoc
                    cursor.getString(6), // loai
                    cursor.getInt(7)     // donHangId ← phải có dòng này
            ));
        }
        cursor.close();
        return ds;
    }

    public void danhDauDaDoc(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("daDoc", 1);
        db.update("ThongBao", values, "id=?", new String[]{String.valueOf(id)});
    }
}