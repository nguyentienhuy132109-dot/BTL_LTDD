package com.example.btl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import Class.DatabaseHelper;
import Class.DonHang;
import Class.KhachHang;

public class ThongTinTaiKhoanActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView tvProfileName, tvProfileEmail, tvProfilePhone;
    Button btnLogout;
    RecyclerView rvOrderHistory;
    BottomNavigationView bottomNav;
    LinearLayout btnChoXacNhan, btnDangXuLy, btnDangGiao, btnHoanTat;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);

        db = new DatabaseHelper(this);
        
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        anhXaView();
        loadThongTin();
        setupBottomNav();
        setupOrderButtons();

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void anhXaView() {
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        btnLogout = findViewById(R.id.btnLogout);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        bottomNav = findViewById(R.id.bottom_navigation);
        
        btnChoXacNhan = findViewById(R.id.btnChoXacNhan);
        btnDangXuLy = findViewById(R.id.btnDangXuLy);
        btnDangGiao = findViewById(R.id.btnDangGiao);
        btnHoanTat = findViewById(R.id.btnHoanTat);
    }

    private void loadThongTin() {
        KhachHang kh = db.getKhachHangById(userId);
        if (kh != null) {
            tvProfileName.setText(kh.getTen());
            tvProfileEmail.setText(kh.getEmail());
            // Hiển thị số điện thoại (Sửa lỗi hiển thị nhầm mật khẩu)
            tvProfilePhone.setText(kh.getSdt());
        }
    }

    private void setupOrderButtons() {
        btnChoXacNhan.setOnClickListener(v -> filterOrders("Chờ xác nhận"));
        btnDangXuLy.setOnClickListener(v -> filterOrders("Đang xử lý"));
        btnDangGiao.setOnClickListener(v -> filterOrders("Đang giao hàng"));
        btnHoanTat.setOnClickListener(v -> filterOrders("Hoàn tất"));
    }

    private void filterOrders(String trangThai) {
        List<DonHang> allOrders = db.getDonHangByKhachHang(userId);
        List<DonHang> filteredList = new ArrayList<>();
        
        for (DonHang dh : allOrders) {
            if (dh.getTrangThai().equals(trangThai)) {
                filteredList.add(dh);
            }
        }

        if (filteredList.isEmpty()) {
            rvOrderHistory.setVisibility(View.GONE);
            Toast.makeText(this, "Không có đơn hàng nào: " + trangThai, Toast.LENGTH_SHORT).show();
        } else {
            rvOrderHistory.setVisibility(View.VISIBLE);
            rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
            rvOrderHistory.setAdapter(new OrderAdapter(filteredList));
        }
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, TrangChuActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, GioHangActivity.class));
                return true;
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, ThongBaoActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        List<DonHang> list;
        OrderAdapter(List<DonHang> list) { this.list = list; }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang, parent, false);
            return new OrderViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            DonHang dh = list.get(position);
            holder.tvMa.setText("Đơn hàng #" + dh.getId());
            holder.tvNgay.setText("Ngày đặt: " + dh.getNgayDat());
            holder.tvGia.setText(String.format("%,.0fđ", dh.getTongTien()));
            holder.tvTrangThai.setText(dh.getTrangThai());
            holder.tvDiaChi.setText("Giao đến: " + dh.getDiaChiGiao());

            if (dh.getTrangThai().equals("Chờ xác nhận")) {
                holder.tvTrangThai.setBackgroundColor(0xFFFFA000);
            } else {
                holder.tvTrangThai.setBackgroundColor(0xFF43A047);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ThongTinTaiKhoanActivity.this, ChiTietDonHangActivity.class);
                intent.putExtra("DON_HANG_ID", dh.getId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView tvMa, tvNgay, tvGia, tvTrangThai, tvDiaChi;
            OrderViewHolder(View itemView) {
                super(itemView);
                tvMa = itemView.findViewById(R.id.tvMaDonHang);
                tvNgay = itemView.findViewById(R.id.tvNgayDat);
                tvGia = itemView.findViewById(R.id.tvTongTien);
                tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
                tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            }
        }
    }
}