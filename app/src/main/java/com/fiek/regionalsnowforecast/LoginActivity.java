package com.fiek.regionalsnowforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    ImageView ivLoginBack;
    TextView tvLoginRegister;
    EditText etEmail, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivLoginBack = findViewById(R.id.ivLoginBack);
        tvLoginRegister = findViewById(R.id.tvLoginRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        ivLoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        tvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAuthentication(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

    }

    public void loginAuthentication(String email, String password){
        SQLiteDatabase objDb = new Database(LoginActivity.this, "RSFDB").getReadableDatabase();
        Cursor c = objDb.query(Database.tblUsers,
                new String[]{User.ID, User.Name, User.Email, User.Address, User.Region,User.Password},
                User.Email + "=?" , new String[]{email}, "", "", "");
        if(c.getCount()==1){
            c.moveToFirst();
            String dbEmail = c.getString(2);
            String dbPassword = c.getString(5);

            if(email.equals(dbEmail) && password.equals(dbPassword)){
                Toast.makeText(LoginActivity.this, "You have logged in successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Email or password is incorrect.", Toast.LENGTH_LONG).show();
                etEmail.requestFocus();
                etPassword.requestFocus();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Email or password is incorrect.", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            etPassword.requestFocus();
        }
    }
}
