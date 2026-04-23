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
        edtTimKiem.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                    actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {

                String tuKhoa = edtTimKiem.getText().toString().trim();
                if (tuKhoa.isEmpty()) {
                    hienThiDanhSachSach(db.getAllSach());
                } else {
                    List<Sach> ketQua = db.timKiemSach(tuKhoa);
                    if (ketQua.isEmpty()) {
                        Toast.makeText(TrangChuActivity.this,
                                "Không tìm thấy: " + tuKhoa,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        hienThiDanhSachSach(ketQua);
                    }
                }

                android.view.inputmethod.InputMethodManager imm =
                        (android.view.inputmethod.InputMethodManager)
                                getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTimKiem.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        edtTimKiem.setOnItemClickListener((parent, view, position, id) -> {
            String tenChon = (String) parent.getItemAtPosition(position);
            List<Sach> ketQua = db.timKiemSach(tenChon);
            hienThiDanhSachSach(ketQua);

            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager)
                            getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtTimKiem.getWindowToken(), 0);
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

        // Set columnCount rõ ràng
        gridProducts.setColumnCount(2);

        for (Sach sach : dsSach) {
            View cardView = LayoutInflater.from(this)
                    .inflate(R.layout.item_sach, gridProducts, false);

            ImageView imgBia = cardView.findViewById(R.id.imgBiaSach);
            TextView tvTen   = cardView.findViewById(R.id.tvTenSach);
            TextView tvGia   = cardView.findViewById(R.id.tvGiaSach);

            tvTen.setText(sach.getTen());
            tvGia.setText(String.format("%,.0fđ", sach.getGia()));

            String tenAnh = sach.getHinhAnh().trim();
            if (!tenAnh.isEmpty()) {
                int resId = getResources().getIdentifier(
                        tenAnh, "drawable", getPackageName());
                if (resId != 0) imgBia.setImageResource(resId);
            }

            // ← Sửa LayoutParams: set cả columnSpec lẫn rowSpec
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.rowSpec    = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.width  = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
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