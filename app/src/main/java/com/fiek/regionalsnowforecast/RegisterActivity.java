package com.fiek.regionalsnowforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Region;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

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
                    if (checkRegisterInput()) {
                        long affected = objDb.insert(Database.tblUsers, null, cv);
                        if (affected > 0) {
                            Toast.makeText(RegisterActivity.this, "Signed up successfully!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Log.e("asd", ex.getMessage());
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

    public static boolean isEmail(EditText etInput) {
        CharSequence email = etInput.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isEmpty(EditText etInput) {
        CharSequence str = etInput.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static boolean passwordsMatch(EditText etPass, EditText etConfirmPass) {
        CharSequence strPass = etPass.getText().toString();
        CharSequence strConfirmPass = etConfirmPass.getText().toString();
        return strPass.equals(strConfirmPass);
    }

    public static boolean passwordValidation(EditText etInput) {
        CharSequence strPassword = etInput.getText().toString();
        Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$\\.]{8,24}");

        return !TextUtils.isEmpty(strPassword) && PASSWORD_PATTERN.matcher(strPassword).matches();
    }

    public boolean emailAvailibility(EditText etInput){
        CharSequence strEmail = etInput.getText().toString();
        String email = strEmail.toString();
        SQLiteDatabase objDb = new Database(RegisterActivity.this, "RSFDB").getReadableDatabase();
        Cursor c = objDb.query(Database.tblUsers,
                new String[]{User.ID, User.Name, User.Email, User.Address, User.Region,User.Password},
                User.Email + "=?" , new String[]{email}, "", "", "");
        boolean exists = false;
        if(c.getCount() == 1){
            c.moveToFirst();
            String dbEmail = c.getString(2);
            if(strEmail.equals(dbEmail)){
                exists = true;
            }
        }
        return exists;
    }


    public boolean checkRegisterInput() {
        if (isEmpty(etName)) {
            etName.setError(getText(R.string.nameerror));
            etName.requestFocus();
            return false;
        } else if (!isEmail(etEmail)) {
            etEmail.setError(getText(R.string.emailerror));
            etEmail.requestFocus();
            return false;
        } else if (isEmpty(etAddress)) {
            etAddress.setError(getText(R.string.addresserror));
            etAddress.requestFocus();
            return false;
        } else if (isEmpty(etRegion)) {
            etRegion.setError(getText(R.string.regionerror));
            etRegion.requestFocus();
            return false;
        } else if (!passwordValidation(etPassword)) {
            etPassword.setError(getText(R.string.passwordlengtherror));
            etPassword.requestFocus();
            return false;
        } else if (isEmpty(etConfirmPassword)) {
            etConfirmPassword.setError(getText(R.string.emptyconfirmpassword));
            etConfirmPassword.requestFocus();
            return false;
        } else if (!passwordsMatch(etPassword, etConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, getText(R.string.passwordmatcherror) , Toast.LENGTH_LONG).show();
            return false;
        } else if (emailAvailibility(etEmail)){
            Toast.makeText(RegisterActivity.this, getText(R.string.accountalreadyexists), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
}
