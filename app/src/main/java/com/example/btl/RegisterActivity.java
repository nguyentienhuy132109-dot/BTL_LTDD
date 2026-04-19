package com.example.btl;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import Class.DatabaseHelper;
import Class.KhachHang;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextInputEditText edtFullName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    Button btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        
        // Ánh xạ
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            String name = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();
            String confirmPass = edtConfirmPassword.getText().toString().trim();

            // Validate dữ liệu
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                edtConfirmPassword.setError("Mật khẩu không khớp");
                edtConfirmPassword.requestFocus();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
                return;
            }

            // Thực hiện đăng ký
            KhachHang kh = new KhachHang(name, email, pass, phone);
            long id = db.themKhachHang(kh);

            if (id != -1) {
                Toast.makeText(this, "Chúc mừng! Đăng ký tài khoản thành công", Toast.LENGTH_LONG).show();
                finish(); 
            } else {
                Toast.makeText(this, "Lỗi: Email này đã được đăng ký trước đó!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}