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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Class.ThongBao;
import Class.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ThongBaoActivity extends AppCompatActivity {

    DatabaseHelper db;
    List<ThongBao> dsThongBao;
    ThongBaoAdapter adapter;
<<<<<<< HEAD
    int khachHangId;
=======
    int khachHangId = 1; // TODO: thay bằng ID thực
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_bao);

<<<<<<< HEAD
        // Lấy userId thực từ session
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        khachHangId = pref.getInt("userId", -1);
        
        if (khachHangId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

=======
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.thong_bao), (v, insets) -> {
                    androidx.core.graphics.Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        db = new DatabaseHelper(this);

        // Load danh sách thông báo
        taiThongBao();

        // Bottom navigation
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiThongBao(); // Tải lại khi quay về
    }

    private void taiThongBao() {
        dsThongBao = db.getThongBao(khachHangId);

        RecyclerView rvThongBao = findViewById(R.id.rvThongBao);
<<<<<<< HEAD
        View layoutTrong = findViewById(R.id.layoutTrong);

        if (dsThongBao.isEmpty()) {
            layoutTrong.setVisibility(View.VISIBLE);
            rvThongBao.setVisibility(View.GONE);
        } else {
            layoutTrong.setVisibility(View.GONE);
=======
        View layoutTrong = findViewById(R.id.layoutTrong); // ← sửa từ tvTrong

        if (dsThongBao.isEmpty()) {
            layoutTrong.setVisibility(View.VISIBLE); // ← sửa
            rvThongBao.setVisibility(View.GONE);
        } else {
            layoutTrong.setVisibility(View.GONE);    // ← sửa
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            rvThongBao.setVisibility(View.VISIBLE);

            rvThongBao.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ThongBaoAdapter(dsThongBao, thongBao -> {
                db.danhDauDaDoc(thongBao.getId());
                thongBao.setDaDoc(1);
                adapter.notifyDataSetChanged();

                if (thongBao.getLoai().equals(DatabaseHelper.LOAI_GIO_HANG)) {
                    startActivity(new Intent(this, GioHangActivity.class));
                }
            });
            rvThongBao.setAdapter(adapter);
        }
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_notifications);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, TrangChuActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, GioHangActivity.class));
                return true;
            } else if (id == R.id.nav_notifications) {
                return true;
            } else if (id == R.id.nav_profile) {
<<<<<<< HEAD
                startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
=======
               // startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
                return true;
            }
            return false;
        });
    }

<<<<<<< HEAD
=======
    // ==================== ADAPTER ====================
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
    static class ThongBaoAdapter extends
            RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {

        interface OnClick { void onClick(ThongBao thongBao); }

        List<ThongBao> dsThongBao;
        OnClick listener;

        ThongBaoAdapter(List<ThongBao> ds, OnClick listener) {
            this.dsThongBao = ds;
            this.listener = listener;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgIcon;
            TextView tvTieuDe, tvNoiDung, tvThoiGian;
            View viewChuaDoc;

            ViewHolder(View itemView) {
                super(itemView);
                imgIcon     = itemView.findViewById(R.id.imgIconThongBao);
                tvTieuDe    = itemView.findViewById(R.id.tvTieuDeThongBao);
                tvNoiDung   = itemView.findViewById(R.id.tvNoiDungThongBao);
                tvThoiGian  = itemView.findViewById(R.id.tvThoiGianThongBao);
                viewChuaDoc = itemView.findViewById(R.id.viewChuaDoc);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_thong_bao, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ThongBao tb = dsThongBao.get(position);

            holder.tvTieuDe.setText(tb.getTieuDe());
            holder.tvNoiDung.setText(tb.getNoiDung());
            holder.tvThoiGian.setText(tb.getThoiGian());

<<<<<<< HEAD
=======
            // Chưa đọc → nền xanh nhạt + chấm đỏ
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            if (!tb.isDaDoc()) {
                holder.itemView.setBackgroundColor(0xFFE3F2FD);
                holder.viewChuaDoc.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setBackgroundColor(0xFFFFFFFF);
                holder.viewChuaDoc.setVisibility(View.GONE);
            }

<<<<<<< HEAD
=======
            // Icon theo loại
>>>>>>> add8799f82b5f791fc382c6ee8100e86bcd7a293
            if (tb.getLoai().equals(DatabaseHelper.LOAI_GIO_HANG)) {
                holder.imgIcon.setImageResource(
                        android.R.drawable.ic_menu_add);
            } else {
                holder.imgIcon.setImageResource(
                        android.R.drawable.ic_menu_send);
            }

            holder.itemView.setOnClickListener(v -> listener.onClick(tb));
        }

        @Override
        public int getItemCount() { return dsThongBao.size(); }
    }
}