package com.fiek.regionalsnowforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    Utils utils = new Utils();

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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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


    }

    @Override
    protected void onStart() {
        super.onStart();

        String userId = utils.getSessionId(LoginActivity.this);

        Toast.makeText(LoginActivity.this, "UserId: "+userId, Toast.LENGTH_SHORT).show();

        if(userId != "remove") {
            Intent intent = new Intent(LoginActivity.this, ResortsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "User not logged in!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
        }
    }


    public void login(View view) {

        loginAuthentication(etEmail, etPassword);

    }

    public void loginAuthentication(EditText etEmail, EditText etPassword){
        String email = etEmail.getText().toString();
        String hashedPassword = utils.SHA256(etPassword);
        SQLiteDatabase objDb = new Database(LoginActivity.this, "RSFDB").getReadableDatabase();
        Cursor c = objDb.query(Database.tblUsers,
                new String[]{User.ID, User.Name, User.Email, User.Address, User.Region,User.Password},
                User.Email + "=?" , new String[]{email}, "", "", "");
        if(c.getCount()==1){
            c.moveToFirst();
            String dbId = c.getString(0);
            String dbEmail = c.getString(2);
            String dbPassword = c.getString(5);


            if(email.equals(dbEmail) && hashedPassword.equals(dbPassword)){
                Toast.makeText(LoginActivity.this, "You have logged in successfully.", Toast.LENGTH_LONG).show();
                utils.saveSession(dbId, dbEmail,LoginActivity.this);
                Toast.makeText(LoginActivity.this, "UserId from DB: "+dbId, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, ResortsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
