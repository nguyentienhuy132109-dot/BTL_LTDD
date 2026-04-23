package com.example.btl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ThongTinSanPhamActivity extends AppCompatActivity {

    DatabaseHelper db;
    Sach sachHienTai;
    int soLuong = 1;
    int khachHangId;

    ImageView imgProductLarge;
    TextView tvProductNameDetail, tvPriceDetail;
    TextView tvMaHang, tvNhaCungCap, tvTacGia, tvNhaXuatBan, tvNamXuatBan;
    TextView tvProductDescription;
    TextView tvQuantity, tvStockStatus;
    Button btnMinus, btnPlus, btnAddToCart, btnBuyNow;
    RecyclerView rvSachDeXuat;

    RecyclerView rvDanhSachSach; // ← thêm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_tin_san_pham);

        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        khachHangId = pref.getInt("userId", -1);
        if (khachHangId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.thong_tin_san_pham), (v, insets) -> {
                    androidx.core.graphics.Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        db = new DatabaseHelper(this);
        anhXaView();

        int sachId = getIntent().getIntExtra("SACH_ID", -1);
        sachHienTai = db.getSachById(sachId);
        
        if (sachHienTai != null) {
            hienThiThongTinSach(sachHienTai);
            hienThiDeXuat(sachHienTai);
        } else {
            Toast.makeText(this, "Không tìm thấy sách!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupSoLuong();
        setupNutHanhDong();
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            Intent intent = new Intent(this, TrangChuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        setupBottomNav();
    }

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
        rvDanhSachSach = findViewById(R.id.rvDanhSachSach); // ← thêm
    }

    private void hienThiThongTinSach(Sach sach) {

        
        tvProductNameDetail.setText(sach.getTen());
        tvPriceDetail.setText(String.format("%,.0fđ", sach.getGia()));
        tvMaHang.setText(String.valueOf(sach.getId()));
        tvNhaCungCap.setText(sach.getTacGia());
        tvTacGia.setText(sach.getTacGia());
        tvNhaXuatBan.setText(sach.getTheLoai());
        tvNamXuatBan.setText("2024");
        tvProductDescription.setText(sach.getMoTa()); // ← đang set getMoTa()

        String tenAnh = sach.getHinhAnh().trim();
        if (!tenAnh.isEmpty()) {
            int resId = getResources().getIdentifier(tenAnh, "drawable", getPackageName());
            if (resId != 0) imgProductLarge.setImageResource(resId);
        }
    }

    private void hienThiDeXuat(Sach sachHienTai) {
        // Phần 1: Đề xuất cùng thể loại (bỏ sách hiện tại)
        List<Sach> dsDeXuat = db.getSachTheoTheLoai(sachHienTai.getTheLoai());
        dsDeXuat.removeIf(s -> s.getId() == sachHienTai.getId());
        rvSachDeXuat.setLayoutManager(new GridLayoutManager(this, 2));
        if (dsDeXuat.isEmpty()) {
            // Nếu không có cùng thể loại → ẩn phần đề xuất
            rvSachDeXuat.setVisibility(View.GONE);
        } else {
            rvSachDeXuat.setVisibility(View.VISIBLE);
            rvSachDeXuat.setAdapter(new SachDeXuatAdapter(dsDeXuat, sach -> {
                Intent intent = new Intent(this, ThongTinSanPhamActivity.class);
                intent.putExtra("SACH_ID", sach.getId());
                startActivity(intent);
            }));
        }

        // Phần 2: Tất cả sách (bỏ sách hiện tại)
        List<Sach> tatCaSach = db.getAllSach();
        tatCaSach.removeIf(s -> s.getId() == sachHienTai.getId());
        rvDanhSachSach.setLayoutManager(new GridLayoutManager(this, 2));
        rvDanhSachSach.setAdapter(new SachDeXuatAdapter(tatCaSach, sach -> {
            Intent intent = new Intent(this, ThongTinSanPhamActivity.class);
            intent.putExtra("SACH_ID", sach.getId());
            startActivity(intent);
        }));
    }

    private void setupSoLuong() {
        tvQuantity.setText(String.valueOf(soLuong));
        btnMinus.setOnClickListener(v -> {
            if (soLuong > 1) {
                soLuong--;
                tvQuantity.setText(String.valueOf(soLuong));
            } else {
                Toast.makeText(this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });
        btnPlus.setOnClickListener(v -> {
            soLuong++;
            tvQuantity.setText(String.valueOf(soLuong));
        });
    }

    private void setupNutHanhDong() {
        btnAddToCart.setOnClickListener(v -> {
            db.themVaoGioHang(khachHangId, sachHienTai.getId(), soLuong);
            db.themThongBao(khachHangId, "Thêm vào giỏ hàng thành công!",
                    "Bạn vừa thêm " + soLuong + " cuốn \"" + sachHienTai.getTen() + "\" vào giỏ hàng.",
                    DatabaseHelper.LOAI_GIO_HANG, (int) -1);
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            List<int[]> dsSach = new ArrayList<>();
            dsSach.add(new int[]{sachHienTai.getId(), soLuong});
            DatHangDialog dialog = new DatHangDialog(this, khachHangId, sachHienTai.getGia() * soLuong, dsSach, (donHang, ds) -> {
                Intent intent = new Intent(this, TrangChuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
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
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, ThongBaoActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
                return true;
            }
            return false;
        });
    }

    static class SachDeXuatAdapter extends RecyclerView.Adapter<SachDeXuatAdapter.ViewHolder> {
        interface OnClickSach { void onClick(Sach sach); }
        List<Sach> dsSach;
        OnClickSach listener;
        SachDeXuatAdapter(List<Sach> dsSach, OnClickSach listener) {
            this.dsSach = dsSach;
            this.listener = listener;
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBia;
            TextView tvTen, tvGia;
            ViewHolder(View itemView) {
                super(itemView);
                imgBia = itemView.findViewById(R.id.imgBiaSach);
                tvTen  = itemView.findViewById(R.id.tvTenSach);
                tvGia  = itemView.findViewById(R.id.tvGiaSach);
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Sach sach = dsSach.get(position);
            holder.tvTen.setText(sach.getTen());
            holder.tvGia.setText(String.format("%,.0fđ", sach.getGia()));
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
}