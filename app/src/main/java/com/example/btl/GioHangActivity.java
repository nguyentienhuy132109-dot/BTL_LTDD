package com.example.btl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Class.DatabaseHelper;
import Class.GioHangItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {

    DatabaseHelper db;
    List<GioHangItem> dsGioHang;
    GioHangAdapter adapter;

    TextView tvTotalAmount;
    AppCompatButton btnCheckout;
    BottomNavigationView bottomNav;

    // TODO: Thay bằng ID khách đăng nhập thực tế
    int khachHangId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gio_hang);
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        khachHangId = pref.getInt("userId", -1);
        if (khachHangId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.gio_hang), (v, insets) -> {
                    androidx.core.graphics.Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);
                    return insets;
                });

        db = new DatabaseHelper(this);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnCheckout   = findViewById(R.id.btnCheckout);
        bottomNav     = findViewById(R.id.bottom_navigation);

        // 1. Load giỏ hàng từ DB
        taiGioHang();

        // 2. Nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            List<GioHangItem> dsChon = adapter.getDsChon();
            if (dsChon.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Tính tổng tiền
            double tongTien = 0;
            for (GioHangItem item : dsChon) tongTien += item.getThanhTien();

            // Chuyển sang định dạng {sachId, soLuong}
            List<int[]> dsSach = new ArrayList<>();
            for (GioHangItem item : dsChon) {
                dsSach.add(new int[]{item.getSachId(), item.getSoLuong()});
            }

            DatHangDialog dialog = new DatHangDialog(
                    this,
                    khachHangId,
                    tongTien,
                    dsSach,
                    (donHang, ds) -> {
                        // Xóa các sản phẩm đã mua khỏi giỏ hàng
                        for (GioHangItem item : dsChon) {
                            db.xoaKhoiGioHang(item.getId());
                        }
                        // Về trang chủ
                        Intent intent = new Intent(this, TrangChuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
            );
            dialog.show();
        });
        // 3. Bottom navigation
        setupBottomNav();
    }

    // Load lại giỏ hàng (gọi khi quay lại từ màn hình khác)
    @Override
    protected void onResume() {
        super.onResume();
        taiGioHang();
    }

    private void taiGioHang() {
        dsGioHang = db.getGioHang(khachHangId);

        // Sửa XML: đổi ScrollView thành RecyclerView
        RecyclerView rvGioHang = findViewById(R.id.rvGioHang);
        rvGioHang.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GioHangAdapter(dsGioHang,
                // Callback tăng/giảm số lượng
                (item, soLuongMoi) -> {
                    db.capNhatSoLuong(item.getId(), soLuongMoi);
                    capNhatTongTien();
                },
                // Callback xóa sản phẩm
                item -> {
                    db.xoaKhoiGioHang(item.getId());
                    dsGioHang.remove(item);
                    adapter.notifyDataSetChanged();
                    capNhatTongTien();
                    Toast.makeText(this, "Đã xóa khỏi giỏ hàng",
                            Toast.LENGTH_SHORT).show();
                },
                // Callback tick chọn
                () -> capNhatTongTien()
        );

        rvGioHang.setAdapter(adapter);
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        double tong = 0;
        for (GioHangItem item : adapter.getDsChon()) {
            tong += item.getThanhTien();
        }
        tvTotalAmount.setText(String.format("%,.0fđ", tong));
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_cart);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, TrangChuActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                return true;
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, ThongBaoActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                // startActivity(new Intent(this, ThongTinTaiKhoanActivity.class));
                return true;
            }
            return false;
        });
    }

    // ==================== ADAPTER GIỎ HÀNG ====================
    static class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {

        interface OnSoLuongChange { void onChange(GioHangItem item, int soLuongMoi); }
        interface OnXoa { void onXoa(GioHangItem item); }
        interface OnChon { void onChon(); }

        List<GioHangItem> dsGioHang;
        OnSoLuongChange onSoLuongChange;
        OnXoa onXoa;
        OnChon onChon;

        GioHangAdapter(List<GioHangItem> ds,
                       OnSoLuongChange onSoLuongChange,
                       OnXoa onXoa,
                       OnChon onChon) {
            this.dsGioHang = ds;
            this.onSoLuongChange = onSoLuongChange;
            this.onXoa = onXoa;
            this.onChon = onChon;
        }

        // Lấy danh sách được tick chọn
        public List<GioHangItem> getDsChon() {
            List<GioHangItem> dsChon = new java.util.ArrayList<>();
            for (GioHangItem item : dsGioHang) {
                if (item.isChon()) dsChon.add(item);
            }
            return dsChon;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox cbSelect;
            ImageView imgProduct;
            TextView tvProductName, tvProductPrice, tvQuantity;
            AppCompatButton btnMinus, btnPlus;

            ViewHolder(View itemView) {
                super(itemView);
                cbSelect      = itemView.findViewById(R.id.cbSelect);
                imgProduct    = itemView.findViewById(R.id.imgProduct);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvProductPrice= itemView.findViewById(R.id.tvProductPrice);
                tvQuantity    = itemView.findViewById(R.id.tvQuantity);
                btnMinus      = itemView.findViewById(R.id.btnMinus);
                btnPlus       = itemView.findViewById(R.id.btnPlus);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gio_hang, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            GioHangItem item = dsGioHang.get(position);

            holder.tvProductName.setText(item.getTenSach());
            holder.tvProductPrice.setText(
                    String.format("%,.0fđ", item.getThanhTien()));
            holder.tvQuantity.setText(String.valueOf(item.getSoLuong()));
            holder.cbSelect.setChecked(item.isChon());

            // Set ảnh
            String tenAnh = item.getHinhAnh().trim();
            if (!tenAnh.isEmpty()) {
                int resId = holder.itemView.getContext().getResources()
                        .getIdentifier(tenAnh, "drawable",
                                holder.itemView.getContext().getPackageName());
                if (resId != 0) holder.imgProduct.setImageResource(resId);
            }

            // Tick chọn
            holder.cbSelect.setOnCheckedChangeListener((btn, isChecked) -> {
                item.setChon(isChecked);
                onChon.onChon();
            });

            // Giảm số lượng
            holder.btnMinus.setOnClickListener(v -> {
                int sl = item.getSoLuong();
                if (sl > 1) {
                    item.setSoLuong(sl - 1);
                    holder.tvQuantity.setText(String.valueOf(item.getSoLuong()));
                    holder.tvProductPrice.setText(
                            String.format("%,.0fđ", item.getThanhTien()));
                    onSoLuongChange.onChange(item, item.getSoLuong());
                } else {
                    // Số lượng = 1 → xóa
                    onXoa.onXoa(item);
                }
            });

            // Tăng số lượng
            holder.btnPlus.setOnClickListener(v -> {
                item.setSoLuong(item.getSoLuong() + 1);
                holder.tvQuantity.setText(String.valueOf(item.getSoLuong()));
                holder.tvProductPrice.setText(
                        String.format("%,.0fđ", item.getThanhTien()));
                onSoLuongChange.onChange(item, item.getSoLuong());
            });
        }

        @Override
        public int getItemCount() { return dsGioHang.size(); }
    }
}