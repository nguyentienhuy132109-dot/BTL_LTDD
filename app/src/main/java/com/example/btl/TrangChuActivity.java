package com.example.btl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import Class.DatabaseHelper;
import Class.Sach;

public class TrangChuActivity extends AppCompatActivity {

    DatabaseHelper db;
    GridLayout gridProducts;
    AutoCompleteTextView edtTimKiem;
    BottomNavigationView bottomNav;

    String[] dsTheLoai = {
            "Trinh thám", "Thiếu nhi", "Tiểu thuyết",
            "Kinh doanh", "Kinh dị", "Thiếu nhi",
            "Lịch sử", "Khoa học", "Văn học", "Ngoại ngữ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (pref.getInt("userId", -1) == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.trang_chu);

        db = new DatabaseHelper(this);
        gridProducts = findViewById(R.id.grid_products);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        bottomNav = findViewById(R.id.bottom_navigation);

        hienThiDanhSachSach(db.getAllSach());
        setupTimKiem();
        setupTheLoai();
        setupBottomNav();
    }

    private void setupTimKiem() {
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tuKhoa = s.toString().trim();
                if (tuKhoa.isEmpty()) {
                    hienThiDanhSachSach(db.getAllSach());
                    edtTimKiem.dismissDropDown();
                } else {
                    List<Sach> ketQua = db.timKiemSach(tuKhoa);
                    hienThiDanhSachSach(ketQua);

                    List<String> tenSach = new ArrayList<>();
                    for (Sach s2 : ketQua) tenSach.add(s2.getTen());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            TrangChuActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            tenSach
                    );
                    edtTimKiem.setAdapter(adapter);
                    edtTimKiem.showDropDown();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtTimKiem.setOnItemClickListener((parent, view, position, id) -> {
            String tenChon = (String) parent.getItemAtPosition(position);
            List<Sach> ketQua = db.timKiemSach(tenChon);
            hienThiDanhSachSach(ketQua);
        });
    }

    private void setupTheLoai() {
        GridLayout gridTheLoai = findViewById(R.id.grid_categories);
        if (gridTheLoai != null) {
            for (int i = 0; i < gridTheLoai.getChildCount(); i++) {
                if (i >= dsTheLoai.length) break;
                View item = gridTheLoai.getChildAt(i);
                final String theLoai = dsTheLoai[i];
                item.setOnClickListener(v -> {
                    List<Sach> dsSach = db.getSachTheoTheLoai(theLoai);
                    if (dsSach.isEmpty()) {
                        Toast.makeText(this, "Không có sách thể loại: " + theLoai, Toast.LENGTH_SHORT).show();
                    } else {
                        hienThiDanhSachSach(dsSach);
                    }
                });
            }
        }
    }

    private void hienThiDanhSachSach(List<Sach> dsSach) {
        gridProducts.removeAllViews();
        for (Sach sach : dsSach) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_sach, gridProducts, false);
            ImageView imgBia = cardView.findViewById(R.id.imgBiaSach);
            TextView tvTen = cardView.findViewById(R.id.tvTenSach);
            TextView tvGia = cardView.findViewById(R.id.tvGiaSach);

            tvTen.setText(sach.getTen());
            tvGia.setText(String.format("%,.0fđ", sach.getGia()));

            String tenAnh = sach.getHinhAnh().trim();
            if (!tenAnh.isEmpty()) {
                int resId = getResources().getIdentifier(tenAnh, "drawable", getPackageName());
                if (resId != 0) imgBia.setImageResource(resId);
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.width = 0;
            params.setMargins(8, 8, 8, 8);
            cardView.setLayoutParams(params);

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(this, ThongTinSanPhamActivity.class);
                intent.putExtra("SACH_ID", sach.getId());
                startActivity(intent);
            });

            gridProducts.addView(cardView);
        }
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
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
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}