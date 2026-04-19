package com.example.btl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import Class.DatabaseHelper;
import Class.KhachHang;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextInputEditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Kiểm tra nếu đã đăng nhập thì vào thẳng trang chủ
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (pref.getInt("userId", -1) != -1) {
            startActivity(new Intent(this, TrangChuActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            KhachHang kh = db.dangNhap(email, pass);
            if (kh != null) {
                // Lưu session
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("userId", kh.getId());
                editor.putString("userName", kh.getTen());
                editor.apply();

                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TrangChuActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}