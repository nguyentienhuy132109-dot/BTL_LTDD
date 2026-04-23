package com.example.btl;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Class.DatabaseHelper;

public class ChiTietDonHangActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView tvTen, tvSdt, tvDiaChi, tvMaDon, tvNgayDat, tvTrangThai, tvTongTien;
    RecyclerView rvSanhSach;
    Button btnChinhSua, btnHuyDon;
    int donHangId;
    String trangThaiHienTai; // lưu trạng thái để kiểm tra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        db = new DatabaseHelper(this);
        donHangId = getIntent().getIntExtra("DON_HANG_ID", -1);

        anhXa();
        loadDuLieu();
        setupNutHanhDong();
    }

    private void anhXa() {
        tvTen       = findViewById(R.id.tvTenNguoiNhan);
        tvSdt       = findViewById(R.id.tvSdtNguoiNhan);
        tvDiaChi    = findViewById(R.id.tvDiaChiNhan);
        tvMaDon     = findViewById(R.id.tvMaDonHangDetail);
        tvNgayDat   = findViewById(R.id.tvNgayDatDetail);
        tvTrangThai = findViewById(R.id.tvTrangThaiDetail);
        tvTongTien  = findViewById(R.id.tvTongTienDetail);
        rvSanhSach  = findViewById(R.id.rvChiTietSanPhan);
        btnChinhSua = findViewById(R.id.btnChinhSua);
        btnHuyDon   = findViewById(R.id.btnHuyDon);
    }

    private void loadDuLieu() {
        SQLiteDatabase database = db.getReadableDatabase();
        android.database.Cursor cursor = database.rawQuery(
                "SELECT * FROM DonHang WHERE id = ?",
                new String[]{String.valueOf(donHangId)});

        if (cursor != null && cursor.moveToFirst()) {
            trangThaiHienTai = cursor.getString(
                    cursor.getColumnIndexOrThrow("trangThai"));

            tvTen.setText("Người nhận: " + cursor.getString(
                    cursor.getColumnIndexOrThrow("tenNguoiNhan")));
            tvSdt.setText("Số điện thoại: " + cursor.getString(
                    cursor.getColumnIndexOrThrow("sdtNguoiNhan")));
            tvDiaChi.setText("Địa chỉ: " + cursor.getString(
                    cursor.getColumnIndexOrThrow("diaChiGiao")));
            tvMaDon.setText("#" + cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")));
            tvNgayDat.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow("ngayDat")));
            tvTrangThai.setText(trangThaiHienTai);
            tvTongTien.setText(String.format("%,.0fđ",
                    cursor.getDouble(cursor.getColumnIndexOrThrow("tongTien"))));
            cursor.close();

            // Ẩn nút nếu đơn đã hoàn thành hoặc đã hủy
            if (trangThaiHienTai.equals("Hoàn thành") ||
                    trangThaiHienTai.equals("Đã hủy")) {
                btnChinhSua.setEnabled(false);
                btnChinhSua.setAlpha(0.5f);
                btnHuyDon.setEnabled(false);
                btnHuyDon.setAlpha(0.5f);
            }
        }

        List<DatabaseHelper.ChiTietModel> dsChiTiet =
                db.getChiTietDonHangFull(donHangId);
        rvSanhSach.setLayoutManager(new LinearLayoutManager(this));
        rvSanhSach.setAdapter(new ChiTietAdapter(dsChiTiet));
    }

    // ==================== NÚT HÀNH ĐỘNG ====================
    private void setupNutHanhDong() {

        // Nút chỉnh sửa → dialog nhập thông tin mới
        btnChinhSua.setOnClickListener(v -> hienDialogChinhSua());

        // Nút hủy đơn → dialog xác nhận
        btnHuyDon.setOnClickListener(v -> hienDialogHuyDon());
    }

    // ==================== DIALOG CHỈNH SỬA ====================
    private void hienDialogChinhSua() {
        // Lấy thông tin hiện tại
        String tenHienTai   = tvTen.getText().toString()
                .replace("Người nhận: ", "");
        String sdtHienTai   = tvSdt.getText().toString()
                .replace("Số điện thoại: ", "");
        String diaChiHienTai = tvDiaChi.getText().toString()
                .replace("Địa chỉ: ", "");

        // Tạo view cho dialog
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_chinh_sua_don_hang, null);

        EditText edtTen    = dialogView.findViewById(R.id.edtTenChinhSua);
        EditText edtSdt    = dialogView.findViewById(R.id.edtSdtChinhSua);
        EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChiChinhSua);

        // Điền thông tin cũ vào
        edtTen.setText(tenHienTai);
        edtSdt.setText(sdtHienTai);
        edtDiaChi.setText(diaChiHienTai);

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa thông tin đơn hàng")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String tenMoi    = edtTen.getText().toString().trim();
                    String sdtMoi    = edtSdt.getText().toString().trim();
                    String diaChiMoi = edtDiaChi.getText().toString().trim();

                    if (tenMoi.isEmpty() || sdtMoi.isEmpty() || diaChiMoi.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cập nhật DB
                    SQLiteDatabase database = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("tenNguoiNhan", tenMoi);
                    values.put("sdtNguoiNhan", sdtMoi);
                    values.put("diaChiGiao", diaChiMoi);
                    database.update("DonHang", values,
                            "id=?", new String[]{String.valueOf(donHangId)});

                    // Cập nhật UI
                    tvTen.setText("Người nhận: " + tenMoi);
                    tvSdt.setText("Số điện thoại: " + sdtMoi);
                    tvDiaChi.setText("Địa chỉ: " + diaChiMoi);

                    Toast.makeText(this, "Cập nhật thành công!",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ==================== DIALOG HỦY ĐƠN ====================
    private void hienDialogHuyDon() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc muốn hủy đơn hàng #" + donHangId + " không?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    // Cập nhật trạng thái → Đã hủy
                    SQLiteDatabase database = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("trangThai", "Đã hủy");
                    database.update("DonHang", values,
                            "id=?", new String[]{String.valueOf(donHangId)});

                    // Cập nhật UI
                    tvTrangThai.setText("Đã hủy");
                    tvTrangThai.setTextColor(0xFFE53935);

                    // Disable 2 nút
                    btnChinhSua.setEnabled(false);
                    btnChinhSua.setAlpha(0.5f);
                    btnHuyDon.setEnabled(false);
                    btnHuyDon.setAlpha(0.5f);

                    Toast.makeText(this, "Đã hủy đơn hàng #" + donHangId,
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    // ==================== ADAPTER ====================
    class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.ViewHolder> {
        List<DatabaseHelper.ChiTietModel> list;
        ChiTietAdapter(List<DatabaseHelper.ChiTietModel> list) { this.list = list; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chi_tiet_sach, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DatabaseHelper.ChiTietModel item = list.get(position);
            holder.tvTen.setText(item.tenSach);
            holder.tvSoLuong.setText("Số lượng: " + item.soLuong);
            holder.tvGia.setText(String.format("%,.0fđ", item.giaBan));

            int resId = getResources().getIdentifier(
                    item.hinhAnh, "drawable", getPackageName());
            if (resId != 0) holder.img.setImageResource(resId);
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView tvTen, tvSoLuong, tvGia;
            ViewHolder(View itemView) {
                super(itemView);
                img       = itemView.findViewById(R.id.imgSachDetail);
                tvTen     = itemView.findViewById(R.id.tvTenSachDetail);
                tvSoLuong = itemView.findViewById(R.id.tvSoLuongDetail);
                tvGia     = itemView.findViewById(R.id.tvGiaDetail);
            }
        }
    }
}