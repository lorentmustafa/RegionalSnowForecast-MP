package com.fiek.regionalsnowforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    ImageView ivRegisterBack;
    TextView tvBackToLogin;
    EditText etName, etEmail, etAddress, etRegion, etPassword, etConfirmPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ivRegisterBack = findViewById(R.id.ivRegisterBack);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etRegion = findViewById(R.id.etRegion);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(User.Name, etName.getText().toString());
                cv.put(User.Email, etEmail.getText().toString());
                cv.put(User.Address, etAddress.getText().toString());
                cv.put(User.Region, etRegion.getText().toString());
                cv.put(User.Password, etPassword.getText().toString());

                SQLiteDatabase objDb = new Database(RegisterActivity.this, "RSFDB").getWritableDatabase();

                try {
                    if(etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                        long affected = objDb.insert(Database.tblUsers, null, cv);
                        if(affected > 0) {
                            Toast.makeText(RegisterActivity.this, "Signed up successfully!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match. \n Could you check again?", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Log.e("Failed to insert user", ex.getMessage());
                } finally {
                    objDb.close();
                }

            }
        });

        ivRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
