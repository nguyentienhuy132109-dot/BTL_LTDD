package com.example.btl;

import android.content.Intent;
<<<<<<< HEAD
import android.content.SharedPreferences;
=======
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Class.DatabaseHelper;
import Class.Sach;
import Class.KhachHang;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ThongTinSanPhamActivity extends AppCompatActivity {

    DatabaseHelper db;
    Sach sachHienTai;
    int soLuong = 1;
<<<<<<< HEAD
    int khachHangId;

=======

    int khachHangId = 1;

    // Views - thông tin sách
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
    ImageView imgProductLarge;
    TextView tvProductNameDetail, tvPriceDetail;
    TextView tvMaHang, tvNhaCungCap, tvTacGia, tvNhaXuatBan, tvNamXuatBan;
    TextView tvProductDescription;
<<<<<<< HEAD
    TextView tvQuantity, tvStockStatus;
    Button btnMinus, btnPlus, btnAddToCart, btnBuyNow;
=======

    // Views - bottom action bar
    TextView tvQuantity, tvStockStatus;
    Button btnMinus, btnPlus, btnAddToCart, btnBuyNow;

    // RecyclerView đề xuất
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
    RecyclerView rvSachDeXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_tin_san_pham);

<<<<<<< HEAD
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        khachHangId = pref.getInt("userId", -1);
        if (khachHangId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        db = new DatabaseHelper(this);
        anhXaView();

        int sachId = getIntent().getIntExtra("SACH_ID", -1);
        sachHienTai = db.getSachById(sachId);
        
        if (sachHienTai != null) {
            hienThiThongTinSach(sachHienTai);
            hienThiDeXuat(sachHienTai);
        } else {
=======
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.thong_tin_san_pham), (v, insets) -> {
                    androidx.core.graphics.Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        db = new DatabaseHelper(this);

        // 1. Ánh xạ view
        anhXaView();

        // 2. Nhận SACH_ID từ Intent
        int sachId = getIntent().getIntExtra("SACH_ID", -1);
        if (sachId == -1) {
            Toast.makeText(this, "Không tìm thấy sách!", Toast.LENGTH_SHORT).show();
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            finish();
            return;
        }

<<<<<<< HEAD
        setupSoLuong();
        setupNutHanhDong();
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        setupBottomNav();
    }

=======
        // 3. Lấy sách từ DB và hiển thị
        sachHienTai = db.getSachById(sachId);
        if (sachHienTai != null) {
            hienThiThongTinSach(sachHienTai);
        } else {
            Toast.makeText(this, "Sách không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 4. Hiển thị sách đề xuất
        hienThiDeXuat(sachHienTai);

        // 5. Xử lý số lượng
        setupSoLuong();

        // 6. Xử lý nút hành động
        setupNutHanhDong();

        // 7. Nút back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // 8. Nút search → về trang chủ
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            Intent intent = new Intent(this, TrangChuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        setupBottomNav();
    }

    // ==================== ÁNH XẠ VIEW ====================
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
    private void anhXaView() {
        imgProductLarge      = findViewById(R.id.imgProductLarge);
        tvProductNameDetail  = findViewById(R.id.tvProductNameDetail);
        tvPriceDetail        = findViewById(R.id.tvPriceDetail);
        tvMaHang             = findViewById(R.id.tvMaHang);
        tvNhaCungCap         = findViewById(R.id.tvNhaCungCap);
        tvTacGia             = findViewById(R.id.tvTacGia);
        tvNhaXuatBan         = findViewById(R.id.tvNhaXuatBan);
        tvNamXuatBan         = findViewById(R.id.tvNamXuatBan);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvQuantity           = findViewById(R.id.tvQuantity);
        tvStockStatus        = findViewById(R.id.tvStockStatus);
        btnMinus             = findViewById(R.id.btnMinus);
        btnPlus              = findViewById(R.id.btnPlus);
        btnAddToCart         = findViewById(R.id.btnAddToCart);
        btnBuyNow            = findViewById(R.id.btnBuyNow);
        rvSachDeXuat         = findViewById(R.id.rvSachDeXuat);
    }

<<<<<<< HEAD
=======
    // ==================== HIỂN THỊ THÔNG TIN SÁCH ====================
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
    private void hienThiThongTinSach(Sach sach) {
        tvProductNameDetail.setText(sach.getTen());
        tvPriceDetail.setText(String.format("%,.0fđ", sach.getGia()));
        tvMaHang.setText(String.valueOf(sach.getId()));
<<<<<<< HEAD
        tvTacGia.setText(sach.getTacGia());
        tvNhaXuatBan.setText(sach.getTheLoai());
        tvProductDescription.setText(sach.getMoTa());

        String tenAnh = sach.getHinhAnh().trim();
        if (!tenAnh.isEmpty()) {
            int resId = getResources().getIdentifier(tenAnh, "drawable", getPackageName());
=======
        tvNhaCungCap.setText(sach.getTacGia());
        tvTacGia.setText(sach.getTacGia());
        tvNhaXuatBan.setText(sach.getTheLoai());
        tvNamXuatBan.setText("2024");
        tvProductDescription.setText(sach.getMoTa());

        // Set ảnh từ drawable
        String tenAnh = sach.getHinhAnh().trim();
        if (!tenAnh.isEmpty()) {
            int resId = getResources().getIdentifier(
                    tenAnh, "drawable", getPackageName());
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            if (resId != 0) imgProductLarge.setImageResource(resId);
        }
    }

<<<<<<< HEAD
    private void hienThiDeXuat(Sach sachHienTai) {
        List<Sach> dsDeXuat = db.getSachTheoTheLoai(sachHienTai.getTheLoai());
        dsDeXuat.removeIf(s -> s.getId() == sachHienTai.getId());
        rvSachDeXuat.setLayoutManager(new GridLayoutManager(this, 2));
        rvSachDeXuat.setAdapter(new SachDeXuatAdapter(dsDeXuat, sach -> {
=======
    // ==================== SÁCH ĐỀ XUẤT ====================
    private void hienThiDeXuat(Sach sachHienTai) {
        // Lấy sách cùng thể loại, bỏ sách hiện tại
        List<Sach> dsDeXuat = db.getSachTheoTheLoai(sachHienTai.getTheLoai());
        dsDeXuat.removeIf(s -> s.getId() == sachHienTai.getId());

        // Nếu không có → lấy tất cả
        if (dsDeXuat.isEmpty()) {
            dsDeXuat = db.getAllSach();
            dsDeXuat.removeIf(s -> s.getId() == sachHienTai.getId());
        }

        setupRecyclerView(dsDeXuat);
    }

    private void setupRecyclerView(List<Sach> dsSach) {
        rvSachDeXuat.setLayoutManager(new GridLayoutManager(this, 2));
        rvSachDeXuat.setAdapter(new SachDeXuatAdapter(dsSach, sach -> {
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            Intent intent = new Intent(this, ThongTinSanPhamActivity.class);
            intent.putExtra("SACH_ID", sach.getId());
            startActivity(intent);
        }));
    }

<<<<<<< HEAD
    private void setupSoLuong() {
        tvQuantity.setText(String.valueOf(soLuong));
=======
    // ==================== SỐ LƯỢNG ====================
    private void setupSoLuong() {
        tvQuantity.setText(String.valueOf(soLuong));

>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
        btnMinus.setOnClickListener(v -> {
            if (soLuong > 1) {
                soLuong--;
                tvQuantity.setText(String.valueOf(soLuong));
<<<<<<< HEAD
            }
        });
=======
            } else {
                Toast.makeText(this, "Số lượng tối thiểu là 1",
                        Toast.LENGTH_SHORT).show();
            }
        });

>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
        btnPlus.setOnClickListener(v -> {
            soLuong++;
            tvQuantity.setText(String.valueOf(soLuong));
        });
    }

<<<<<<< HEAD
    private void setupNutHanhDong() {
        btnAddToCart.setOnClickListener(v -> {
            db.themVaoGioHang(khachHangId, sachHienTai.getId(), soLuong);
            db.themThongBao(khachHangId, "Thêm vào giỏ hàng thành công!",
                    "Bạn vừa thêm " + soLuong + " cuốn \"" + sachHienTai.getTen() + "\" vào giỏ hàng.",
                    DatabaseHelper.LOAI_GIO_HANG);
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            List<int[]> dsSach = new ArrayList<>();
            dsSach.add(new int[]{sachHienTai.getId(), soLuong});
            DatHangDialog dialog = new DatHangDialog(this, khachHangId, sachHienTai.getGia() * soLuong, dsSach, (donHang, ds) -> {
                finish();
            });
=======
    // ==================== NÚT HÀNH ĐỘNG ====================
    private void setupNutHanhDong() {
        btnAddToCart.setOnClickListener(v -> {
            int khachHangId = 1;
            db.themVaoGioHang(khachHangId, sachHienTai.getId(), soLuong);

            // ← Phải có dòng này mới tạo thông báo
            db.themThongBao(khachHangId,
                    "Thêm vào giỏ hàng thành công!",
                    "Bạn vừa thêm " + soLuong + " cuốn \""
                            + sachHienTai.getTen() + "\" vào giỏ hàng.",
                    DatabaseHelper.LOAI_GIO_HANG);

            Toast.makeText(this, "Đã thêm vào giỏ hàng!",
                    Toast.LENGTH_SHORT).show();
        });

        // Sửa btnBuyNow
        btnBuyNow.setOnClickListener(v -> {
            // Tạo danh sách sách {sachId, soLuong}
            List<int[]> dsSach = new ArrayList<>();
            dsSach.add(new int[]{sachHienTai.getId(), soLuong});

            DatHangDialog dialog = new DatHangDialog(
                    this,
                    khachHangId,                    // ID khách hàng
                    sachHienTai.getGia() * soLuong, // tổng tiền
                    dsSach,
                    (donHang, ds) -> {
                        // Đặt hàng thành công → về trang chủ
                        Intent intent = new Intent(this, TrangChuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
            );
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            dialog.show();
        });
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, TrangChuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, GioHangActivity.class));
                return true;
<<<<<<< HEAD
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, ThongBaoActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
=======
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ThongBaoActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
               // startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
                return true;
            }
            return false;
        });
    }

<<<<<<< HEAD
    static class SachDeXuatAdapter extends RecyclerView.Adapter<SachDeXuatAdapter.ViewHolder> {
        interface OnClickSach { void onClick(Sach sach); }
        List<Sach> dsSach;
        OnClickSach listener;
=======
    // ==================== ADAPTER ĐỀ XUẤT ====================
    static class SachDeXuatAdapter extends RecyclerView.Adapter<SachDeXuatAdapter.ViewHolder> {

        interface OnClickSach { void onClick(Sach sach); }

        List<Sach> dsSach;
        OnClickSach listener;

>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
        SachDeXuatAdapter(List<Sach> dsSach, OnClickSach listener) {
            this.dsSach = dsSach;
            this.listener = listener;
        }
<<<<<<< HEAD
        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBia;
            TextView tvTen, tvGia;
=======

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBia;
            TextView tvTen, tvGia;

>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            ViewHolder(View itemView) {
                super(itemView);
                imgBia = itemView.findViewById(R.id.imgBiaSach);
                tvTen  = itemView.findViewById(R.id.tvTenSach);
                tvGia  = itemView.findViewById(R.id.tvGiaSach);
            }
        }
<<<<<<< HEAD
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach, parent, false);
            return new ViewHolder(view);
        }
=======

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sach, parent, false);
            return new ViewHolder(view);
        }

>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Sach sach = dsSach.get(position);
            holder.tvTen.setText(sach.getTen());
            holder.tvGia.setText(String.format("%,.0fđ", sach.getGia()));
<<<<<<< HEAD
            String tenAnh = sach.getHinhAnh().trim();
            if (!tenAnh.isEmpty()) {
                int resId = holder.itemView.getContext().getResources().getIdentifier(tenAnh, "drawable", holder.itemView.getContext().getPackageName());
                if (resId != 0) holder.imgBia.setImageResource(resId);
            }
            holder.itemView.setOnClickListener(v -> listener.onClick(sach));
        }
        @Override
        public int getItemCount() { return dsSach.size(); }
    }
=======

            // Set ảnh drawable
            String tenAnh = sach.getHinhAnh().trim();
            if (!tenAnh.isEmpty()) {
                int resId = holder.itemView.getContext().getResources()
                        .getIdentifier(tenAnh, "drawable",
                                holder.itemView.getContext().getPackageName());
                if (resId != 0) holder.imgBia.setImageResource(resId);
            }

            holder.itemView.setOnClickListener(v -> listener.onClick(sach));
        }


        @Override
        public int getItemCount() { return dsSach.size(); }
    }


>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
}