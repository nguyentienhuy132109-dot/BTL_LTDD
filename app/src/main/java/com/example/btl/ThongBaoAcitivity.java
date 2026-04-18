package com.example.btl;

import android.content.Intent;
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
    int khachHangId = 1; // TODO: thay bằng ID thực

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_bao);

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
        TextView tvTrong = findViewById(R.id.tvTrong);

        if (dsThongBao.isEmpty()) {
            tvTrong.setVisibility(View.VISIBLE);
            rvThongBao.setVisibility(View.GONE);
        } else {
            tvTrong.setVisibility(View.GONE);
            rvThongBao.setVisibility(View.VISIBLE);

            rvThongBao.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ThongBaoAdapter(dsThongBao, thongBao -> {
                // Click vào thông báo → đánh dấu đã đọc
                db.danhDauDaDoc(thongBao.getId());
                thongBao.setDaDoc(1);
                adapter.notifyDataSetChanged();

                // Điều hướng theo loại thông báo
                if (thongBao.getLoai().equals(DatabaseHelper.LOAI_GIO_HANG)) {
                    startActivity(new Intent(this, GioHangActivity.class));
                } else if (thongBao.getLoai().equals(DatabaseHelper.LOAI_DON_HANG)) {
                    startActivity(new Intent(this, LichSuDonHangActivity.class));
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
                startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
                return true;
            }
            return false;
        });
    }

    // ==================== ADAPTER ====================
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

            // Chưa đọc → nền xanh nhạt + chấm đỏ
            if (!tb.isDaDoc()) {
                holder.itemView.setBackgroundColor(0xFFE3F2FD);
                holder.viewChuaDoc.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setBackgroundColor(0xFFFFFFFF);
                holder.viewChuaDoc.setVisibility(View.GONE);
            }

            // Icon theo loại
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