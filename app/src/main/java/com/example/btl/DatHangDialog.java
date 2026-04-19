package com.example.btl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Class.DatabaseHelper;
import Class.DonHang;
import Class.KhachHang;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatHangDialog extends Dialog {

    // Interface callback khi xác nhận đặt hàng
    public interface OnDatHangListener {
        void onXacNhan(DonHang donHang, List<int[]> dsSachVaSoLuong);
        // int[] = {sachId, soLuong}
    }

    Context context;
    DatabaseHelper db;
    int khachHangId;
    double tongTien;
    List<int[]> dsSachVaSoLuong; // danh sách {sachId, soLuong}
    OnDatHangListener listener;

    // Views
    EditText edtTenNguoiNhan, edtSdt, edtDiaChi;
    Button btnLayThongTin, btnXacNhan, btnHuy;
    TextView tvTongTien;

    public DatHangDialog(Context context, int khachHangId,
                         double tongTien, List<int[]> dsSachVaSoLuong,
                         OnDatHangListener listener) {
        super(context);
        this.context = context;
        this.khachHangId = khachHangId;
        this.tongTien = tongTien;
        this.dsSachVaSoLuong = dsSachVaSoLuong;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_dat_hang);

        // Cho dialog rộng hơn
        getWindow().setLayout(
                (int)(context.getResources().getDisplayMetrics().widthPixels * 0.92f),
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Ánh xạ view
        edtTenNguoiNhan = findViewById(R.id.edtTenNguoiNhan);
        edtSdt          = findViewById(R.id.edtSdt);
        edtDiaChi       = findViewById(R.id.edtDiaChi);
        btnLayThongTin  = findViewById(R.id.btnLayThongTin);
        btnXacNhan      = findViewById(R.id.btnXacNhan);
        btnHuy          = findViewById(R.id.btnHuy);
        tvTongTien      = findViewById(R.id.tvTongTienDialog);

        // Hiển thị tổng tiền
        tvTongTien.setText(String.format("Tổng tiền: %,.0fđ", tongTien));

        // Nút lấy thông tin khách hàng
        btnLayThongTin.setOnClickListener(v -> layThongTinKhachHang());

        // Nút xác nhận đặt hàng
        btnXacNhan.setOnClickListener(v -> xacNhanDatHang());

        // Nút hủy
        btnHuy.setOnClickListener(v -> dismiss());
    }

    // ==================== LẤY THÔNG TIN KHÁCH HÀNG ====================
    private void layThongTinKhachHang() {
        KhachHang kh = db.getKhachHangById(khachHangId);
        if (kh != null) {
            edtTenNguoiNhan.setText(kh.getTen());
            edtSdt.setText(kh.getSdt());
            Toast.makeText(context,
                    "Đã điền thông tin của " + kh.getTen(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,
                    "Không tìm thấy thông tin khách hàng!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // ==================== XÁC NHẬN ĐẶT HÀNG ====================
    private void xacNhanDatHang() {
        String ten    = edtTenNguoiNhan.getText().toString().trim();
        String sdt    = edtSdt.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();

        // Validate
        if (ten.isEmpty()) {
            edtTenNguoiNhan.setError("Vui lòng nhập tên người nhận!");
            edtTenNguoiNhan.requestFocus();
            return;
        }
        if (sdt.isEmpty()) {
            edtSdt.setError("Vui lòng nhập số điện thoại!");
            edtSdt.requestFocus();
            return;
        }
        if (sdt.length() < 10) {
            edtSdt.setError("Số điện thoại không hợp lệ!");
            edtSdt.requestFocus();
            return;
        }
        if (diaChi.isEmpty()) {
            edtDiaChi.setError("Vui lòng nhập địa chỉ giao hàng!");
            edtDiaChi.requestFocus();
            return;
        }

        // Tạo ngày đặt hàng
        String ngayDat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date());

        // Tạo object DonHang
        DonHang donHang = new DonHang(
                khachHangId,
                ngayDat,
                tongTien,
                diaChi,
                ten,
                sdt
        );

        // Lưu vào DB
        long donHangId = db.themDonHang(donHang);
        if (donHangId == -1) {
            Toast.makeText(context,
                    "Đặt hàng thất bại, vui lòng thử lại!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu chi tiết đơn hàng
        for (int[] item : dsSachVaSoLuong) {
            int sachId   = item[0];
            int soLuong  = item[1];
            double giaBan = db.getSachById(sachId).getGia();

//            Class.ChiTietDonHang chiTiet = new Class.ChiTietDonHang(
//                    (int) donHangId, sachId, soLuong, giaBan);
//            db.themChiTietDonHang(chiTiet);
        }

        // Thêm thông báo đặt hàng thành công
        db.themThongBao(khachHangId,
                "Đặt hàng thành công! 🎉",
                "Đơn hàng #" + donHangId + " của bạn đã được xác nhận. " +
                        "Giao đến: " + diaChi,
                DatabaseHelper.LOAI_DON_HANG);

        Toast.makeText(context,
                "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

        // Callback về Activity
        donHang.setId((int) donHangId);
        listener.onXacNhan(donHang, dsSachVaSoLuong);

        dismiss();
    }
}