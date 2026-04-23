package com.example.btl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import Class.DatabaseHelper;
import Class.KhachHang;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextInputEditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvRegister;
    MaterialCheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        cbRemember = findViewById(R.id.cbRemember);

        // Kiểm tra nếu có lưu tài khoản thì điền vào
        boolean isRemembered = pref.getBoolean("isRemembered", false);
        if (isRemembered) {
            edtEmail.setText(pref.getString("rememberedEmail", ""));
            edtPassword.setText(pref.getString("rememberedPass", ""));
            cbRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            KhachHang kh = db.dangNhap(email, pass);
            if (kh != null) {
                // Lưu session và thông tin ghi nhớ
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("userId", kh.getId());
                editor.putString("userName", kh.getTen());
                
                if (cbRemember.isChecked()) {
                    editor.putBoolean("isRemembered", true);
                    editor.putString("rememberedEmail", email);
                    editor.putString("rememberedPass", pass);
                } else {
                    editor.putBoolean("isRemembered", false);
                    editor.remove("rememberedEmail");
                    editor.remove("rememberedPass");
                }
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